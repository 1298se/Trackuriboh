package sam.g.trackuriboh.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithSetInfo
import sam.g.trackuriboh.databinding.ItemCardBinding
import javax.inject.Inject

class CardListAdapter @Inject constructor(): PagingDataAdapter<ProductWithSetInfo, CardListAdapter.CardViewHolder>(CARD_COMPARATOR) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(cardId: Long)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<ProductWithSetInfo>() {
            override fun areItemsTheSame(oldItem: ProductWithSetInfo, newItem: ProductWithSetInfo): Boolean =
                oldItem.product.id == newItem.product.id

            override fun areContentsTheSame(oldItem: ProductWithSetInfo, newItem: ProductWithSetInfo): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        return CardViewHolder(ItemCardBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val cardItem = getItem(position)
        if (cardItem != null) {
            holder.bind(cardItem)
        }
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {
                mOnItemClickListener?.onItemClick(
                    getItem(bindingAdapterPosition)?.product?.id ?: throw IllegalArgumentException("card is null")
                )
            }
        }

        internal fun bind(item: ProductWithSetInfo) {
            val requestOptions = RequestOptions
                .placeholderOf(R.drawable.img_cardback)
                .error(R.drawable.img_cardback)
            Glide.with(itemView).setDefaultRequestOptions(requestOptions).load(item.product.imageUrl).into(binding.itemCardImage)

            binding.apply {
                itemCardTitleTextview.text = item.product.name
                itemCardNumberRarityTextview.text = itemView.resources.getString(
                    R.string.item_card_number_rarity_text,
                    item.product.number,
                    item.product.rarity
                )
                itemCardSetNameTextview.text = item.cardSet.name
            }
        }
    }
}