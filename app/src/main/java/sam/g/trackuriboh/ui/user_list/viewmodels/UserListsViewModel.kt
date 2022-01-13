package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.analytics.Events
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.repository.UserListRepository
import javax.inject.Inject

@HiltViewModel
class UserListsViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val firebaseAnalytics: FirebaseAnalytics,
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

    private var newCreatedListId: Long? = null

    init {
        _state.addSource(userLists) { lists ->
            if (newCreatedListId != null) {
                _state.value = _state.value?.copy(currentSelectedTabPosition = lists.indexOfFirst { it.id == newCreatedListId })
                newCreatedListId = null
            }
        }
    }


    fun createUserList(list: UserList) {
        firebaseAnalytics.logEvent(Events.CREATE_USER_LIST, bundleOf("name" to list.name))

        viewModelScope.launch {
            newCreatedListId = userListRepository.insertUserList(list)
        }
    }


    fun setCurrentTabPosition(position: Int) {
        _state.value = _state.value?.copy(currentSelectedTabPosition = position)
    }

    fun renameCurrentList(name: String) {
        firebaseAnalytics.logEvent(Events.RENAME_USER_LIST, bundleOf("name" to name))

        viewModelScope.launch {
            val currentList = getCurrentList()

            if (currentList != null) {
                val updatedList = currentList.copy(name = name)

                userListRepository.updateUserList(updatedList)
            }
        }
    }

    fun deleteCurrentList() {

        viewModelScope.launch {
            val currentList = getCurrentList()

            firebaseAnalytics.logEvent(Events.DELETE_USER_LIST, bundleOf("name" to currentList?.name))

            if (currentList != null) {
                userListRepository.deleteUserList(currentList)
            }
        }

    }

    fun getCurrentList(): UserList? =
        // We need getOrNull here in case there is no lists and the user tries to rename/delete a list because
        // ViewPager currentItem will return 0 if the list is empty or null
        _state.value?.currentSelectedTabPosition?.let { userLists.value?.getOrNull(it) }
}
