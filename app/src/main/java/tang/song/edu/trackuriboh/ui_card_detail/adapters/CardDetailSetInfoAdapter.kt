package tang.song.edu.trackuriboh.ui_card_detail.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.trackuriboh.data.db.relations.CardSetInfo
import tang.song.edu.trackuriboh.databinding.ItemCardDetailSetInfoBinding

class CardDetailSetInfoAdapter(private val cardSetInfoList: List<CardSetInfo>) : RecyclerView.Adapter<CardDetailSetInfoAdapter.CardDetailSetInfoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDetailSetInfoViewHolder {
        return CardDetailSetInfoViewHolder(ItemCardDetailSetInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CardDetailSetInfoViewHolder, position: Int) {
        holder.bind(cardSetInfoList[position])
    }

    override fun getItemCount(): Int {
        return cardSetInfoList.size
    }

    inner class CardDetailSetInfoViewHolder(private val binding: ItemCardDetailSetInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener {

            }
        }

        internal fun bind(cardSetInfo: CardSetInfo) {
            binding.itemCardDetailSetInfoCardnumberTextview.text = cardSetInfo.cardNumber
            binding.itemCardDetailSetInfoRarityTextview.text = cardSetInfo.rarity
        }
    }
}