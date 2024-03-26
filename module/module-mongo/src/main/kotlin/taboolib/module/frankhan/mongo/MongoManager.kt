package taboolib.module.frankhan.mongo

import com.mongodb.*
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.UpdateOptions
import org.bson.Document
import org.bson.UuidRepresentation
import org.bson.conversions.Bson
import taboolib.common.LifeCycle
import taboolib.common.asyncThread
import taboolib.common.platform.Awake
import taboolib.common.platform.function.info
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.submitAsync
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import java.util.*
import java.util.concurrent.CompletableFuture
import java.util.function.Function

class MongoManager(config: Configuration) {
    private var mongoClient: MongoClient
    private var database: MongoDatabase
    private var disabled = false
    private var collectionPrefix: String = config.getString("collection-prefix", "missingbyriver")!!

    init {
        val settings = MongoClientSettings.builder().uuidRepresentation(UuidRepresentation.STANDARD)
        if (config.getString("connection-uri", "") != "") {
            settings.applyConnectionString(ConnectionString(config.getString("connection-uri", "")!!))
            mongoClient = MongoClients.create(settings.build())
        } else {
            if (config.contains("user")) {
                val credential = MongoCredential.createCredential(
                    config.getString("user", "root")!!,
                    config.getString("database", "minecraft")!!,
                    (config.getString("password", "password"))!!.toCharArray()
                )
                settings.credential(credential)
            }
            settings.applyToClusterSettings { builder ->
                builder.hosts(
                    listOf(
                        ServerAddress(
                            config.getString("host", "localhost"),
                            config.getInt("port", 27017)
                        )
                    )
                )
            }
            mongoClient = MongoClients.create(settings.build())
        }
        database = mongoClient.getDatabase(config.getString("database", "minecraft")!!)
        info("MongoDB Service ✅")
    }

    fun disabled() {
        disabled = true
        mongoClient.close()
    }

    private fun getCollectionName(value: String): String {
        return collectionPrefix + "_" + value
    }

    fun findAll(
        collectionName: String,
        query: Bson = Filters.empty(),
    ): List<Document> {
        submitAsync {  }
        if (disabled) return emptyList()
        val collection = database.getCollection(getCollectionName(collectionName))
        return collection.find(query).toList()
    }

    fun get(
        collectionName: String,
        document: Bson
    ): List<Document> {
        if (disabled) return emptyList()
        val collection = database.getCollection(getCollectionName(collectionName))
        return collection.find(document).toList()
    }

    fun getFirst(
        collectionName: String,
        document: Bson
    ): Optional<Document> {
        if (disabled) return Optional.empty()
        val collection = database.getCollection(getCollectionName(collectionName))
        val result = collection.find(document).toList()
        return if (result.isNotEmpty()) {
            Optional.of(result.first())
        } else {
            Optional.empty()
        }
    }

    fun insert(
        collectionName: String,
        data: Document
    ): Boolean {
        if (disabled) return false
        val collection = database.getCollection(getCollectionName(collectionName))
        return try {
            val result = collection.insertOne(data)
            result.wasAcknowledged()
        } catch (e: MongoException) {
            false
        }
    }

    fun update(
        collectionName: String,
        query: Bson,
        updates: Bson,
        options: UpdateOptions,
        many: Boolean = false
    ): Boolean {
        if (disabled) return false
        val collection = database.getCollection(getCollectionName(collectionName))
        return try {
            val result = if (many) {
                collection.updateMany(query, updates, options)
            } else {
                collection.updateOne(query, updates, options)
            }
            result.wasAcknowledged()
        } catch (e: MongoException) {
            false
        }
    }

    fun setIndex(
        collectionName: String,
        key: String
    ): Boolean {
        if (disabled) return false
        val collection = database.getCollection(getCollectionName(collectionName))
        return try {
            collection.createIndex(Indexes.ascending(key))
            true
        } catch (e: MongoException) {
            false
        }
    }

    fun delete(
        collectionName: String,
        document: Bson,
        many: Boolean = false
    ): Boolean {
        if (disabled) return false
        val collection = database.getCollection(getCollectionName(collectionName))
        return try {
            if (many) {
                collection.deleteMany(document)
            } else {
                collection.deleteOne(document)
            }
            true
        } catch (e: MongoException) {
            false
        }
    }

    fun <T : Any> execute(callback: Function<MongoDatabase, T>) : Optional<T> {
        if (disabled) return Optional.empty()
        return Optional.of(callback.apply(database))
    }

    fun <T : Any> executeFuture(callback: Function<MongoDatabase, T>) : CompletableFuture<Optional<T>> {
        val future = CompletableFuture<Optional<T>>()
        if (disabled) {
            future.complete(Optional.empty())
        }else {
            asyncThread {
                future.complete(Optional.ofNullable(callback.apply(database)))
            }
        }
        return future
    }

    companion object {
        @Config("mongo.yml", autoReload = true)
        lateinit var config: Configuration
        private lateinit var instance: MongoManager

        @Awake(LifeCycle.LOAD)
        fun init() {
            if (config.getBoolean("disabled")) return
            try {
                instance = MongoManager(config)
            } catch (e: Exception) {
                severe("MongoDB Service ❌")
            }
        }

        fun manager(): MongoManager? {
            return if (this::instance.isInitialized) {
                instance
            } else null
        }

        fun disabled() {
            if (this::instance.isInitialized) {
                instance.disabled()
            }
        }

        fun restart():MongoManager? {
            if (config.getBoolean("disabled")) return null
            return if (this::instance.isInitialized) {
                if (instance.disabled) {
                    instance = MongoManager(config)
                }
                instance
            }else null
        }
    }
}
