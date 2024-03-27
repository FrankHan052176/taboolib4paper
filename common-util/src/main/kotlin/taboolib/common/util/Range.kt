package taboolib.common.util

import java.util.Random

data class Range(
    var min:Int,
    var max:Int
) {
    fun random():Int {
        val random = Random()
        return if (min >= max+1) {
            0
        }else random.nextInt(min,max+1)
    }
    fun isIn(int:Int):Boolean {
        return int in min..max
    }
    companion object {
        fun fromString(string: String): Range {
            val split = string.split("-")
            return Range(split[0].toInt(), split[1].toInt())
        }
    }
}
