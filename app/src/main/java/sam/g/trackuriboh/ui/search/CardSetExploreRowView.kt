package sam.g.trackuriboh.ui.search

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.parcelize.Parcelize
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.databinding.CardSetExploreRowViewBinding
import sam.g.trackuriboh.ui.common.HorizontalSpaceItemDecoration
import sam.g.trackuriboh.ui.database.adapters.CardSetExploreCardsAdapter


class CardSetExploreRowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : ConstraintLayout(context, attrs, defStyle) {
    private val binding = CardSetExploreRowViewBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        binding.cardExploreList.addItemDecoration(
            HorizontalSpaceItemDecoration(context.resources.getDimension(
                R.dimen.list_item_row_spacing))
        )
    }

    fun setupWith(
        cardSet: CardSet, products: List<Product>,
        onItemClickListener: CardSetExploreCardsAdapter.OnItemClickListener,
        onCardSetNameClick: (setId: Long) -> Unit,
    ) {
        binding.cardExploreList.apply {
            adapter = CardSetExploreCardsAdapter(products, onItemClickListener)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.cardSetNameTextview.apply {
            text = cardSet.name
            setOnClickListener {
                onCardSetNameClick(cardSet.id)
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        return SavedState(this.id, super.onSaveInstanceState(), binding.cardExploreList.computeHorizontalScrollOffset())
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        super.onRestoreInstanceState(state)
        val savedState = state as SavedState
        savedState.scrollPosition?.let { binding.cardExploreList.scrollToPosition(it) }
    }

    @Parcelize
    data class SavedState(
        val id: Int,
        val state: Parcelable?,
        val scrollPosition: Int?
    ) : BaseSavedState(state)
}