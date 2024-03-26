package taboolib.frankhan.itemtags

import org.bukkit.inventory.ItemStack
import taboolib.platform.compat.getID
import java.util.concurrent.ConcurrentHashMap

object ItemTags {
    private val tags = ConcurrentHashMap<String, HashSet<String>>()
    fun registerTags(id: String, tags: Set<String>) {
        if (id.isBlank()) return
        if (this.tags.containsKey(id)) {
            this.tags[id]?.addAll(tags)
        }else {
            this.tags[id] = HashSet(tags)
        }
    }

    fun getTags(id: String): Set<String> {
        if (id.isBlank()) return emptySet()
        return tags[id]?: emptySet()
    }
}

fun ItemStack?.getTags(): Set<String> {
    return ItemTags.getTags(getID())
}