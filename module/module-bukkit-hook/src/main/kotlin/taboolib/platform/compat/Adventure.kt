package taboolib.platform.compat

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.entity.Player

fun String.component(player: Player): Component {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return papi.component()
}

fun String.component(): Component {
    return MiniMessage.miniMessage().deserialize(this)
}

fun List<String>.components(player: Player): List<Component> {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return papi.components()
}

fun List<String>.components(): List<Component> {
    return this.map { MiniMessage.miniMessage().deserialize(it) }
}