package sam.g.trackuriboh.ui.search.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.databinding.ItemCardSetBinding
import java.text.DateFormat
import java.util.*

private val SET_COMPARATOR = object : DiffUtil.ItemCallback<CardSet>() {
    override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
        oldItem == newItem
}

class CardSetListAdapter : PagingDataAdapter<CardSet, CardSetListAdapter.CardSetViewHolder>(
    SET_COMPARATOR
) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(setId: Long)
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
            binding.root.setOnClickListener{
                mOnItemClickListener?.onItemClick(
                    getItem(bindingAdapterPosition)?.id ?:
                    throw IllegalArgumentException("card set id is null")
                )
            }
        }

        internal fun bind(item: CardSet) {
            val releaseDate = item.releaseDate?.let {
                DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(item.releaseDate)
            } ?: itemView.context.getString(R.string.lbl_no_date_available)

            binding.cardSetTitleTextview.text = item.name
            binding.cardSetReleaseDateTextview.text = releaseDate
        }
    }
}
