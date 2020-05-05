package tang.song.edu.yugiohcollectiontracker

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import tang.song.edu.yugiohcollectiontracker.ui_base.ErrorDialogFragment

abstract class BaseFragment(contentLayoutId: Int) : Fragment(contentLayoutId) {
    private var mErrorDialog: ErrorDialogFragment? = null

    open fun showError(title: Int, message: Int) {
        showError(context?.getString(title), context?.getString(message))
    }

    open fun showError(title: String?, message: String?) {
        if (mErrorDialog == null) {
            mErrorDialog = ErrorDialogFragment.newInstance(title, message)
            mErrorDialog?.show(parentFragmentManager, TAG_ERROR_DIALOG)
        }
    }

    fun hideSoftKeyboard() {
        (activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(activity?.currentFocus?.windowToken, 0)
    }
}