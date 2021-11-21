package sam.g.trackuriboh.ui_transaction

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.repository.SessionManager
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    callbackManager: CallbackManager,
    loginManager: LoginManager,
    private val sessionManager: SessionManager
) : ViewModel() {
    sealed class AuthenticationState {
        data class Success(val accessToken: AccessToken) : AuthenticationState()
        object Cancel : AuthenticationState()
        data class Error(val error: FacebookException) : AuthenticationState()
    }

    val authenticationState = MutableLiveData<AuthenticationState>()

    init {
        loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onCancel() {
                print("Cancel")
                authenticationState.value = AuthenticationState.Cancel
            }

            override fun onError(error: FacebookException) {
                print(error)
                authenticationState.value = AuthenticationState.Error(error)
            }

            override fun onSuccess(result: LoginResult) {
                authenticationState.value = AuthenticationState.Success(result.accessToken)
            }
        })
    }
}