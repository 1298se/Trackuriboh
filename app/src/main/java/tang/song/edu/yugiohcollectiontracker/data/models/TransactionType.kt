package tang.song.edu.yugiohcollectiontracker.data.models

enum class TransactionType(val value: Int) {
    PURCHASE(1),
    SALE(2),
    UNKNOWN(3);

    companion object {
        private val map = values().associateBy(TransactionType::value)
        fun fromInt(transactionType: Int?) = map[transactionType] ?: UNKNOWN
    }
}