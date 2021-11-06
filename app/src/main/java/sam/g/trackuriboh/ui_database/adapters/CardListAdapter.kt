package sam.g.trackuriboh.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import sam.g.trackuriboh.data.db.entities.CardWithSetInfo
import sam.g.trackuriboh.databinding.ItemCardBinding
import javax.inject.Inject

class CardListAdapter @Inject constructor(
    val requestManager: RequestManager
) : PagingDataAdapter<CardWithSetInfo, CardListAdapter.CardViewHolder>(CARD_COMPARATOR) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(cardId: Long)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<CardWithSetInfo>() {
            override fun areItemsTheSame(oldItem: CardWithSetInfo, newItem: CardWithSetInfo): Boolean =
                oldItem.card.id == newItem.card.id

            override fun areContentsTheSame(oldItem: CardWithSetInfo, newItem: CardWithSetInfo): Boolean =
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
                    getItem(bindingAdapterPosition)?.card?.id ?: throw IllegalArgumentException("card is null")
                )
            }
        }

        internal fun bind(item: CardWithSetInfo) {
            requestManager.load(item.card.imageUrl).into(binding.itemCardImage)

            binding.itemCardTitleTextview.text = item.card.name
            binding.itemCardTypeTextview.text = item.card.attribute

        }
    }
}