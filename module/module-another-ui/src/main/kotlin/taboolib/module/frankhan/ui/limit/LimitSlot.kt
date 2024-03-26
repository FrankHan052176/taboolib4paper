package taboolib.module.frankhan.ui.limit

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

abstract class LimitSlot {
    abstract fun check(player: Player, item:ItemStack?) :Boolean
}