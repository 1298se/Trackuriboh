package tang.song.edu.yugiohcollectiontracker

import androidx.fragment.app.Fragment
import tang.song.edu.yugiohcollectiontracker.ui_base.ErrorDialogFragment

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
}