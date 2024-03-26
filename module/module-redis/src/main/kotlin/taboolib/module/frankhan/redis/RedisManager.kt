package taboolib.module.frankhan.redis

import redis.clients.jedis.Jedis
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPoolConfig
import redis.clients.jedis.StreamEntryID
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.pluginId
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.time.Duration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Function

class RedisManager(config: Configuration) {

    val STREAM = "missingbyriver"
    private var redisPool: JedisPool
    private var password: String
    private var port: Int
    private var host: String
    private var useSSL: Boolean
    private var disabled = false
    private var aliveTime: Int
    private val thread: BlockingThreadTask

    init {
        val poolConfig = JedisPoolConfig()
        aliveTime = config.getInt("aliveTime", 10)
        poolConfig.apply {
            testWhileIdle = true
            timeBetweenEvictionRuns = Duration.ofMillis(30000)
            numTestsPerEvictionRun = -1
            minEvictableIdleTime =
                Duration.ofMillis(config.getInt("MinEvictableIdleTimeMillis", 1800000).toLong())
            maxTotal = config.getInt("MaxTotal", 8)
            maxIdle = config.getInt("MaxIdle", 8)
            minIdle = config.getInt("MinIdle", 1)
            setMaxWait(Duration.ofMillis(config.getInt("MaxWaitMillis", 2).toLong()))
            password = config.getString("password", "")!!
            port = config.getInt("port", 6379)
            host = config.getString("host", "localhost")!!
            useSSL = config.getBoolean("use-ssl", false)
        }
        redisPool = if (password.isBlank()) {
            JedisPool(poolConfig, host, port, 0, useSSL)
        } else {
            JedisPool(poolConfig, host, port, 0, password, useSSL)
        }
        thread = BlockingThreadTask(STREAM)
        info("Redis Service ✅")
    }

    fun getJedis(): Jedis? {
        return redisPool.resource
    }

    fun disable() {
        disabled = true
        if (!redisPool.isClosed) redisPool.close()
    }

    fun publishRedisMessage(server: String, message: String) {
        if (disabled) return
        Thread {
            redisPool.resource.use { jedis ->
                val messages =
                    HashMap<String, String>()
                messages["plugin"] = pluginId
                messages["server"] = server
                messages["message"] = message
                jedis.xadd(STREAM, StreamEntryID.NEW_ENTRY, messages)
            }
        }.start()
    }

    fun expire(key: String, sec: Long): Boolean {
        if (disabled) return false
        return try {
            redisPool.resource.use { jedis ->
                jedis.expire(key, sec)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun get(key: String, name: String): Optional<String> {
        if (disabled) return Optional.empty()
        return try {
            redisPool.resource.use { jedis ->
                val keyB = getRedisKey(key, name)
                val data = jedis.get(keyB)
                if (data != null) {
                    jedis.expire(getRedisKey(key, name), aliveTime.toLong())
                    Optional.of(
                        data
                    )
                } else {
                    Optional.empty()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    fun set(key: String, name: String, value: String, alive: Long = aliveTime.toLong()): Boolean {
        if (disabled) {
            return false
        }
        return try {
            redisPool.resource.use { jedis ->
                jedis.setex(
                    getRedisKey(key, name),
                    alive,
                    value
                )
                jedis.expire(getRedisKey(key, name), alive)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun incr(key: String, name: String, add: Long = 1, safe: Boolean = true): Optional<Long> {
        if (disabled) return Optional.empty()
        return try {
            redisPool.resource.use { jedis ->
                val keyB = getRedisKey(key, name)
                if (safe) {
                    val data = jedis.get(keyB)
                    if (data != null && data.isNotBlank() && data.isNotEmpty()) {
                        Optional.of(jedis.incrBy(keyB, add))
                    } else {
                        Optional.empty()
                    }
                } else {
                    Optional.of(jedis.incrBy(keyB, add))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    fun add2Set(key: String, vararg value: String): Boolean {
        if (disabled) return false
        return try {
            redisPool.resource.use { jedis ->
                if (value.isNotEmpty()) {
                    jedis.sadd(key, *value)
                }
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun delInSet(key: String, value: String): Boolean {
        if (disabled) return false
        return try {
            redisPool.resource.use { jedis ->
                jedis.srem(key, value)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun membersInSet(key: String): Set<String> {
        if (disabled) return emptySet()
        return try {
            redisPool.resource.use { jedis ->
                jedis.smembers(key)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptySet()
        }
    }

    fun sizeOfSet(key: String): Optional<Long> {
        if (disabled) return Optional.empty()
        return try {
            redisPool.resource.use { jedis ->
                Optional.of(jedis.scard(key))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    fun setInMap(key: String, field: String, value: String): Boolean {
        if (disabled) return false
        return try {
            redisPool.resource.use { jedis ->
                jedis.hset(key, field, value)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun delInMap(key: String, vararg field: String): Boolean {
        if (disabled) return false
        return try {
            redisPool.resource.use { jedis ->
                jedis.hdel(key, *field)
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getInMap(key: String, field: String): Optional<String> {
        if (disabled) return Optional.empty()
        return try {
            redisPool.resource.use { jedis ->
                Optional.ofNullable(jedis.hget(key, field))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    fun <T:Any> executeFuture(callback: Function<Jedis, T?>): CompletableFuture<Optional<T>> {
        val future = CompletableFuture<Optional<T>>()
        submitAsync {
            if (disabled) future.complete(Optional.empty())
            try {
                redisPool.resource.use { jedis ->
                    future.complete(Optional.ofNullable(callback.apply(jedis)))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                future.complete(Optional.empty())
            }
        }
        return future
    }

    fun <T:Any> execute(callback: Function<Jedis, T?>): Optional<T> {
        if (disabled) return Optional.empty()
        return try {
            redisPool.resource.use { jedis ->
                Optional.ofNullable(callback.apply(jedis))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Optional.empty()
        }
    }

    private fun getRedisKey(key: String, name: String): String {
        return "$key:$name"
    }

    companion object {
        @Config("redis.yml", autoReload = true)
        lateinit var config: Configuration
        private lateinit var instance: RedisManager

        @Awake(LifeCycle.LOAD)
        fun init() {
            if (config.getBoolean("disable")) return
            try {
                instance = RedisManager(config)
            } catch (e: Exception) {
                severe("Redis Service ❌")
            }
        }

        fun manager(): RedisManager? {
            return if (this::instance.isInitialized) {
                instance
            } else null
        }

        fun disable() {
            if (this::instance.isInitialized) {
                instance.disable()
            }
        }

        fun restart():RedisManager? {
            if (config.getBoolean("disabled")) return null
            return if (this::instance.isInitialized) {
                if (instance.disabled) {
                    instance = RedisManager(config)
                }
                instance
            }else null
        }
    }
}