package taboolib.module.frankhan.ui

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.event.SubscribeEvent
import taboolib.platform.compat.getID
import taboolib.platform.util.isAir
import taboolib.platform.util.isNotAir
import kotlin.math.min

object InteractUIListener {
    @SubscribeEvent(ignoreCancelled = true)
    fun onDrag(e: InventoryDragEvent) {
        if (e.inventory.holder !is UIHolder) return
        e.isCancelled = true
    }
    @SubscribeEvent(ignoreCancelled = true)
    fun onClick(e: InventoryClickEvent) {
        val topInv = e.view.topInventory
        val ui = UIHolder.fromInventory(topInv) ?:return
        //外部按钮，不处理
        if (e.rawSlot < 0) return
        //无法处理的交互
        if (e.action == InventoryAction.CLONE_STACK || e.action == InventoryAction.COLLECT_TO_CURSOR) {
            if (e.rawSlot < topInv.size) {
                e.isCancelled = true
                return
            }else return
        }
        //存在点击函数，遂直接触发并返回
        if (ui.clickFun.containsKey(e.rawSlot)) {
            ui.clickFun[e.slot]!!.apply(e.whoClicked as Player, ui, e.click)
            e.isCancelled = true
            return
        }
        //点击位置不允许放置物品，遂直接取消并返回
        if (e.slot !in ui.limitSlot.keys && e.rawSlot < topInv.size) {
            e.isCancelled = true
            return
        }
        when {
            e.isLeftClick -> {
                if (e.rawSlot >= topInv.size) return
                e.isCancelled = true
                val cursor = e.cursor?:ItemStack(Material.AIR)
                val cid = cursor.getID()
                val slot = e.clickedInventory?.getItem(e.slot)?: ItemStack(Material.AIR)
                val sid = slot.getID()
                if (ui.limitSlot[e.slot]!!.check(e.whoClicked as Player, cursor)) {
                    if (cid == sid) {
                        val need = cursor.maxStackSize - cursor.amount
                        val amount = min(need, slot.amount)
                        e.view.cursor = cursor.subtract(amount)
                        topInv.setItem(e.slot, slot.add(amount))
                    }else {
                        e.view.cursor = slot
                        topInv.setItem(e.slot, cursor)
                    }
                }else if (cursor.isAir()) {
                    if (slot.isAir()) return
                    e.view.cursor = slot
                    topInv.setItem(e.slot,ItemStack(Material.AIR))
                }
            }
            e.isRightClick -> {
                if (e.rawSlot >= topInv.size) return
                e.isCancelled = true
                val cursor = e.cursor
                if (cursor.isAir()) return
                val cid = cursor.getID()
                val slot = e.clickedInventory?.getItem(e.slot)?: ItemStack(Material.AIR)
                val sid = slot.getID()
                if (ui.limitSlot[e.slot]!!.check(e.whoClicked as Player, cursor)) {
                    if (sid != cid) return
                    val new = cursor.clone()
                    new.amount = 1
                    topInv.setItem(e.slot, new)
                    e.view.cursor = cursor.subtract()
                }
            }
            e.isShiftClick -> {
                val current = e.currentItem?: return
                val cid = current.getID()
                if (e.rawSlot < topInv.size) {
                    val items = e.view.bottomInventory.addItem(current).values
                    val item = if (items.isEmpty()) {
                        ItemStack(Material.AIR)
                    }else items.iterator().next()
                    topInv.setItem(e.rawSlot, item)
                    return
                }
                e.isCancelled = true
                for (slot in ui.limitSlot.keys) {
                    val item: ItemStack = topInv.getItem(slot)?: ItemStack(Material.AIR)
                    val sid = item.getID()
                    if (!ui.limitSlot[slot]!!.check(e.whoClicked as Player, current) ||
                        (cid != sid && item.isNotAir())) continue
                    if (item.isAir()) {
                        topInv.setItem(slot, current)
                        e.view.bottomInventory.setItem(e.slot, ItemStack(Material.AIR))
                    }else {
                        val amount = min(item.maxStackSize - item.amount, current.amount)
                        topInv.setItem(slot, item.add(amount))
                        e.view.bottomInventory.setItem(e.slot, current.subtract(amount))
                    }
                    break
                }
            }
        }
    }
}