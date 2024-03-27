package taboolib.module.frankhan.loots

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.common.platform.function.severe
import taboolib.common.util.Range
import taboolib.library.configuration.ConfigurationSection
import taboolib.platform.compat.getItem
import taboolib.platform.util.isAir

class Loots {
    val list = ArrayList<LootItem>()
    fun reload(section: ConfigurationSection): Loots {
        list.clear()
        for (key in section.getKeys(false)) {
            val loot = fromSection(key, section.getConfigurationSection(key) ?: continue)
            if (key.getItem().isAir() && Material.getMaterial(loot.id) == null) {
                severe("物品ID${loot.id}无效！")
                continue
            } else {
                list.add(loot)
            }
        }
        return this
    }

    companion object {
        private var maxWeight = 1
        fun readLoots(section: ConfigurationSection): Loots {
            return Loots().reload(section)
        }

        fun fromSection(id: String, section: ConfigurationSection): LootItem {
            val loot = LootItem(
                id, Range.fromString(section.getString("amount", "0-0")!!), Range(
                    maxWeight,
                    maxWeight + section.getInt("weight", 1)
                )
            )
            maxWeight += section.getInt("weight", 1)
            return loot
        }

        fun randomItem(loots: Loots): ItemStack {
            val weight = Range(1, maxWeight + 1).random()
            for (loot in loots.list) {
                if (loot.weight.isIn(weight)) {
                    return loot.generate()
                }
            }
            return ItemStack(Material.AIR)
        }
    }
}