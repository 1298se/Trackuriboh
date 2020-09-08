package tang.song.edu.yugiohcollectiontracker.data.models

enum class TransactionType(val value: Int) {
    PURCHASE(1),
    TRADE(2),
    SALE(3),
    UNKNOWN(4);

    companion object {
        private val map = values().associateBy(TransactionType::value)
        fun fromInt(transactionType: Int?) = map[transactionType] ?: UNKNOWN
    }
}