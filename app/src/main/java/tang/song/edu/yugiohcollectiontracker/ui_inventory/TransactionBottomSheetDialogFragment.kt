package tang.song.edu.yugiohcollectiontracker.ui_inventory

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentTransactionDialogBinding
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

class TransactionBottomSheetDialogFragment : BottomSheetDialogFragment(), Toolbar.OnMenuItemClickListener {
    @Inject
    lateinit var mViewModelFactory: TransactionDialogViewModelFactory

    private val binding by viewBinding(FragmentTransactionDialogBinding::inflate)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog?.setOnShowListener {
            dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)?.let {
                BottomSheetBehavior.from(it).apply {
                    state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }

        return binding.root
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        return when (menuItem?.itemId) {
            R.id.action_clear_filter -> true
            else -> false
        }
    }

}
