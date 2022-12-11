package sam.g.trackuriboh.ui.user_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import sam.g.trackuriboh.data.db.entities.UserTransaction
import sam.g.trackuriboh.data.repository.TransactionRepository
import javax.inject.Inject

@HiltViewModel
class UserListEntryDetailViewModel @Inject constructor(
    transactionRepository: TransactionRepository,
    state: SavedStateHandle
) : ViewModel() {

    sealed class UiModel {
        data class TransactionItem(val transaction: UserTransaction) :
            UiModel()

        object Header : UiModel()
    }

    // TODO: Fix magic strings
    val listId = state.get<Long>("listId")!!
    val skuId = state.get<Long>("skuId")!!

    val transactions = transactionRepository.getUserListEntryTransactions(listId, skuId).map { transactions ->
        val uiList = mutableListOf<UiModel>()

        uiList.add(UiModel.Header)
        uiList.addAll(transactions.map { UiModel.TransactionItem(it) })

        return@map uiList
    }.asLiveData()
}