package sam.g.trackuriboh.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import sam.g.trackuriboh.databinding.DialogProgressBinding
import sam.g.trackuriboh.utils.viewBinding

class ProgressDialog : DialogFragment() {
    private val binding by viewBinding(DialogProgressBinding::inflate)

    companion object {
        const val ARG_MESSAGE = "ProgressDialog_argMessage"

        fun newInstance(message: String) =
            ProgressDialog().apply {
                arguments = bundleOf(ARG_MESSAGE to message)
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)

        binding.progressMessage.text = arguments?.getString(ARG_MESSAGE)

        return binding.root
    }
}