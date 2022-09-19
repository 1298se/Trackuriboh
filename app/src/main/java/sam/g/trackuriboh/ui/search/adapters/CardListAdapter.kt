package sam.g.trackuriboh.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.CardFilterRowViewBinding
import sam.g.trackuriboh.databinding.ItemCardBinding
import sam.g.trackuriboh.ui.search.viewmodels.CardListViewModel

class CardListAdapter(private val onInteractionListener: OnInteractionListener) :
    PagingDataAdapter<CardListViewModel.UiModel, RecyclerView.ViewHolder>(CARD_COMPARATOR) {

    interface OnInteractionListener {
        fun onCardItemClick(cardId: Long)
        fun onFilterButtonClick()
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<CardListViewModel.UiModel>() {
            override fun areItemsTheSame(
                oldItem: CardListViewModel.UiModel,
                newItem: CardListViewModel.UiModel
            ): Boolean {
                return (oldItem is CardListViewModel.UiModel.CardItem && newItem is CardListViewModel.UiModel.CardItem &&
                        oldItem.productWithCardSetAndSkuIds.product.id == newItem.productWithCardSetAndSkuIds.product.id) ||
                        (oldItem is CardListViewModel.UiModel.FilterHeaderItem && newItem is CardListViewModel.UiModel.FilterHeaderItem)
            }

            override fun areContentsTheSame(
                oldItem: CardListViewModel.UiModel,
                newItem: CardListViewModel.UiModel
            ): Boolean =
                oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (val item = getItem(position)) {
            is CardListViewModel.UiModel.CardItem -> R.layout.item_card
            is CardListViewModel.UiModel.FilterHeaderItem -> R.layout.card_filter_row_view
            else -> throw UnsupportedOperationException("Unknown item $item")
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val uiModel = getItem(position)) {
            is CardListViewModel.UiModel.CardItem -> (holder as CardViewHolder).bind(uiModel.productWithCardSetAndSkuIds)
            else -> {}
        }
    }

    inner class FilterViewHolder(binding: CardFilterRowViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.cardFilterContainer.setOnClickListener {
                onInteractionListener?.onFilterButtonClick()
            }
        }
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProductWithCardSetAndSkuIds) {
            Glide.with(itemView)
                .load(item.product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemCardImage)

            with(binding) {
                root.setOnClickListener {
                    onInteractionListener.onCardItemClick(item.product.id)
                }

                itemCardTitleTextview.text = item.product.name
                itemCardNumberRarityTextview.text = itemView.resources.getString(
                    R.string.number_rarity_oneline,
                    item.product.number,
                    item.rarity.name
                )
                itemCardSetNameTextview.text = item.cardSet.name
            }
        }
    }
}