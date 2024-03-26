package taboolib.platform.compat

import com.ticxo.modelengine.api.ModelEngineAPI
import org.bukkit.Location
import org.bukkit.entity.ArmorStand

class SimpleModel(location: Location, animation: String) {
    val base = location.world.spawn(location, ArmorStand::class.java) {
        it.isInvisible = true
        it.setBasePlate(false)
        it.isSmall = true
        it.isInvulnerable = true
        it.isSilent = true
    }
    val modeled = ModelEngineAPI.createModeledEntity(base)
    var model = ModelEngineAPI.createActiveModel(animation)
        private set
    init {
        modeled.addModel(model, true)
    }
    fun changeModel(animation: String) {
        val id = model.blueprint.modelId
        model = ModelEngineAPI.createActiveModel(animation)
        modeled.addModel(model, true)
        modeled.removeModel(id)
    }

    fun remove() {
        model.destroy()
        base.remove()
    }
}