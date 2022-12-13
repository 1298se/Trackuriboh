package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.databinding.ItemUserListEntryBinding
import sam.g.trackuriboh.databinding.ListHeaderBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.show

class UserListEntryAdapter(private val onInteractionListener: OnInteractionListener)
    : ListAdapter<UserListDetailViewModel.UiModel, BaseViewHolder<UserListDetailViewModel.UiModel>>(
    object : DiffUtil.ItemCallback<UserListDetailViewModel.UiModel>() {
        override fun areItemsTheSame(
            oldItem: UserListDetailViewModel.UiModel,
            newItem: UserListDetailViewModel.UiModel
        ): Boolean {
            return ((oldItem is UserListDetailViewModel.UiModel.UserListEntryItem &&
                        newItem is UserListDetailViewModel.UiModel.UserListEntryItem &&
                        oldItem.data.entry.skuId == newItem.data.entry.skuId) ||
                    (oldItem is UserListDetailViewModel.UiModel.Header &&
                            newItem is UserListDetailViewModel.UiModel.Header &&
                            oldItem.totalCount == newItem.totalCount &&
                            oldItem.totalValue == newItem.totalValue)
                    )
        }

        override fun areContentsTheSame(oldItem: UserListDetailViewModel.UiModel, newItem: UserListDetailViewModel.UiModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    private var inActionMode = false

    interface OnInteractionListener {
        fun onListEntryClick(skuId: Long)
        fun onQuantityTextClick(entry: UserListEntry)
        fun onListEntryLongClick(skuId: Long)
        fun onListEntryChecked(skuId: Long, isChecked: Boolean)
    }

    inner class UserListEntryViewHolder(
        val binding: ItemUserListEntryBinding,
    ) : BaseViewHolder<UserListDetailViewModel.UiModel>(binding.root) {

        init {
            binding.root.setOnClickListener {
                val userListEntryItem = getItem(bindingAdapterPosition) as UserListDetailViewModel.UiModel.UserListEntryItem

                if (inActionMode) {
                    binding.itemUserListEntryCheckbox.toggle()
                    onInteractionListener.onListEntryChecked(
                        userListEntryItem.data.entry.skuId,
                        binding.itemUserListEntryCheckbox.isChecked
                    )
                } else {
                    onInteractionListener.onListEntryClick(
                        userListEntryItem.data.skuWithConditionAndPrintingAndProduct.sku.id
                    )
                }
            }

            binding.root.setOnLongClickListener {
                val userListEntryItem = getItem(bindingAdapterPosition) as UserListDetailViewModel.UiModel.UserListEntryItem

                onInteractionListener.onListEntryLongClick(
                    userListEntryItem.data.entry.skuId
                )

                true
            }
        }

        override fun bind(item: UserListDetailViewModel.UiModel) {
            val uiModel = item as UserListDetailViewModel.UiModel.UserListEntryItem
            val skuWithConditionAndPrintingAndProduct = uiModel.data.skuWithConditionAndPrintingAndProduct
            val entryItem = uiModel.data.entry

            val productWithCardSet = skuWithConditionAndPrintingAndProduct.productWithCardSet

            val product = productWithCardSet.product
            val sku = skuWithConditionAndPrintingAndProduct.sku

            Glide.with(itemView)
                .load(product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemUserListEntryImage)

            binding.itemUserListEntryTitleTextview.text = product.name
            binding.itemUserListRarityNumberTextview.text = itemView.resources.getString(
                R.string.number_rarity_oneline,
                product.number,
                productWithCardSet.rarity.name
            )

            binding.itemUserListEntryEditionConditionTextview.text = itemView.resources.getString(
                R.string.edition_condition_oneline,
                skuWithConditionAndPrintingAndProduct.printing?.name,
                skuWithConditionAndPrintingAndProduct.condition?.name,
            )

            binding.itemUserListEntryCheckbox.show(inActionMode)

            binding.itemUserListEntryCheckbox.isChecked = uiModel.isChecked

            binding.itemUserListEntryPriceTextview.text = sku.lowestBasePrice?.let {
                itemView.context.getString(
                    R.string.lbl_price_with_dollar_sign,
                    it
                )
            } ?: itemView.context.getString(R.string.lbl_not_available)

            binding.itemUserListEntryQuantityAvgPurchasePriceTextview.text = itemView.resources.getString(
                R.string.item_user_list_quantity_avg_price,
                entryItem.quantity,
                entryItem.avgPurchasePrice
            )

            if (sku.lowestBasePrice != null) {
                val profit = (sku.lowestBasePrice - entryItem.avgPurchasePrice) * entryItem.quantity

                val profitPercentage = if (entryItem.avgPurchasePrice == 0.0) {
                    itemView.resources.getString(R.string.lbl_inf)
                } else {
                    (profit / entryItem.avgPurchasePrice).toInt().toString()
                }

                binding.itemUserListEntryProfitTextview.text = itemView.resources.getString(
                    R.string.item_user_list_profit,
                    profit,
                    profitPercentage
                )
            }
        }
    }

    inner class HeaderViewHolder(
        val binding: ListHeaderBinding,
    ) : BaseViewHolder<UserListDetailViewModel.UiModel>(binding.root) {

        override fun bind(item: UserListDetailViewModel.UiModel) {
            with (item as UserListDetailViewModel.UiModel.Header) {
                val totalCount = totalCount
                val totalValue = totalValue

                binding.headerStartTextTextview.text = itemView.resources.getQuantityString(
                    R.plurals.user_list_detail_total_count, totalCount, totalCount
                )

                binding.headerEndTextTextview.text = itemView.resources.getString(R.string.lbl_price_with_dollar_sign, totalValue)
            }

        }
    }

    companion object {
        private const val VIEW_TYPE_USER_LIST_ENTRY = 1
        private const val VIEW_TYPE_HEADER = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UserListDetailViewModel.UiModel> {
        return when (viewType) {
            VIEW_TYPE_USER_LIST_ENTRY -> UserListEntryViewHolder(
                ItemUserListEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalStateException("invalid viewtype $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<UserListDetailViewModel.UiModel>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UserListDetailViewModel.UiModel.UserListEntryItem -> VIEW_TYPE_USER_LIST_ENTRY
            is UserListDetailViewModel.UiModel.Header -> VIEW_TYPE_HEADER
        }
    }

    fun setInActionMode(active: Boolean) {
        if (active == inActionMode) {
            return
        }

        inActionMode = active

        notifyItemRangeChanged(0, itemCount)
    }
}
