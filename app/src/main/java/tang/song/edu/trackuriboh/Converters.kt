package tang.song.edu.trackuriboh
import java.text.SimpleDateFormat
import java.util.*

object Converters {
    @JvmStatic fun dateToString(
        value: Long?
    ): String? {
        return value?.let { SimpleDateFormat("dd/MM/yyyy").apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(value)) }
    }
}
