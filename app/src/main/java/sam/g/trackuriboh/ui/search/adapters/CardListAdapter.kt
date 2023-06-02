package sam.g.trackuriboh.ui.search.adapters

import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.CardFilterRowViewBinding
import sam.g.trackuriboh.databinding.ItemCardBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.ui.search.viewmodels.CardListViewModel
import sam.g.trackuriboh.utils.joinStringsWithInterpunct

class CardListAdapter(private val onInteractionListener: OnInteractionListener) :
    PagingDataAdapter<CardListViewModel.UiState, BaseViewHolder<CardListViewModel.UiState>>(
        CARD_COMPARATOR
    ) {

    interface OnInteractionListener {
        fun onCardItemClick(cardId: Long)
        fun onFilterButtonClick()

        fun onSortOptionClick(sortOption: CardListViewModel.SortOption)
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<CardListViewModel.UiState>() {
            override fun areItemsTheSame(
                oldItem: CardListViewModel.UiState,
                newItem: CardListViewModel.UiState
            ): Boolean {
                return (oldItem is CardListViewModel.UiState.CardItemUiState && newItem is CardListViewModel.UiState.CardItemUiState &&
                        oldItem.productWithCardSetAndSkuIds.product.id == newItem.productWithCardSetAndSkuIds.product.id) ||
                        (oldItem is CardListViewModel.UiState.SortFilterUiState && newItem is CardListViewModel.UiState.SortFilterUiState)
            }

            override fun areContentsTheSame(
                oldItem: CardListViewModel.UiState,
                newItem: CardListViewModel.UiState
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is CardListViewModel.UiState.CardItemUiState -> R.layout.item_card
            is CardListViewModel.UiState.SortFilterUiState -> R.layout.card_filter_row_view
            else -> throw UnsupportedOperationException("Unknown item $item")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<CardListViewModel.UiState> {
        return if (viewType == R.layout.item_card) {
            CardViewHolder(
                ItemCardBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        } else {
            FilterViewHolder(
                CardFilterRowViewBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<CardListViewModel.UiState>,
        position: Int
    ) {
        val item = getItem(position)

        if (item != null) {
            holder.bind(item)
        }
    }

    inner class FilterViewHolder(private val binding: CardFilterRowViewBinding) :
        BaseViewHolder<CardListViewModel.UiState>(binding.root) {

        override fun bind(item: CardListViewModel.UiState) {
            item as CardListViewModel.UiState.SortFilterUiState

            binding.cardSortButton.text =
                itemView.context.getString(item.currentSortOrdering.getDisplayStringResId())

            binding.cardSortButton.setOnClickListener { button ->
                val popupMenu = PopupMenu(itemView.context, button, Gravity.START)

                item.sortOptions.forEachIndexed { index, sortOption ->

                    popupMenu.menu.add(Menu.NONE, index, index, sortOption.getDisplayStringResId())
                }

                popupMenu.setOnMenuItemClickListener {
                    onInteractionListener.onSortOptionClick(item.sortOptions[it.itemId])
                    true
                }

                popupMenu.show()
            }
        }
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        BaseViewHolder<CardListViewModel.UiState>(binding.root) {

        init {
            binding.root.setOnClickListener {
                val item =
                    getItem(bindingAdapterPosition) as CardListViewModel.UiState.CardItemUiState

                onInteractionListener.onCardItemClick(item.productWithCardSetAndSkuIds.product.id)
            }
        }

        override fun bind(item: CardListViewModel.UiState) {
            item as CardListViewModel.UiState.CardItemUiState
            val productWithCardSetAndSkuIds = item.productWithCardSetAndSkuIds

            Glide.with(itemView)
                .load(productWithCardSetAndSkuIds.product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemCardImage)

            with(binding) {
                itemCardTitleTextview.text = productWithCardSetAndSkuIds.product.name
                itemCardNumberRarityTextview.text = joinStringsWithInterpunct(
                    productWithCardSetAndSkuIds.product.number,
                    productWithCardSetAndSkuIds.rarity.name
                )
                itemCardSetNameTextview.text = productWithCardSetAndSkuIds.cardSet.name
                itemCardMarketPriceTextview.text = itemView.context.getString(
                    R.string.market_price_placeholder,
                    productWithCardSetAndSkuIds.product.marketPrice
                )
            }
        }
    }
}