package taboolib.module.frankhan.ui

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import taboolib.platform.compat.component
import taboolib.module.frankhan.ui.limit.LimitSlot
import java.util.function.Function
import kotlin.math.max
import kotlin.math.min

class InteractUI(var title: String, var rows: Int) {
    var lastInv: Inventory? = null
    val getFun = HashMap<Int, Function<Unit, ItemStack?>>()
    val clickFun = HashMap<Int, A3Function<Player, InteractUI, ClickType, Unit>>()
    val setFun = HashMap<Int, A3Function<Player, InteractUI, ItemStack, ItemStack>>()
    val limitSlot = HashMap<Int, LimitSlot>()
    val holder = UIHolder(this)

    fun build(player: Player): Inventory {
        lastInv = Bukkit.createInventory(holder, max(1, min(rows,6)) *9, title.component(player))
        updateInv()
        return lastInv as Inventory
    }

    fun updateInv(): Inventory {
        for (slot in getFun.keys) {
            lastInv!!.setItem(slot, getFun[slot]!!.apply(Unit))
        }
        return lastInv as Inventory
    }

    fun updateTitle(player: Player, title: String): Inventory {
        this.title = title
        val viewers = lastInv!!.viewers.toList()
        val context = lastInv!!.contents.toList()
        lastInv = Bukkit.createInventory(UIHolder(this), max(1, min(rows,6)) *9, title.component(player))
        for (index in context.indices) {
            lastInv!!.setItem(index, context[index])
        }
        updateInv()
        viewers.forEach { it.openInventory(lastInv!!) }
        return lastInv!!
    }
}