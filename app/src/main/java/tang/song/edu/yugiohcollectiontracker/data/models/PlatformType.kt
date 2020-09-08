package tang.song.edu.yugiohcollectiontracker.data.models

enum class PlatformType(val value: Int) {
    EBAY(1),
    FACEBOOK(2),
    OTHER(3),
    UNKNOWN(4);

    companion object {
        private val map = values().associateBy(PlatformType::value)
        fun fromInt(platformType: Int?) = map[platformType] ?: UNKNOWN
    }
}