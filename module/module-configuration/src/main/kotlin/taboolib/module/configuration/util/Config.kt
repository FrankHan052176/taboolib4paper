package taboolib.module.configuration.util

import taboolib.common5.FileWatcher
import taboolib.module.configuration.Configuration

fun Configuration.reloadListener(callback: (Configuration) -> Unit) {
    if (file == null) return
    FileWatcher.INSTANCE.addSimpleListener(file) {
        if (file?.exists() == true) {
            this.loadFromFile(file!!)
            callback.invoke(this)
        }
    }
}