package tang.song.edu.yugiohcollectiontracker.ui_base

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tang.song.edu.yugiohcollectiontracker.R

private const val TAG_DIALOG_TITLE = "TAG_DIALOG_TITLE"
private const val TAG_DIALOG_MESSAGE = "TAG_DIALOG_MESSAGE"

open class ErrorDialogFragment : DialogFragment() {
    private lateinit var mDialogTitle: String
    private lateinit var mDialogMessage: String

    companion object {
        fun newInstance(title: String?, message: String?): ErrorDialogFragment {
            val fragment = ErrorDialogFragment()

            fragment.arguments = Bundle().apply {
                putString(TAG_DIALOG_TITLE, title)
                putString(TAG_DIALOG_MESSAGE, message)
            }

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mDialogTitle = it.getString(
                TAG_DIALOG_TITLE,
                requireContext().getString(R.string.error_title_generic)
            )
            mDialogMessage = it.getString(
                TAG_DIALOG_MESSAGE,
                requireContext().getString(R.string.error_message_generic)
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(mDialogTitle)
                .setMessage(mDialogMessage)
                .create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}