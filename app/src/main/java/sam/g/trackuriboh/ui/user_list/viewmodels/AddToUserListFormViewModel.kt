package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.repository.UserListRepository
import javax.inject.Inject

@HiltViewModel
class AddToUserListFormViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
) : ViewModel() {

    data class AddToUserListFormState(
        val canSave: Boolean = false,
        val formData: AddToUserListFormData,
        val entryAddCompleted: Boolean = false,
    )

    data class AddToUserListFormData(
        val skuWithConditionAndPrinting: SkuWithConditionAndPrinting? = null,
        val userList: UserList? = null,
        val quantity: Int = 1,
    )

    private val _formState = MutableLiveData(AddToUserListFormState(
        formData = AddToUserListFormData()
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

    fun addEntryToUserList() {
        val sku = formState.value?.formData?.skuWithConditionAndPrinting?.sku
        val userList = formState.value?.formData?.userList
        val quantity = formState.value?.formData?.quantity

        if (sku != null && userList != null && quantity != null) {
            viewModelScope.launch {
                userListRepository.insertUserListEntry(UserListEntry(listId = userList.id, skuId = sku.id, quantity = quantity))

                _formState.value = _formState.value?.copy(entryAddCompleted = true)
            }
        }
    }
}
