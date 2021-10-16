package tang.song.edu.trackuriboh.ui_inventory.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.trackuriboh.R
import tang.song.edu.trackuriboh.data.db.entities.CardInventory
import tang.song.edu.trackuriboh.databinding.ItemInventoryBinding
import javax.inject.Inject

class InventoryListAdapter @Inject constructor(
    val requestManager: RequestManager,
) : PagingDataAdapter<CardInventory, InventoryListAdapter.InventoryViewHolder>(INVENTORY_COMPARATOR) {
    private var mOnItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(inventoryId: Long)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mOnItemClickListener = onItemClickListener
    }

    companion object {
        private val INVENTORY_COMPARATOR = object : DiffUtil.ItemCallback<CardInventory>() {
            override fun areItemsTheSame(oldItem: CardInventory, newItem: CardInventory): Boolean =
                oldItem.cardId == newItem.cardId

            override fun areContentsTheSame(oldItem: CardInventory, newItem: CardInventory): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InventoryViewHolder {
        return InventoryViewHolder(ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: InventoryViewHolder, position: Int) {
        val inventoryItem = getItem(position)
        if (inventoryItem != null) {
            holder.bind(inventoryItem)
        }
    }

    inner class InventoryViewHolder(private val binding: ItemInventoryBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            itemView.setOnClickListener{ mOnItemClickListener?.onItemClick(getItem(bindingAdapterPosition)?.inventoryId ?: throw IllegalArgumentException("inventory has no id")) }
        }

        internal fun bind(item: CardInventory) {
            requestManager.load(item.cardImageURL).into(binding.itemInventoryImage)

            binding.itemInventoryTitleTextview.text = item.cardName
            binding.itemInventoryQuantityTextview.text = itemView.context.getString(R.string.item_inventory_quantity_text, item.quantity)
            binding.itemInventorySetcodeRarityEditionTextview.text = itemView.context.getString(R.string.item_inventory_setcode_rarity_edition_text, item.cardNumber, item.rarity, "1st Edition")
            binding.itemInventoryAvgPurchasePriceTextview.text = itemView.context.getString(R.string.item_inventory_avg_purchase_price, item.curAvgPurchasePrice)
        }
    }
}