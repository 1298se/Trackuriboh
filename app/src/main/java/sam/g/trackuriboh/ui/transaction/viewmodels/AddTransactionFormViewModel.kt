package sam.g.trackuriboh.ui.transaction.viewmodels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.Transaction
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.TransactionRepository
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.transaction.AddTransactionDialogFragment
import sam.g.trackuriboh.ui.user_list.AddToUserListDialogFragment
import sam.g.trackuriboh.ui.user_list.viewmodels.AddToUserListFormViewModel
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.math.absoluteValue

@ExperimentalMaterialApi
@HiltViewModel
class AddTransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    // private val priceRepository: PriceRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    state: SavedStateHandle
) : ViewModel() {

    data class AddTransactionFormState(
        val canSave: Boolean = false,
        val formData: AddTransactionFormData,
    )

    data class AddTransactionFormData(
        val type: TransactionType = TransactionType.PURCHASE,
        val date: Date = Calendar.getInstance().time,
        val price: Double = 0.00,
        val quantity: Int = 1,
    )

    private val _formState = MutableLiveData(
        AddTransactionFormState(formData = AddTransactionFormData())
    )

    private fun validate(formDataState: AddTransactionFormData): Boolean {
        with (formDataState) {
            return true
        }
    }

    val formState = Transformations.map(_formState) {
        if (validate(it.formData)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }

    fun onTypeChanged(type: TransactionType) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(type = type))
        }
    }

    fun onPriceChanged(price: Double) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(price = price.absoluteValue))
        }
    }

    fun onDateChanged(date: Date) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(date = date))
        }
    }

    fun onQuantityChanged(quantity: Int) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(quantity = quantity.coerceIn(1, 99)))
        }
    }

    fun addEntryToTransactionList(product: Product, userListEntry: UserListEntry)  = liveData {
        val type = formState.value?.formData?.type
        val date = formState.value?.formData?.date
        val price = formState.value?.formData?.price
        val quantity = formState.value?.formData?.quantity

//        firebaseAnalytics.logEvent(
//            Events.ADD_TO_USER_LIST, bundleOf(
//            "skuId" to sku?.id,
//            "userList" to userList?.name,
//            "quantity" to quantity)
//        )

        if (type != null && date != null && price != null && quantity != null) {
            transactionRepository.upsertTransactionAndUpdateUserListEntry(
                userListEntry,
                Transaction(
                    type = type,
                    productId = product.id,
                    listId = userListEntry.listId,
                    skuId = userListEntry.skuId,
                    quantity = quantity,
                    price = price,
                    date = date
                )
            )

            emit(
                bundleOf(AddTransactionDialogFragment.ADDED_TRANSACTION_NAME_DATA_KEY to "")
            )
        }
    }
}