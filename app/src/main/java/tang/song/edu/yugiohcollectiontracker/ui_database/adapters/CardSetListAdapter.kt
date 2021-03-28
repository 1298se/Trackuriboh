package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.databinding.ItemCardSetBinding

private val SET_COMPARATOR = object : DiffUtil.ItemCallback<CardSet>() {
    override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
        oldItem.setName == newItem.setName

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
                mOnItemClickListener?.onItemClick(getItem(bindingAdapterPosition)?.setName ?: throw IllegalArgumentException("card set is null"))
            }
        }

        internal fun bind(item: CardSet) {
            binding.cardSetTitleTextview.text = item.setName
            binding.cardSetReleaseDateTextview.text = item.releaseDate
        }
    }
}
