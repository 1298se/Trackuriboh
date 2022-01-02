package sam.g.trackuriboh.ui.user_list.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuAndProduct
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.user_list.UserListDetailFragment
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class UserListDetailViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val priceRepository: PriceRepository,
    application: Application,
    state: SavedStateHandle,
) : ViewModel() {

    val userList = state.get<UserList>(UserListDetailFragment.ARG_USER_LIST)!!

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
     * This should just expose state. **We can also pass lambdas, but I don't think it's very necessary as
     * using interfaces is pretty clean as well**
     */
    val state: LiveData<UiState>
        get() = _state

    private val _state = MediatorLiveData<UiState>().apply {
        value = UiState(emptyList(),  false, null)
    }

    private val checkedSkuIdsLiveData = MutableLiveData<MutableSet<Long>>(mutableSetOf())

    private var currentEditEntry: UserListEntry? = null

    private var lastPriceUpdateTime: Long? = null

    init {
        _state.addSource(userListRepository.getEntriesInUserListObservable(userList.id).asLiveData()) { list ->

            refreshPricesIfNecessary(list)

            viewModelScope.launch(Dispatchers.Default) {
                var totalCount = 0
                // Map it to UiModels
                val transformList: MutableList<UiModel> = list.map { entry ->
                    UiModel.UserListEntryItem(entry, false).also {
                        totalCount += it.data.entry.quantity
                    }
                }.toMutableList()

                // Add the header and the footer
                transformList.apply {
                    add(
                        0,
                        UiModel.Header(
                            application.resources.getQuantityString(
                                R.plurals.user_list_detail_total_count,
                                totalCount,
                                totalCount
                            )
                        )
                    )
                    add(UiModel.Footer)
                }

                _state.postValue(_state.value?.copy(entries = transformList.toList()))
            }
        }

        _state.addSource(checkedSkuIdsLiveData) { checkedProductIds ->
            viewModelScope.launch(Dispatchers.Default) {
                _state.value?.entries?.let {
                    _state.postValue(
                        _state.value?.copy(
                            entries = updateCheckedStates(it, checkedProductIds),
                            actionModeTitle = application.getString(R.string.user_list_detail_selected_count, checkedProductIds.size)
                        )
                    )
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

    fun setCurrentEditEntry(entry: UserListEntry) {
        currentEditEntry = entry
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            userListRepository.deleteUserListEntries(userList.id, checkedSkuIdsLiveData.value?.toList())

            setActionMode(false)
        }
    }

    fun updateCurrentEditEntryQuantity(quantity: Int) {
        viewModelScope.launch {
            val updatedEntry = currentEditEntry?.copy(quantity = quantity)

            if (updatedEntry != null) {
                if (quantity > 0) {
                    userListRepository.updateUserListEntry(updatedEntry)
                } else {
                    userListRepository.deleteUserListEntry(updatedEntry)
                }

                currentEditEntry = null
            }
        }
    }

    private fun refreshPricesIfNecessary(list: List<UserListEntryWithSkuAndProduct>) {
        viewModelScope.launch {
            lastPriceUpdateTime.let {  lastUpdateTime ->
                val curTime = System.currentTimeMillis()
                if (lastUpdateTime == null || (curTime - lastUpdateTime) > TimeUnit.HOURS.toMillis(1) ) {
                    priceRepository.getPricesForSkuIds(list.map { it.skuWithConditionAndPrintingAndProduct.sku.id })

                    lastPriceUpdateTime = curTime
                }
            }
        }
    }
}
