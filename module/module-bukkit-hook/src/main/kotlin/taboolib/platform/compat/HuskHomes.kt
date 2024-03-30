package taboolib.platform.compat

import net.william278.huskhomes.api.HuskHomesAPI
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.severe
import taboolib.common.platform.function.submit
import taboolib.common.platform.function.warning

fun Player.teleportToSpawn(default: Location = world.spawnLocation) {
    try {
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
    } catch (ex: NoClassDefFoundError) {
        severe("Not Found HuskHomes Plugin!!!")
    }

}

fun getServerID(): String {
    try {
        return HuskHomesAPI.getInstance().server
    } catch (ex: NoClassDefFoundError) {
        severe("Not Found HuskHomes Plugin!!!")
        return "N/A"
    }
}