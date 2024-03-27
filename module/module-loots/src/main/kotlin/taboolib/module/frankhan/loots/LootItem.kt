package taboolib.module.frankhan.loots

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.util.Range
import taboolib.platform.compat.getItem

data class LootItem(val id:String, val amount: Range, val weight: Range) {
    fun generate():ItemStack {
        val item = id.getItem(ItemStack(Material.PAPER))
        item.amount *= amount.random()
        return if (item.amount == 0) {
            ItemStack(Material.AIR)
        }else item
    }
}