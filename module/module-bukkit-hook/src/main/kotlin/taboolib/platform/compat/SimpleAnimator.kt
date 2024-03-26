package taboolib.platform.compat

import com.ticxo.playeranimator.api.model.player.PlayerModel
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

class SimpleAnimator(player: Player, location: Location = player.location, val entity: Entity = location.world.spawn(location, ArmorStand::class.java) {
    it.isInvisible = true
    it.setBasePlate(false)
    it.isInvulnerable = true
    it.isSilent = true
}): PlayerModel(entity, player) {
    fun play(name: String):Boolean {
        try {
            playAnimation(name)
            return true
        }catch (e: Exception) {
            return false
        }
    }

    fun remove() {
        entity.remove()
    }
}