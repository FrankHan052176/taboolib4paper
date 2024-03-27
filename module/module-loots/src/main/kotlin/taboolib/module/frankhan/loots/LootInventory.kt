package taboolib.module.frankhan.loots

import org.bukkit.inventory.Inventory
import taboolib.common.util.Range

object LootInventory {
    fun randomInventory(inv:Inventory, amount: Range, loots: Loots):Inventory {
        inv.clear()
        val num = amount.random()
        val slotR = Range(0,inv.size-1)
        for (i in 1..num) {
            var slot = slotR.random()
            while(inv.getItem(slot) != null) {
                slot = slotR.random()
            }
            inv.setItem(slot, Loots.randomItem(loots))
        }
        return inv
    }
}