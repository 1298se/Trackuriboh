package tang.song.edu.yugiohcollectiontracker.ui_transaction_form.models

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import tang.song.edu.yugiohcollectiontracker.BR
import tang.song.edu.yugiohcollectiontracker.data.types.StringResourceEnum
import tang.song.edu.yugiohcollectiontracker.data.types.TransactionType

class TransactionDataModel : BaseObservable() {
    var cardId: Long? = null
    var cardImageURL: String? = null

    @get: Bindable
    var cardName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardName)
        }
    @get: Bindable
    var cardNumber: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.cardNumber)
        }
    @get: Bindable
    var rarity: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.rarity)
        }
    @get: Bindable
    var edition: StringResourceEnum? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.edition)
        }
    @get: Bindable
    var date: Long? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.date)
        }
    @get: Bindable
    var price: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.price)
        }
    @get: Bindable
    var quantity: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.quantity)
        }
    @get: Bindable
    var transactionType: StringResourceEnum? = TransactionType.PURCHASE
        set(value) {
            field = value
            notifyPropertyChanged(BR.transactionType)
        }
    @get: Bindable
    var platformType: StringResourceEnum? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.platformType)
        }
    @get: Bindable
    var condition: StringResourceEnum? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.condition)
        }
    @get: Bindable
    var partyName: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.partyName)
        }
    @get: Bindable
    var tracking: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.tracking)
        }
}
