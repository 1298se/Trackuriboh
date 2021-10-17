package sam.g.trackuriboh.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import sam.g.trackuriboh.data.db.entities.Card
import sam.g.trackuriboh.databinding.ItemCardBinding
import javax.inject.Inject

class CardListAdapter @Inject constructor(
    val requestManager: RequestManager
) : PagingDataAdapter<Card, CardListAdapter.CardViewHolder>(CARD_COMPARATOR) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(cardId: Long)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Card, newItem: Card): Boolean =
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
            itemView.setOnClickListener { mOnItemClickListener?.onItemClick(getItem(bindingAdapterPosition)?.id ?: throw IllegalArgumentException("card is null")) }
        }

        internal fun bind(item: Card) {
            requestManager.load(item.imageUrl).into(binding.itemCardImage)

            binding.itemCardTitleTextview.text = item.name
            binding.itemCardTypeTextview.text = ""
        }
    }
}