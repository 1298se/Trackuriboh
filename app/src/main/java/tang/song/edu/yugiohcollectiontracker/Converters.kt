package tang.song.edu.yugiohcollectiontracker
    import androidx.databinding.InverseMethod

object Converters {
    @InverseMethod("stringToDouble")
    @JvmStatic
    fun doubleToString(
        value: Double
    ): String = value.toString()

    @JvmStatic
   fun stringToDouble(
        value: String
    ): Double = value.toDouble()

    @InverseMethod("stringToInt")
    @JvmStatic
    fun intToString(
        value: Int
    ): String = value.toString()

    @JvmStatic
    fun stringToInt(
        value: String
    ): Int = value.toInt()
}
