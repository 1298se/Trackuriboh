package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.db.relations.SkuWithMetadata
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.user_list.AddToUserListFormDialogFragment
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddToUserListFormViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val userListRepository: UserListRepository,
    private val priceRepository: PriceRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    state: SavedStateHandle
) : ViewModel() {

    data class AddToUserListFormState(
        val isInitialized: Boolean = false,
        val canSave: Boolean = false,
        val formData: AddToUserListFormData = AddToUserListFormData(),
    )

    data class AddToUserListFormData(
        val skuWithMetadata: SkuWithMetadata? = null,
        val userList: UserList? = null,
        val quantity: Int = 1,
    )

    private val productId = state.get<Long>(AddToUserListFormDialogFragment.ARG_PRODUCT_ID)!!

    lateinit var productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds
    lateinit var userLists: List<UserList>

    private val _formState = MutableLiveData(
        AddToUserListFormState(
            formData = AddToUserListFormData(userList = state[AddToUserListFormDialogFragment.ARG_USER_LIST])
        )
    )

    val formState = Transformations.map(_formState) {
        if (validate(it.formData)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }

    init {
        viewModelScope.launch {
            productWithCardSetAndSkuIds = productRepository.getProductWithSkusById(productId).let {
                it.copy(skusWithMetadata = it.getOrderedSkusByPrintingAndCondition())
            }

            userLists = userListRepository.getAllUserLists()

            _formState.value = _formState.value?.copy(isInitialized = true)
        }
    }

    private fun validate(formDataState: AddToUserListFormData): Boolean {
        with(formDataState) {
            return skuWithMetadata != null &&
                    userList != null &&
                    quantity > 0
        }
    }

    fun setSku(skuWithMetadata: SkuWithMetadata?) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(skuWithMetadata = skuWithMetadata))
        }

    }

    fun setUserList(userList: UserList?) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(userList = userList))
        }
    }

    fun setQuantity(quantity: Int) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(quantity = quantity.coerceIn(1, 99)))
        }
    }

    /**
     * For one shot operations, we can do it like this using LiveData instead of having to add a flag to state.
     */
    fun addEntryToUserList()  = liveData {
        val sku = formState.value?.formData?.skuWithMetadata?.sku
        val userList = formState.value?.formData?.userList
        val quantity = formState.value?.formData?.quantity

        firebaseAnalytics.logEvent(Events.ADD_TO_USER_LIST, bundleOf(
            "skuId" to sku?.id,
            "userList" to userList?.name,
            "quantity" to quantity,
        ))

        if (sku != null && userList != null && quantity != null) {
            userListRepository.upsertUserListEntryWithQuantity(
                UserListEntry(
                    listId = userList.id,
                    skuId = sku.id,
                    quantity = quantity,
                    dateAdded = Date(),
                )
            )

            priceRepository.updatePricesForSkus(listOf(sku.id))

            emit(bundleOf(
                AddToUserListFormDialogFragment.ADDED_USER_LIST_NAME_DATA_KEY to userList.name,
            ))
        }
    }
}
