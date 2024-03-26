package taboolib.module.frankhan.ui.limit

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.platform.compat.getID
import taboolib.platform.util.isAir

data class LimitItem(
    val ids:Set<String>,
    val allowAir: Boolean = true
): LimitSlot() {
    override fun check(player: Player, item:ItemStack?):Boolean {
        return if (ids.isNotEmpty()) {
            ids.contains(item.getID()) || (allowAir && item.isAir())
        }else item.isAir()
    }
}
