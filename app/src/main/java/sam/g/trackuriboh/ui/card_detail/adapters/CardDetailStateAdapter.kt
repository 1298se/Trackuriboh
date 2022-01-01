package sam.g.trackuriboh.ui.card_detail.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.ui.card_detail.CardDetailOverviewFragment
import sam.g.trackuriboh.ui.card_detail.viewmodels.CardDetailViewModel
import sam.g.trackuriboh.ui.price.CardPricesBottomSheetFragment

class CardDetailStateAdapter(
    fragment: Fragment,
    private val productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds,
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        var total = 0

        productWithCardSetAndSkuIds.let { total++ }
        productWithCardSetAndSkuIds.skuIds.let { total++ }
        return total
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CardDetailViewModel.Page.PRICES.position -> CardPricesBottomSheetFragment.newInstance(productWithCardSetAndSkuIds.product.id, true)
            CardDetailViewModel.Page.OVERVIEW.position -> CardDetailOverviewFragment.newInstance(productWithCardSetAndSkuIds)
            else -> throw Exception("view pager out of bounds")
        }
    }
}
