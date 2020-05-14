package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.models.CardType
import tang.song.edu.yugiohcollectiontracker.databinding.ItemCardBinding

class CardListAdapter(
    val onItemClickListener: OnItemClickListener,
    val requestManager: RequestManager
) : PagedListAdapter<Card, CardListAdapter.CardViewHolder>(CARD_COMPARATOR) {
    interface OnItemClickListener {
        fun onItemClick(cardId: Long)
    }

    init {
        setHasStableIds(true)
    }

    companion object {
        private val CARD_COMPARATOR = object : DiffUtil.ItemCallback<Card>() {
            override fun areItemsTheSame(oldItem: Card, newItem: Card): Boolean =
                oldItem.cardId == newItem.cardId

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

    override fun getItemId(position: Int): Long {
        return getItem(position)?.cardId ?: -1
    }

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        init {
            itemView.setOnClickListener(this)
        }

        internal fun bind(item: Card) {
            requestManager.load(item.cardImageList?.get(0)).into(binding.itemCardImage)

            binding.itemCardTitleTextview.text = item.name
            binding.itemCardTypeTextview.text = item.type.value

            binding.itemCardRaceAttributeAdTextview.text = when (item.type) {
                CardType.SPELL_CARD, CardType.TRAP_CARD -> item.race
                CardType.LINK_MONSTER -> itemView.context.getString(R.string.item_card_list_link_attribute_race_text, item.linkval, item.attribute, item.race)
                CardType.UNKNOWN -> null
                else -> itemView.context.getString(R.string.item_card_list_level_attribute_race_text, item.level, item.attribute, item.race)
            }
        }

        override fun onClick(p0: View?) {
            onItemClickListener.onItemClick(itemId)
        }
    }
}