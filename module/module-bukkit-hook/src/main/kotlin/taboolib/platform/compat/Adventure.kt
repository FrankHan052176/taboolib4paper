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
    return try {
        MiniMessage.miniMessage().deserialize(papi)
    } catch (ex: NoClassDefFoundError) {
        Component.text(papi)
    }
}

fun List<String>.components(player: Player): List<Component> {
    val papi = try {
        PlaceholderAPI.setPlaceholders(player, this)
    } catch (ex: NoClassDefFoundError) {
        this
    }
    return try {
        papi.map { MiniMessage.miniMessage().deserialize(it) }
    } catch (ex: NoClassDefFoundError) {
        papi.map { Component.text(it) }
    }
}