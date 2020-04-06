package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet

class CardSetListAdapter : PagedListAdapter<CardSet, CardSetListAdapter.SetViewHolder>(SET_COMPARATOR) {
    companion object {
        private val SET_COMPARATOR = object : DiffUtil.ItemCallback<CardSet>() {
            override fun areItemsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
                oldItem.setCode == newItem.setCode

            override fun areContentsTheSame(oldItem: CardSet, newItem: CardSet): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetViewHolder {
        return SetViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_set, parent, false))
    }

    override fun onBindViewHolder(holder: SetViewHolder, position: Int) {
        val cardSetItem = getItem(position)
        if (cardSetItem != null) {
            holder.bind(cardSetItem)
        }
    }

    inner class SetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal fun bind(item: CardSet) {
            itemView.findViewById<TextView>(R.id.item_title_textview).text = (item.setName)
        }
    }

}
