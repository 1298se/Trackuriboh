package sam.g.trackuriboh.ui.collection.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.repository.UserListRepository
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val application: Application,
) : ViewModel() {

    data class UiState(
        val currentSelectedTabPosition: Int
    )

    val state: LiveData<UiState>
        get() = _state

    val collections = userListRepository.getUserListsObervable().asLiveData()

    private val _state = MediatorLiveData<UiState>().apply {
        value = (UiState(0))
    }

    private var newCreatedCollection: Long? = null

    init {
        _state.addSource(collections) { collections ->
            if (newCreatedCollection != null) {
                _state.value = _state.value?.copy(currentSelectedTabPosition = collections.indexOfFirst { it.id == newCreatedCollection })
                newCreatedCollection = null
            }
        }
    }


    fun createCollection(list: UserList) =
        viewModelScope.launch {
            newCreatedCollection = userListRepository.insertUserList(list)
        }

    fun insertToCollection(listId: Long, skuId: Long) =
        viewModelScope.launch {
            userListRepository.insertUserListEntry(UserListEntry(listId, skuId))
        }

    fun setCurrentTabPosition(position: Int) {
        _state.value = _state.value?.copy(currentSelectedTabPosition = position)
    }
}
