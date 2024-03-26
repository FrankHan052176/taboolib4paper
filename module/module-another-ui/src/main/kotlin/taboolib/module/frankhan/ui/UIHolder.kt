package taboolib.module.frankhan.ui

import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class UIHolder(val ui: InteractUI) : InventoryHolder {
    override fun getInventory(): Inventory {
        return ui.lastInv!!
    }
    companion object {

        fun fromInventory(inventory: Inventory): InteractUI? {
            return (inventory.holder as? UIHolder)?.ui
        }
    }
}