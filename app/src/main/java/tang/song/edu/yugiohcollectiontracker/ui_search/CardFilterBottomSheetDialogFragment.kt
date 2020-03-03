package tang.song.edu.yugiohcollectiontracker.ui_search

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import tang.song.edu.yugiohcollectiontracker.R

class CardFilterBottomSheetDialogFragment : BottomSheetDialogFragment(),
    Toolbar.OnMenuItemClickListener {
    private lateinit var mBottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private lateinit var mToolbar: MaterialToolbar
    private lateinit var mCategoryChipGroup: ChipGroup

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = View.inflate(context, R.layout.card_bottom_sheet_filter, null)
        val bottomSheetDialog = initDialogView(savedInstanceState, view)

        mToolbar = view.findViewById(R.id.filter_toolbar)
        mCategoryChipGroup = view.findViewById(R.id.card_category_chipgroup)

        return bottomSheetDialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initToolbar()
        initBottomSheetBehaviour()
        initChipGroups()
    }

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        return when (menuItem?.itemId) {
            R.id.action_clear_filter -> true
            else -> false
        }
    }

    private fun initDialogView(savedInstanceState: Bundle?, view: View): BottomSheetDialog {
        return (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            setContentView(view)

            this.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    mBottomSheetBehavior = BottomSheetBehavior.from(it)

                    it.layoutParams.apply {
                        val displayMetrics = DisplayMetrics()
                        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

                        height = displayMetrics.heightPixels
                    }
                }
        }
    }

    private fun initBottomSheetBehaviour() {
        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        mBottomSheetBehavior.skipCollapsed = true
    }

    private fun initToolbar() {
        mToolbar.inflateMenu(R.menu.filter_toolbar_menu)
        mToolbar.setOnMenuItemClickListener(this)
        mToolbar.title = "Filter"
        mToolbar.setNavigationOnClickListener { dismiss() }
    }

    private fun initChipGroups() {
        val categoryList = resources.getStringArray(R.array.card_category_list)

        categoryList.forEach {
            val chip =
                layoutInflater.inflate(R.layout.filter_chip, mCategoryChipGroup, false) as Chip
            chip.text = it
            mCategoryChipGroup.addView(chip)
        }
    }
}