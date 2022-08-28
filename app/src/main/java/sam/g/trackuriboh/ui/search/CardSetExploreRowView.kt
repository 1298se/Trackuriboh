package sam.g.trackuriboh.ui.search

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.databinding.CardSetExploreRowViewBinding
import sam.g.trackuriboh.ui.common.HorizontalSpaceItemDecoration
import sam.g.trackuriboh.ui.search.adapters.CardSetExploreCardsAdapter

class CardSetExploreRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {
    private val binding = CardSetExploreRowViewBinding.inflate(LayoutInflater.from(context), this)

    fun setupWith(
        cardSet: CardSet, productsWithPrice: Map<Product, Double?>,
        onItemClickListener: CardSetExploreCardsAdapter.OnItemClickListener,
    ) {
        binding.cardExploreList.apply {
            adapter = CardSetExploreCardsAdapter(productsWithPrice, onItemClickListener)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                HorizontalSpaceItemDecoration(context.resources.getDimension(
                    R.dimen.list_item_row_spacing))
            )
        }
        binding.cardSetNameTextview.text = cardSet.name
    }
}