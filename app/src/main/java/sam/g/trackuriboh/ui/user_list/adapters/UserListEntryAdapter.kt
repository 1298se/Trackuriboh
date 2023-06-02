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
import sam.g.trackuriboh.utils.joinStringsWithInterpunct

class UserListEntryAdapter(private val onInteractionListener: OnInteractionListener) :
    ListAdapter<UserListDetailViewModel.ItemUiState, BaseViewHolder<UserListDetailViewModel.ItemUiState>>(
        object : DiffUtil.ItemCallback<UserListDetailViewModel.ItemUiState>() {
            override fun areItemsTheSame(
                oldItem: UserListDetailViewModel.ItemUiState,
                newItem: UserListDetailViewModel.ItemUiState
            ): Boolean {
                return ((oldItem is UserListDetailViewModel.ItemUiState.UserListEntryItem &&
                        newItem is UserListDetailViewModel.ItemUiState.UserListEntryItem &&
                        oldItem.data.entry.skuId == newItem.data.entry.skuId) ||
                        (oldItem is UserListDetailViewModel.ItemUiState.Header &&
                                newItem is UserListDetailViewModel.ItemUiState.Header &&
                                oldItem.totalCount == newItem.totalCount &&
                                oldItem.totalValue == newItem.totalValue)
                        )
            }

            override fun areContentsTheSame(
                oldItem: UserListDetailViewModel.ItemUiState,
                newItem: UserListDetailViewModel.ItemUiState
            ): Boolean {
                return oldItem == newItem
            }
    }
) {

    interface OnInteractionListener {
        fun onListEntryClick(productId: Long)
        fun onQuantityTextClick(entry: UserListEntry)
    }

    inner class UserListEntryViewHolder(
        val binding: ItemUserListEntryBinding,
    ) : BaseViewHolder<UserListDetailViewModel.ItemUiState>(binding.root) {

        init {
            binding.root.setOnClickListener {
                val userListEntryItem =
                    getItem(bindingAdapterPosition) as UserListDetailViewModel.ItemUiState.UserListEntryItem

                onInteractionListener.onListEntryClick(
                    userListEntryItem.data.skuWithMetadata.productWithCardSet.product.id
                )
            }

            binding.itemUserListEntryQuantityButton.setOnClickListener {
                val userListEntryItem =
                    getItem(bindingAdapterPosition) as UserListDetailViewModel.ItemUiState.UserListEntryItem

                onInteractionListener.onQuantityTextClick(userListEntryItem.data.entry)
            }
        }

        override fun bind(item: UserListDetailViewModel.ItemUiState) {
            val uiState = item as UserListDetailViewModel.ItemUiState.UserListEntryItem
            val skuWithConditionAndPrintingAndProduct = uiState.data.skuWithMetadata

            val productWithCardSet = skuWithConditionAndPrintingAndProduct.productWithCardSet

            val product = productWithCardSet.product
            val sku = skuWithConditionAndPrintingAndProduct.sku

            Glide.with(itemView)
                .load(product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemUserListEntryImage)

            binding.itemUserListEntryTitleTextview.text = product.name
            binding.itemUserListRarityNumberTextview.text = joinStringsWithInterpunct(
                product.number,
                productWithCardSet.rarity.name
            )

            binding.itemUserListEntryEditionConditionTextview.text = joinStringsWithInterpunct(
                skuWithConditionAndPrintingAndProduct.printing?.name,
                skuWithConditionAndPrintingAndProduct.condition?.name,
            )

            binding.itemUserListEntryQuantityButton.text = uiState.data.entry.quantity.toString()

            binding.itemUserListEntryPriceTextview.text = sku.lowestListingPrice?.let {
                itemView.context.getString(
                    R.string.price_placeholder,
                    it
                )
            } ?: itemView.context.getString(R.string.lbl_not_available)
        }
    }

    inner class HeaderViewHolder(
        val binding: ListHeaderBinding,
    ) : BaseViewHolder<UserListDetailViewModel.ItemUiState>(binding.root) {

        override fun bind(item: UserListDetailViewModel.ItemUiState) {
            with(item as UserListDetailViewModel.ItemUiState.Header) {
                val totalCount = totalCount
                val totalValue = totalValue

                binding.headerStartTextTextview.text = itemView.resources.getQuantityString(
                    R.plurals.user_list_detail_total_count, totalCount, totalCount
                )

                binding.headerEndTextTextview.text =
                    itemView.resources.getString(R.string.price_placeholder, totalValue)
            }

        }
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<UserListDetailViewModel.ItemUiState> {
        return when (viewType) {
            R.layout.item_user_list_entry -> UserListEntryViewHolder(
                ItemUserListEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            R.layout.list_header -> HeaderViewHolder(
                ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalStateException("invalid viewtype $viewType")
        }
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<UserListDetailViewModel.ItemUiState>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is UserListDetailViewModel.ItemUiState.UserListEntryItem -> R.layout.item_user_list_entry
            is UserListDetailViewModel.ItemUiState.Header -> R.layout.list_header
        }
    }

    override fun getItemId(position: Int): Long {
        return when (val item = getItem(position)) {
            is UserListDetailViewModel.ItemUiState.Header -> 0
            is UserListDetailViewModel.ItemUiState.UserListEntryItem -> item.data.entry.skuId
        }
    }
}
