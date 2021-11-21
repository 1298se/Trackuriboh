package sam.g.trackuriboh.ui_card_detail.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.ui_price.CardPricesBottomSheetDialogFragment

class CardDetailPagerAdapter constructor(
    fragment: Fragment,
    private val skuIds: List<Long>? = null
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return if (skuIds == null) 0 else 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardPricesBottomSheetDialogFragment.newInstance(skuIds, false)
            else -> throw Exception("view pager out of bounds")
        }
    }
}
