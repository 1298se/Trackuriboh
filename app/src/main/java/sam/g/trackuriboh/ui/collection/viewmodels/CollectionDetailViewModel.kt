package sam.g.trackuriboh.ui.collection.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuAndProduct
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.ui.collection.CollectionDetailFragment.Companion.ARG_WATCHLIST_ID
import javax.inject.Inject

@HiltViewModel
class CollectionDetailViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    application: Application,
    state: SavedStateHandle,
) : ViewModel() {

    private val collectionId = state.get<Long>(ARG_WATCHLIST_ID)!!

    sealed class UiModel {
        data class CollectionEntryItem(
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

    private val checkedProductIdsLiveData = MutableLiveData<MutableSet<Long>>(mutableSetOf())

    init {
        _state.addSource(userListRepository.getEntriesInUserListObservable(collectionId).map { list ->
            // Map it to UiModels
            val transformList: MutableList<UiModel> = list.map {
                UiModel.CollectionEntryItem(it, false)
            }.toMutableList()

            // Add the header and the footer
            transformList.apply {
                add(
                    0,
                    UiModel.Header(application.resources.getQuantityString(R.plurals.collection_detail_total_count, list.size, list.size))
                )
                add(UiModel.Footer)
            }
        }.asLiveData()) {
            _state.value = _state.value?.copy(entries = it.toList())
        }

        _state.addSource(checkedProductIdsLiveData) { checkedProductIds ->
            _state.value?.entries?.let {
                _state.value = _state.value?.copy(
                    entries = updateCheckedStates(it, checkedProductIds),
                    actionModeTitle = application.getString(R.string.collection_detail_selected_count, checkedProductIds.size)
                )
            }
        }
    }

    private fun updateCheckedStates(list: List<UiModel>, checkedProductIds: Set<Long>): List<UiModel> {
        return list.map {
            if (it is UiModel.CollectionEntryItem) {
                if (checkedProductIds.contains(it.data.entry.skuId)) {
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

        checkedProductIdsLiveData.value = mutableSetOf()
    }

    fun setCollectionEntryChecked(entry: UserListEntry, isChecked: Boolean) {
        val checkedProductIds = if (isChecked) {
            checkedProductIdsLiveData.value?.apply { add(entry.skuId) }
        } else {
            checkedProductIdsLiveData.value?.apply { remove(entry.skuId) }
        }

        checkedProductIdsLiveData.value = checkedProductIds ?: mutableSetOf()
    }

    fun deleteSelectedItems() {
        viewModelScope.launch {
            userListRepository.deleteUserListEntries(collectionId, checkedProductIdsLiveData.value?.toList())

            setActionMode(false)
        }
    }
}
