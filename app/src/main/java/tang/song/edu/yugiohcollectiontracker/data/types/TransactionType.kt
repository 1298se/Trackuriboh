package tang.song.edu.yugiohcollectiontracker.data.types

import tang.song.edu.yugiohcollectiontracker.R

enum class TransactionType(val value: Int) : StringResourceEnum {
    PURCHASE(1),
    SALE(2);

    companion object {
        private val map = values().associateBy(TransactionType::value)
        fun fromInt(transactionType: Int?) = map[transactionType]
    }

    override fun getResourceId(): Int = when(this) {
        PURCHASE -> R.string.lbl_purchase
        SALE -> R.string.lbl_sale
    }
}