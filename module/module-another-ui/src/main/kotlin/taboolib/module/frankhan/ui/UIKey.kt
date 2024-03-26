package taboolib.module.frankhan.ui

import org.bukkit.NamespacedKey
import org.bukkit.plugin.Plugin

class UIKey(val namespace:String, val key: String) {
    constructor(plugin: Plugin, key: String) : this(plugin.name, key)
    fun toNamespaceKey(): NamespacedKey {
        return NamespacedKey(namespace, key)
    }

    override fun hashCode(): Int {
        return StringBuilder().append(namespace).append(key).toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other !is UIKey) return false
        return other.namespace == namespace && other.key == key
    }
}