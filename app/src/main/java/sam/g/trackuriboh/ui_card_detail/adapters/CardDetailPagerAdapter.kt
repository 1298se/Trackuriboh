package sam.g.trackuriboh.ui_card_detail.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.ui_price.CardPricesBottomSheetDialogFragment

class CardDetailPagerAdapter constructor(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    private var mSkuIds: List<Long>? = null

    override fun getItemCount(): Int {
        return if (mSkuIds == null) 0 else 1
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardPricesBottomSheetDialogFragment.newInstance(mSkuIds, false)
            else -> throw Exception("view pager out of bounds")
        }
    }

    fun setSkuIds(skuIds: List<Long>) {
        mSkuIds = skuIds
        notifyItemRangeInserted(0, 1)
    }
}
