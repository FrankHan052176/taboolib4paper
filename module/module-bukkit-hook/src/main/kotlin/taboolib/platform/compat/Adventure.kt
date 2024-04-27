package taboolib.platform.compat

import me.clip.placeholderapi.PlaceholderAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

fun String.component(player: Player?): Component {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return MiniMessage.miniMessage().deserialize("<!i>$papi")
}

fun String.component(player: OfflinePlayer?): Component {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return MiniMessage.miniMessage().deserialize("<!i>$papi")
}

fun String.component(): Component {
    val papi = try {
        PlaceholderAPI.setPlaceholders(null, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return MiniMessage.miniMessage().deserialize("<!i>$papi")
}

fun List<String>.components(player: Player?): List<Component> {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return papi.map { MiniMessage.miniMessage().deserialize("<!i>$it") }
}

fun List<String>.components(player: OfflinePlayer?): List<Component> {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return papi.map { MiniMessage.miniMessage().deserialize("<!i>$it") }
}

fun List<String>.components(): List<Component> {
    val papi = try {
        PlaceholderAPI.setPlaceholders(null, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return papi.map { MiniMessage.miniMessage().deserialize("<!i>$it") }
}