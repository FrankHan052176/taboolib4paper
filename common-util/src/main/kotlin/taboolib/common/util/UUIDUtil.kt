package taboolib.common.util

import java.util.UUID

object UUIDUtil {
    fun offlinePlayerUUID(name:String):UUID {
        return UUID.nameUUIDFromBytes("OfflinePlayer:${name}".toByteArray())
    }
}