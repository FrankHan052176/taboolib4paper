package taboolib.platform.compat

import net.william278.huskhomes.api.HuskHomesAPI
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

fun Player.teleportToSpawn(default: Location = world.spawnLocation) {
    val api = HuskHomesAPI.getInstance()
    val onlineUser = api.adaptUser(this)
    val spawn = api.spawn.get()
    if (spawn.isPresent) {
        api.teleportBuilder().teleporter(onlineUser).target(spawn.get()).buildAndComplete(false)
    }else {
        submit {
            this@teleportToSpawn.teleport(default)
        }
    }
}