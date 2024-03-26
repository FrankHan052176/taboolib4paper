package taboolib.module.frankhan.ui.limit

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import taboolib.frankhan.itemtags.getTags
import taboolib.platform.util.isAir

class LimitTag(
    val tags:Set<String>,
    val allContains:Boolean
): LimitSlot() {
    override fun check(player: Player, item: ItemStack?): Boolean {
        if (item.isAir()) return false
        val tags = item.getTags()
        if (allContains) {
            return tags.containsAll(tags)
        }else {
            tags.forEach {
                if (this.tags.contains(it)) return true
            }
            return false
        }
    }
}