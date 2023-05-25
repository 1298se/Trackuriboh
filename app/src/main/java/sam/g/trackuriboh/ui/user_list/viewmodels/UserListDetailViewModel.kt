package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.db.relations.UserListEntryWithSkuMetadata
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.UserListRepository
import sam.g.trackuriboh.utils.UpdateTimer
import sam.g.trackuriboh.utils.refreshPricesIfNecessary
import javax.inject.Inject

@HiltViewModel
class UserListDetailViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val priceRepository: PriceRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
    private val updateTimer: UpdateTimer,
    state: SavedStateHandle,
) : ViewModel() {

    // TODO: Fix magic strings
    val userList = state.get<UserList>("userList")!!

    sealed class UiModel {
        data class UserListEntryItem(
            val data: UserListEntryWithSkuMetadata,
        ) : UiModel()

        data class Header(val totalCount: Int, val totalValue: Double) : UiModel()
    }

    /*
     * We'll try to design things like this going forward
     * https://developer.android.com/jetpack/guide/ui-layer/events#decision-tree
     *
     * This should just expose state. **We can also pass lambdas, but I don't think it's very necessary as
     * using interfaces is pretty clean as well**
     */

    val entries = userListRepository.getEntriesInUserListObservable(userList.id).map { list ->
        // Periodically refresh prices
        refreshPricesIfNecessary(
            list.map { it.skuWithMetadata.sku.id },
            updateTimer,
            priceRepository
        )

        var totalCount = 0
        var totalValue = 0.0
        // Map it to UiModels
        val transformList: MutableList<UiModel> = list.map { entry ->
            UiModel.UserListEntryItem(entry).also {
                totalCount += entry.entry.quantity
                totalValue += (entry.skuWithMetadata.sku.lowestBasePrice
                    ?: 0.0) * entry.entry.quantity
            }
        }.toMutableList()

        // Add the header
        transformList.add(
            0,
            UiModel.Header(
                totalCount,
                totalValue,
            )
        )

        transformList
    }.asLiveData(viewModelScope.coroutineContext)

    private var currentEditEntry: UserListEntry? = null

    fun setCurrentEditEntry(entry: UserListEntry) {
        currentEditEntry = entry
    }

    fun updateCurrentEditEntryQuantity(quantity: Int) {
        firebaseAnalytics.logEvent(
            Events.CHANGE_QUANTITY_USER_LIST,
            bundleOf("quantity" to quantity)
        )

        viewModelScope.launch {
            val updatedEntry = currentEditEntry?.copy(quantity = quantity)

            if (updatedEntry != null) {
                userListRepository.updateUserListEntry(updatedEntry)
            }

            currentEditEntry = null
        }
    }

    fun deleteEntry(skuId: Long) {
        viewModelScope.launch {
            userListRepository.deleteUserListEntry(userList.id, skuId)
        }
    }
}
