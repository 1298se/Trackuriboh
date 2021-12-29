package sam.g.trackuriboh.ui.user_list

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.types.UserListType
import sam.g.trackuriboh.databinding.DialogAddEditUserListBinding

class AddEditUserListDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddEditUserListBinding

    private val args: AddEditUserListDialogFragmentArgs by navArgs()


    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddEditListDialogFragment_fragmentResultRequestKey"
        const val USER_LIST_NAME_DATA_KEY = "AddEditListDialogFragment_userList"
    }

    /**
     * Can't use viewbinding delegate here because [onCreateDialog] is called before [onCreateView]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        binding = DialogAddEditUserListBinding.inflate(layoutInflater, null, false)

        val title = when (args.userListType) {
            UserListType.USER_LIST -> R.string.create_user_list_option
            // CollectionType.CHECKLIST -> R.string.create_checklist_option
        }

        builder.setView(binding.root)
            .setTitle(title)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->

                val name = binding.addEditWatchlistNameText.text?.toString()?.trim()

                setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(USER_LIST_NAME_DATA_KEY to name)
                )

                findNavController().popBackStack()
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                findNavController().popBackStack()
            }

        return builder.create().apply {
            setOnShowListener {
                getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

                with (getButton(AlertDialog.BUTTON_POSITIVE)) {
                    isEnabled = false

                    binding.addEditWatchlistNameText.addTextChangedListener {
                        isEnabled = !it.isNullOrBlank()
                    }
                }

            }
        }
    }

}
