package tang.song.edu.trackuriboh.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.trackuriboh.data.db.entities.CardSet
import tang.song.edu.trackuriboh.databinding.ItemCardSetBinding

private val SET_COMPARATOR = object : DiffUtil.ItemCallback<CardSet>() {
    override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
        oldItem.name == newItem.name

    override fun areContentsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
        oldItem == newItem
}

class CardSetListAdapter : PagingDataAdapter<CardSet, CardSetListAdapter.CardSetViewHolder>(SET_COMPARATOR) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(setName: String)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardSetViewHolder {
        return CardSetViewHolder(ItemCardSetBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardSetViewHolder, position: Int) {
        val cardSetItem = getItem(position)
        if (cardSetItem != null) {
            holder.bind(cardSetItem)
        }
    }

    inner class CardSetViewHolder(private val binding: ItemCardSetBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener{
                mOnItemClickListener?.onItemClick(getItem(bindingAdapterPosition)?.name ?: throw IllegalArgumentException("card set is null"))
            }
        }

        internal fun bind(item: CardSet) {
            binding.cardSetTitleTextview.text = item.name
            binding.cardSetReleaseDateTextview.text = item.publishedOn
        }
    }
}
