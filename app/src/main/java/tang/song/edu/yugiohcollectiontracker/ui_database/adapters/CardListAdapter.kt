package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dagger.hilt.android.scopes.ActivityScoped
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.types.CardType
import tang.song.edu.yugiohcollectiontracker.databinding.ItemCardBinding
import javax.inject.Inject

@ActivityScoped
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

    inner class CardViewHolder(private val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {
        private var card: Card? = null
        init {
            itemView.setOnClickListener(this)
        }

        internal fun bind(item: Card) {
            this.card = item
            requestManager.load(item.getDefaultImageURL()).into(binding.itemCardImage)

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
            mOnItemClickListener?.onItemClick(card?.cardId ?: throw IllegalArgumentException("card has no id"))
        }
    }
}