package sam.g.trackuriboh.ui.user_list.viewmodels

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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

    val userLists =
        userListRepository.getUserListsObervable().asLiveData(viewModelScope.coroutineContext)

    fun createUserList(list: UserList) {
        firebaseAnalytics.logEvent(Events.CREATE_USER_LIST, bundleOf("name" to list.name))

        viewModelScope.launch {
            userListRepository.upsertUserList(list)
        }
    }

    fun renameCurrentList(userList: UserList, name: String) {
        firebaseAnalytics.logEvent(Events.RENAME_USER_LIST, bundleOf("name" to name))

        viewModelScope.launch {
            val updateUserList = userList.copy(name = name)
            userListRepository.updateUserList(updateUserList)
        }
    }

    fun deleteUserList(userList: UserList) {
        viewModelScope.launch {
            userListRepository.deleteUserList(userList)
        }
    }
}
