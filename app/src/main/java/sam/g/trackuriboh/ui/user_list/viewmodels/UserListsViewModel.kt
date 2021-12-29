package sam.g.trackuriboh.ui.user_list.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.data.repository.UserListRepository
import javax.inject.Inject

@HiltViewModel
class UserListsViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val application: Application,
) : ViewModel() {

    data class UiState(
        val currentSelectedTabPosition: Int
    )

    val state: LiveData<UiState>
        get() = _state

    val userLists = userListRepository.getUserListsObervable().asLiveData()

    private val _state = MediatorLiveData<UiState>().apply {
        value = (UiState(0))
    }

    private var newCreatedList: Long? = null

    init {
        _state.addSource(userLists) { lists ->
            if (newCreatedList != null) {
                _state.value = _state.value?.copy(currentSelectedTabPosition = lists.indexOfFirst { it.id == newCreatedList })
                newCreatedList = null
            }
        }
    }


    fun createUserList(list: UserList) =
        viewModelScope.launch {
            newCreatedList = userListRepository.insertUserList(list)
        }

    fun insertToUserList(listId: Long, skuId: Long) =
        viewModelScope.launch {
            userListRepository.insertUserListEntry(UserListEntry(listId, skuId))
        }

    fun setCurrentTabPosition(position: Int) {
        _state.value = _state.value?.copy(currentSelectedTabPosition = position)
    }
}
