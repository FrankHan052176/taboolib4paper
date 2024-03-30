package taboolib.platform.compat

import org.bukkit.Material
import org.bukkit.entity.Player
import taboolib.common.platform.function.severe
import xyz.jpenilla.announcerplus.config.message.ToastSettings

fun Player.sendToast(icon: Material, header: String, footer: String, enchanted: Boolean = false, customModelData: Int = 0, type:String = "TASK") {
    try {
        val settings = ToastSettings(icon, ToastSettings.FrameType.valueOf(type), header, footer, enchanted, customModelData)
        settings.displayIfEnabled(this)
    }catch (e: NoClassDefFoundError) {
        severe("AnnouncerPlus Not Found!!!")
    }
}