package taboolib.module.frankhan.redis

import redis.clients.jedis.StreamEntryID
import redis.clients.jedis.params.XReadParams
import redis.clients.jedis.params.XTrimParams
import taboolib.common.event.InternalEventBus
import taboolib.common.platform.function.warning

class BlockingThreadTask(val STREAM: String) {
    private var key:StreamEntryID = StreamEntryID(System.currentTimeMillis())
    private var stopped = false
    init {
        Thread {
            Thread.sleep(10)
            val map = HashMap<String, StreamEntryID>()
            while (!this.stopped) {
                try {
                    val connection = RedisManager.manager()?.getJedis()
                    if (connection == null) {
                        Thread.sleep(100)
                        continue
                    }
                    map[STREAM] = key
                    val messages = connection.xread(XReadParams.xReadParams().count(100), map)
                    connection.xtrim(STREAM, XTrimParams.xTrimParams().maxLen(20000L))
                    connection.close()
                    if (messages != null && messages.size != 0) {
                        val value = messages[messages.size-1].value
                        key = value[value.size-1].id
                        for ((key, value1) in messages) {
                            if (key == STREAM) {
                                val values = value1[0].fields
                                val plugin = values["plugin"]?:continue
                                val server = values["server"]?:continue
                                val message = values["message"]?:continue
                                InternalEventBus.call(RedisMessageEvent(plugin, server, message))
                            }
                        }
                    }
                } catch (e: Exception) {
                    warning("连接Redis失败，10s后重连")
                    try {
                        Thread.sleep(10000)
                    } catch (ex: InterruptedException) {
                        this.stopped = true
                    }
                }
            }
        }.start()
    }

    fun stop() {
        stopped = true
    }
}