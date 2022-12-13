package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.compose.material.ExperimentalMaterialApi
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.user_list.AddToUserListDialogFragment
import java.util.*
import javax.inject.Inject

@ExperimentalMaterialApi
@HiltViewModel
class AddToUserListFormViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val priceRepository: PriceRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    state: SavedStateHandle
) : ViewModel() {

    data class AddToUserListFormState(
        val canSave: Boolean = false,
        val formData: AddToUserListFormData,
    )

    data class AddToUserListFormData(
        val skuWithConditionAndPrinting: SkuWithConditionAndPrinting? = null,
        val userList: UserList? = null,
        val quantity: Int = 1,
    )

    private val userList = state.get<UserList?>(AddToUserListDialogFragment.ARG_USER_LIST)

    private val _formState = MutableLiveData(AddToUserListFormState(
        formData = AddToUserListFormData(userList = userList)
    ))

    val formState = Transformations.map(_formState) {
        if (validate(it.formData)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }

    private fun validate(formDataState: AddToUserListFormData): Boolean {
        with (formDataState) {
            return skuWithConditionAndPrinting != null &&
                    userList != null &&
                    quantity > 0
        }
    }

    fun onSkuChanged(skuWithConditionAndPrinting: SkuWithConditionAndPrinting?) {
        _formState.value = _formState.value?.let { it ->
            it.copy(formData = it.formData.copy(skuWithConditionAndPrinting = skuWithConditionAndPrinting))
        }

    }

    fun onUserListChanged(userList: UserList?) {
        _formState.value = _formState.value?.let { it ->
            it.copy(formData = it.formData.copy(userList = userList))
        }
    }

    fun onQuantityChanged(quantity: Int) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(quantity = quantity.coerceIn(1, 99)))
        }
    }

    /**
     * For one shot operations, we can do it like this using LiveData instead of having to add a flag to state.
     */
    fun addEntryToUserList()  = liveData {
        val sku = formState.value?.formData?.skuWithConditionAndPrinting?.sku
        val userList = formState.value?.formData?.userList
        val quantity = formState.value?.formData?.quantity

        firebaseAnalytics.logEvent(Events.ADD_TO_USER_LIST, bundleOf(
            "skuId" to sku?.id,
            "userList" to userList?.name,
            "quantity" to quantity,
        ))

        if (sku != null && userList != null && quantity != null) {
            userListRepository.upsertUserListEntryAndAddQuantity(
                UserListEntry(
                    listId = userList.id,
                    skuId = sku.id,
                    quantity = quantity,
                    dateAdded = Date(),
                    avgPurchasePrice = 0.0
                )
            )

            priceRepository.updatePricesForSkus(listOf(sku.id))

            emit(bundleOf(
                AddToUserListDialogFragment.ADDED_USER_LIST_NAME_DATA_KEY to userList.name,
            ))
        }
    }
}
