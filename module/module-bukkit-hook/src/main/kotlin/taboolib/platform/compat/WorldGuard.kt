package taboolib.platform.compat

import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldguard.WorldGuard
import com.sk89q.worldguard.protection.regions.ProtectedRegion
import org.bukkit.entity.Player

fun inWhichGuard(player: Player): ProtectedRegion? {
    val loc = BukkitAdapter.adapt(player.location)
    val container = WorldGuard.getInstance().platform.regionContainer
    val query = container.createQuery()
    val set = query.getApplicableRegions(loc)
    for (region in set) {
        return region
    }
    return null
}