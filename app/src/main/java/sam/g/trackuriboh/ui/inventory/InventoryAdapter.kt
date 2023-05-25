package sam.g.trackuriboh.ui.inventory

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.google.android.material.color.MaterialColors
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.InventorySummaryBinding
import sam.g.trackuriboh.databinding.ItemInventoryBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.utils.joinStringsWithInterpunct

class InventoryAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<InventoryViewModel.ItemUiState, BaseViewHolder<InventoryViewModel.ItemUiState>>(
        object : DiffUtil.ItemCallback<InventoryViewModel.ItemUiState>() {
            override fun areItemsTheSame(
                oldItem: InventoryViewModel.ItemUiState,
                newItem: InventoryViewModel.ItemUiState
            ): Boolean {
                return (oldItem is InventoryViewModel.ItemUiState.InventoryItemUiState &&
                        newItem is InventoryViewModel.ItemUiState.InventoryItemUiState &&
                        oldItem.data.inventory.id == newItem.data.inventory.id)
            }

            override fun areContentsTheSame(
                oldItem: InventoryViewModel.ItemUiState,
                newItem: InventoryViewModel.ItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    ) {

    interface OnInteractionListener {
        fun onItemClick(inventoryId: Long)
    }

    inner class SummaryViewHolder(
        val binding: InventorySummaryBinding,
    ) : BaseViewHolder<InventoryViewModel.ItemUiState>(binding.root) {
        override fun bind(item: InventoryViewModel.ItemUiState) {
            val uiState = item as InventoryViewModel.ItemUiState.SummaryUiState
            val context = itemView.context

            binding.totalValueTextview.text =
                context.getString(R.string.price_placeholder, uiState.totalValue)
            binding.totalUnrealizedProfitTextview.text = context.getString(
                R.string.total_unrealized_profit_placeholder,
                uiState.totalUnrealizedProfit
            )
            binding.totalRealizedProfitTextview.text = context.getString(
                R.string.total_realized_profit_placeholder,
                uiState.totalRealizedProfit
            )
            binding.totalPurchaseAmountTextview.text = context.getString(
                R.string.total_purchase_amount_placeholder,
                uiState.totalPurchaseAmount
            )
            binding.grossProfitTextview.text = context.getString(
                R.string.gross_profit_placeholder,
                (uiState.totalRealizedProfit - uiState.totalPurchaseAmount)
            )
        }

    }

    inner class InventoryViewHolder(
        val binding: ItemInventoryBinding,
    ) : BaseViewHolder<InventoryViewModel.ItemUiState>(binding.root) {
        init {
            binding.root.setOnClickListener {
                val uiModel =
                    getItem(bindingAdapterPosition) as InventoryViewModel.ItemUiState.InventoryItemUiState

                onInteractionListener.onItemClick(uiModel.data.inventory.id)
            }
        }

        override fun bind(item: InventoryViewModel.ItemUiState) {
            val uiModel = item as InventoryViewModel.ItemUiState.InventoryItemUiState
            val inventory = uiModel.data.inventory
            val skuWithMetadata = uiModel.data.skuWithMetadata

            val productWithCardSet = skuWithMetadata.productWithCardSet

            val product = productWithCardSet.product
            val sku = skuWithMetadata.sku

            Glide.with(itemView)
                .load(product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemUserListEntryImage)

            binding.itemUserListEntryTitleTextview.text = product.name
            binding.itemUserListRarityNumberTextview.text =
                joinStringsWithInterpunct(product.number, productWithCardSet.rarity.name)

            binding.itemUserListEntryEditionConditionTextview.text = joinStringsWithInterpunct(
                skuWithMetadata.printing?.name,
                skuWithMetadata.condition?.name,
            )

            binding.itemUserListEntryPriceTextview.text = sku.lowestBasePrice?.let {
                itemView.context.getString(
                    R.string.price_placeholder,
                    it
                )
            } ?: itemView.context.getString(R.string.lbl_not_available)

            binding.itemUserListEntryQuantityAvgPurchasePriceTextview.text =
                itemView.resources.getString(
                    R.string.quantity_avg_price_oneline,
                    inventory.quantity,
                    inventory.avgPurchasePrice
                )

            binding.itemUserListEntryProfitTextview.text = itemView.resources.getString(
                R.string.item_user_list_profit_with_percentage,
                uiModel.data.getTotalUnrealizedProfit(),
                uiModel.data.getUnrealizedProfitPercentagePerCard()?.toString(),
            )

            val unrealizedProfitPercentagePerCard =
                uiModel.data.getUnrealizedProfitPercentagePerCard()

            if (unrealizedProfitPercentagePerCard != null) {
                binding.itemUserListEntryProfitTextview.setTextColor(
                    if (unrealizedProfitPercentagePerCard < 0) {
                        MaterialColors.getColor(itemView.context, R.attr.colorError, Color.RED)
                    } else {
                        MaterialColors.getColor(itemView.context, R.attr.colorPrimary, Color.GREEN)
                    }
                )
            }
        }
    }

    init {
        // Set so we can use getItemId
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<InventoryViewModel.ItemUiState> {
        return when (viewType) {
            R.layout.item_inventory -> InventoryViewHolder(
                ItemInventoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            R.layout.inventory_summary -> SummaryViewHolder(
                InventorySummaryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalStateException("invalid viewtype $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<InventoryViewModel.ItemUiState>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is InventoryViewModel.ItemUiState.InventoryItemUiState -> R.layout.item_inventory
            is InventoryViewModel.ItemUiState.SummaryUiState -> R.layout.inventory_summary
        }
    }

    override fun getItemId(position: Int): Long {
        return when (val item = getItem(position)) {
            is InventoryViewModel.ItemUiState.InventoryItemUiState -> item.data.inventory.id
            is InventoryViewModel.ItemUiState.SummaryUiState -> -1
        }
    }
}
