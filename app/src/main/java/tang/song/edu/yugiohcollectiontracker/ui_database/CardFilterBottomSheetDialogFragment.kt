package tang.song.edu.yugiohcollectiontracker.ui_database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.CardBottomSheetFilterBinding
import tang.song.edu.yugiohcollectiontracker.viewBinding

class CardFilterBottomSheetDialogFragment : BottomSheetDialogFragment(), Toolbar.OnMenuItemClickListener {
    private val binding by viewBinding(CardBottomSheetFilterBinding::inflate)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initChipGroups()
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        return when (menuItem?.itemId) {
            R.id.action_clear_filter -> true
            else -> false
        }
    }

    private fun initToolbar() {
        binding.filterToolbar.apply {
            inflateMenu(R.menu.filter_toolbar_menu)
            setOnMenuItemClickListener(this@CardFilterBottomSheetDialogFragment)
            title = "Filter"
            setNavigationOnClickListener { dismiss() }
        }
    }

    private fun initChipGroups() {
        val categoryList = resources.getStringArray(R.array.card_category_list)

        categoryList.forEach {
            val chip = layoutInflater.inflate(R.layout.filter_chip, binding.cardCategoryChipgroup, false) as Chip
            chip.text = it
            binding.cardCategoryChipgroup.addView(chip)
        }
    }
}