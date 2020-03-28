package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse

class CardListAdapter(val requestManager: RequestManager) :
    ListAdapter<CardResponse, CardListAdapter.CardViewHolder>(object :
        DiffUtil.ItemCallback<CardResponse>() {
        override fun areItemsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CardResponse, newItem: CardResponse): Boolean {
            return oldItem.desc == newItem.desc
        }
    }) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val itemCardView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false)
        return CardViewHolder(itemCardView)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(item: CardResponse) {
            requestManager.load(item.cardImages?.get(0)?.imageUrlSmall)
                .into(itemView.findViewById(R.id.item_card_image))
            itemView.findViewById<TextView>(R.id.item_card_title).text = item.name
        }
    }
}