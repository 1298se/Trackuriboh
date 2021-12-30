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
import sam.g.trackuriboh.databinding.DialogSimpleTextfieldBinding

class SimpleTextFieldDialogFragment : DialogFragment() {
    private lateinit var binding: DialogSimpleTextfieldBinding

    private val args: SimpleTextFieldDialogFragmentArgs by navArgs()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "SimpleTextFieldDialogFragment_fragmentResultRequestKey"
        const val TEXT_DATA_KEY = "SimpleTextFieldDialogFragment_userList"
    }

    /**
     * Can't use viewbinding delegate here because [onCreateDialog] is called before [onCreateView]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        binding = DialogSimpleTextfieldBinding.inflate(layoutInflater, null, false)

        builder.setView(binding.root)
            .setTitle(args.title)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->

                val name = binding.simpleTextfieldEdittext.text?.toString()?.trim()

                setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(TEXT_DATA_KEY to name)
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

                    binding.simpleTextfieldEdittext.addTextChangedListener {
                        isEnabled = !it.isNullOrBlank()
                    }
                }

            }
        }
    }

}
