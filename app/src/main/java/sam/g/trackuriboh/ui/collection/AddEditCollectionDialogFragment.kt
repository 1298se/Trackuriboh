package sam.g.trackuriboh.ui.collection

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
import sam.g.trackuriboh.data.types.CollectionType
import sam.g.trackuriboh.databinding.DialogAddEditCollectionBinding

class AddEditCollectionDialogFragment : DialogFragment() {
    private lateinit var binding: DialogAddEditCollectionBinding

    private val args: AddEditCollectionDialogFragmentArgs by navArgs()

    private lateinit var dialog: AlertDialog

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "AddEditCollectionDialogFragment_fragmentResultRequestKey"
        const val COLLECTION_NAME_DATA_KEY = "AddEditCollectionDialogFragment_collectionName"
    }

    /**
     * Can't use viewbinding delegate here because [onCreateDialog] is called before [onCreateView]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        binding = DialogAddEditCollectionBinding.inflate(layoutInflater, null, false)

        val title = when (args.collectionType) {
            CollectionType.COLLECTION -> R.string.create_collection_option
            // CollectionType.CHECKLIST -> R.string.create_checklist_option
        }

        builder.setView(binding.root)
            .setTitle(title)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->

                val name = binding.addEditWatchlistNameText.text?.toString()?.trim()

                setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(COLLECTION_NAME_DATA_KEY to name)
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
