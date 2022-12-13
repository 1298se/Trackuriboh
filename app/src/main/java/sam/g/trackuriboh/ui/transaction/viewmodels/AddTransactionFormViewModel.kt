package sam.g.trackuriboh.ui.transaction.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.data.repository.TransactionRepository
import sam.g.trackuriboh.ui.transaction.AddTransactionDialogFragment.Companion.ARG_LIST_ID
import sam.g.trackuriboh.ui.transaction.AddTransactionDialogFragment.Companion.ARG_SKU_ID
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class AddTransactionFormViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val listId = state.get<Long>(ARG_LIST_ID)!!
    private val skuId = state.get<Long>(ARG_SKU_ID)!!

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

    suspend fun addEntryToTransactionList() {
        val type = formState.value?.formData?.type
        val date = formState.value?.formData?.date
        val price = formState.value?.formData?.price
        val quantity = formState.value?.formData?.quantity

        if (type != null && date != null && price != null && quantity != null) {
            transactionRepository.insertTransaction(
                UserTransaction(
                    type = type,
                    listId = listId,
                    skuId = skuId,
                    quantity = quantity,
                    price = price,
                    date = date
                )
            )
        }
    }
}
