package taboolib.platform.compat

import io.th0rgal.oraxen.api.OraxenItems
import io.th0rgal.oraxen.api.events.OraxenItemsLoadedEvent
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

fun ItemStack?.getID(): String {
    if (this == null) return ""
    val id = OraxenItems.getIdByItem(this)
    if (id != null) return id
    return type.name
}

fun String?.getItem(): ItemStack {
    if (this.isNullOrBlank()) return ItemStack(Material.AIR)
    val oraxen = OraxenItems.getItemById(this)
    if (oraxen != null) return oraxen.build()
    val material = Material.matchMaterial(this)
    return ItemStack(material?:Material.AIR)
}

fun String?.getItem(default: ItemStack): ItemStack {
    if (this.isNullOrBlank()) return default
    val oraxen = OraxenItems.getItemById(this)
    if (oraxen != null) return oraxen.build()
    val material = Material.matchMaterial(this)
    return material?.let { ItemStack(it) }?:default
}