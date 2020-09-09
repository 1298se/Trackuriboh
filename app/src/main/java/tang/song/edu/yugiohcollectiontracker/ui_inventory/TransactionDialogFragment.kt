package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentTransactionDialogBinding
import tang.song.edu.yugiohcollectiontracker.viewBinding

class TransactionDialogFragment : DialogFragment(), Toolbar.OnMenuItemClickListener {
    private lateinit var viewModel: TransactionDialogViewModel

    private val binding by viewBinding(FragmentTransactionDialogBinding::inflate)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }

    private fun initToolbar() {
        binding.newTransactionToolbar.apply {
            inflateMenu(R.menu.transaction_dialog_toolbar_menu)
            setOnMenuItemClickListener(this@TransactionDialogFragment)

            setNavigationOnClickListener {
                findNavController().navigateUp()
            }
        }
    }
}
