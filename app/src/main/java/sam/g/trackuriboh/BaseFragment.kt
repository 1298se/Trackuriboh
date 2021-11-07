package sam.g.trackuriboh

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import sam.g.trackuriboh.ui_common.ErrorDialogFragment

const val TAG_ERROR_DIALOG = "ERROR_DIALOG"

abstract class BaseFragment : Fragment() {
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

    fun setViewPagerBackPressBehaviour(viewPager: ViewPager2) {
        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (viewPager.currentItem == 0) {
                if (!findNavController().popBackStack()) {
                    activity?.finish()
                }
            } else {
                viewPager.currentItem = viewPager.currentItem - 1
            }
        }
    }
}