package sam.g.trackuriboh.ui.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.DialogSimpleTextfieldBinding

// TODO: Refactor to compose
class SimpleTextFieldDialogFragment : DialogFragment() {
    private lateinit var binding: DialogSimpleTextfieldBinding

    private var title: String? = null

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "SimpleTextFieldDialogFragment_fragmentResultRequestKey"
        const val TEXT_DATA_KEY = "SimpleTextFieldDialogFragment_userList"

        private const val ARG_TITLE = "SimpleTextFieldDialogFragment_argTitle"

        fun newInstance(title: String? = null) =
            SimpleTextFieldDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            title = it.getString(ARG_TITLE)
        }
    }

    /**
     * Can't use viewbinding delegate here because [onCreateDialog] is called before [onCreateView]
     */
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        binding = DialogSimpleTextfieldBinding.inflate(layoutInflater, null, false)

        builder.setView(binding.root)
            .setTitle(title)
            .setPositiveButton(R.string.lbl_ok) { _, _ ->

                val name = binding.simpleTextfieldEdittext.text?.toString()?.trim()

                setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY,
                    bundleOf(TEXT_DATA_KEY to name)
                )

                dismiss()
            }
            .setNegativeButton(R.string.lbl_cancel) { _, _ ->
                dismiss()
            }

        return builder.create().apply {
            setOnShowListener {
                with (getButton(AlertDialog.BUTTON_POSITIVE)) {
                    isEnabled =  !binding.simpleTextfieldEdittext.text.isNullOrBlank()

                    binding.simpleTextfieldEdittext.addTextChangedListener {
                        isEnabled = !it.isNullOrBlank()
                    }
                }

            }
        }
    }

}
