package sam.g.trackuriboh.ui.user_list.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuAndProduct
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.user_list.UserListDetailFragment.Companion.ARG_USER_LIST
import javax.inject.Inject

@HiltViewModel
class UserListDetailViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    application: Application,
    state: SavedStateHandle,
) : ViewModel() {

    val userList = state.get<UserList>(ARG_USER_LIST)!!

    sealed class UiModel {
        data class UserListEntryItem(
            val data: UserListEntryWithSkuAndProduct,
            val isChecked: Boolean,
        ) : UiModel()
        data class Header(val title: String) : UiModel()
        object Footer : UiModel()
    }

    data class UiState(
        val entries: List<UiModel>,
        val actionModeActive: Boolean,
        val actionModeTitle: String?
    )

    /*
     * We'll try to design things like this going forward
     * https://developer.android.com/jetpack/guide/ui-layer/events#decision-tree
     *
     * This should just expose state. **We can also pass lambdas**
     */
    val state: LiveData<UiState>
        get() = _state

    private val _state = MediatorLiveData<UiState>().apply {
        value = UiState(emptyList(), false, null)
    }

    private val checkedSkuIdsLiveData = MutableLiveData<MutableSet<Long>>(mutableSetOf())

    init {
        _state.addSource(userListRepository.getEntriesInUserListObservable(userList.id).map { list ->
            // Map it to UiModels
            val transformList: MutableList<UiModel> = list.map {
                UiModel.UserListEntryItem(it, false)
            }.toMutableList()

            // Add the header and the footer
            transformList.apply {
                add(
                    0,
                    UiModel.Header(application.resources.getQuantityString(R.plurals.user_list_detail_total_count, list.size, list.size))
                )
                add(UiModel.Footer)
            }
        }.flowOn(Dispatchers.Default).asLiveData()) {
            _state.value = _state.value?.copy(entries = it.toList())
        }

        _state.addSource(checkedSkuIdsLiveData) { checkedProductIds ->
            viewModelScope.launch(Dispatchers.Default) {
                _state.value?.entries?.let {
                    _state.postValue(_state.value?.copy(
                        entries = updateCheckedStates(it, checkedProductIds),
                        actionModeTitle = application.getString(R.string.user_list_detail_selected_count, checkedProductIds.size)
                    ))
                }
            }
        }
    }

    private fun updateCheckedStates(list: List<UiModel>, checkedSkuIds: Set<Long>): List<UiModel> {
        return list.map {
            if (it is UiModel.UserListEntryItem) {
                if (checkedSkuIds.contains(it.data.entry.skuId)) {
                    it.copy(isChecked = true)
                } else {
                    it.copy(isChecked = false)
                }
            } else {
                it
            }
        }
    }

    fun setActionMode(active: Boolean) {
        if (_state.value?.actionModeActive == active) {
            return
        }

        _state.value = _state.value?.copy(actionModeActive = active)

        checkedSkuIdsLiveData.value = mutableSetOf()
    }

    fun setUserListEntryChecked(skuId: Long, isChecked: Boolean) {
        val checkedSkuIds = if (isChecked) {
            checkedSkuIdsLiveData.value?.apply { add(skuId) }
        } else {
            checkedSkuIdsLiveData.value?.apply { remove(skuId) }
        }

        checkedSkuIdsLiveData.value = checkedSkuIds ?: mutableSetOf()
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            userListRepository.deleteUserListEntries(userList.id, checkedSkuIdsLiveData.value?.toList())

            setActionMode(false)
        }
    }
}
