package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserListEntry
import sam.g.trackuriboh.databinding.ItemUserListEntryBinding
import sam.g.trackuriboh.databinding.ListAddItemFooterBinding
import sam.g.trackuriboh.databinding.ListHeaderBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.show

class UserListEntryAdapter
    : ListAdapter<UserListDetailViewModel.UiModel, BaseViewHolder<UserListDetailViewModel.UiModel>>(
    object : DiffUtil.ItemCallback<UserListDetailViewModel.UiModel>() {
        override fun areItemsTheSame(oldItem: UserListDetailViewModel.UiModel, newItem: UserListDetailViewModel.UiModel): Boolean {
            return ((oldItem is UserListDetailViewModel.UiModel.UserListEntryItem &&
                    newItem is UserListDetailViewModel.UiModel.UserListEntryItem &&
                    oldItem.data.entry.skuId == newItem.data.entry.skuId) ||
                    (oldItem is UserListDetailViewModel.UiModel.Header &&
                    newItem is UserListDetailViewModel.UiModel.Header &&
                    oldItem.title == newItem.title) ||
                    (oldItem is UserListDetailViewModel.UiModel.Footer && newItem is UserListDetailViewModel.UiModel.Footer)
                    )
        }

        override fun areContentsTheSame(oldItem: UserListDetailViewModel.UiModel, newItem: UserListDetailViewModel.UiModel): Boolean {
            return oldItem == newItem
        }
    }
) {
    private var onItemClickListener: OnItemClickListener? = null

    private var inActionMode = false

    interface OnItemClickListener {
        fun onListEntryClick(entry: UserListEntry)
        fun onAddCardClick()
        fun onListEntryLongClick(entry: UserListEntry)
        fun onListEntryChecked(entry: UserListEntry, isChecked: Boolean)
    }

    inner class UserListEntryViewHolder(
        val binding: ItemUserListEntryBinding,
    ) : BaseViewHolder<UserListDetailViewModel.UiModel>(binding.root) {

        init {
            binding.root.setOnClickListener {
                if (inActionMode) {
                    binding.itemUserListEntryCheckbox.toggle()

                    onItemClickListener?.onListEntryChecked(
                        (getItem(bindingAdapterPosition) as UserListDetailViewModel.UiModel.UserListEntryItem).data.entry,
                        binding.itemUserListEntryCheckbox.isChecked
                    )
                } else {
                    onItemClickListener?.onListEntryClick(
                        (getItem(bindingAdapterPosition) as UserListDetailViewModel.UiModel.UserListEntryItem).data.entry
                    )
                }
            }

            binding.root.setOnLongClickListener {
                onItemClickListener?.onListEntryLongClick(
                    (getItem(bindingAdapterPosition) as UserListDetailViewModel.UiModel.UserListEntryItem).data.entry
                )

                true
            }
        }

        override fun bind(item: UserListDetailViewModel.UiModel) {
            val entryItem = item as UserListDetailViewModel.UiModel.UserListEntryItem
            val skuWithConditionAndPrintingAndProduct = entryItem.data.skuWithConditionAndPrintingAndProduct
            val product = skuWithConditionAndPrintingAndProduct.productWithCardSet.product

            Glide.with(itemView)
                .load(product.imageUrl)
                .placeholder(R.drawable.img_cardback)
                .into(binding.itemUserListEntryImage)

            binding.itemUserListEntryTitleTextview.text = product.name
            binding.itemUserListRarityNumberTextview.text = itemView.resources.getString(
                R.string.item_card_number_rarity,
                product.number,
                product.rarity
            )

            binding.itemUserListEntryEditionConditionTextview.text = itemView.resources.getString(
                R.string.item_user_list_edition_condition,
                skuWithConditionAndPrintingAndProduct.printing?.name,
                skuWithConditionAndPrintingAndProduct.condition?.name,
            )

            binding.itemUserListEntryCheckbox.show(inActionMode)

            binding.itemUserListEntryCheckbox.isChecked = entryItem.isChecked
        }

    }

    inner class HeaderViewHolder(
        val binding: ListHeaderBinding,
    ) : BaseViewHolder<UserListDetailViewModel.UiModel>(binding.root) {

        override fun bind(item: UserListDetailViewModel.UiModel) {
            val title = (item as UserListDetailViewModel.UiModel.Header).title

            binding.headerTextTextview.text = title
        }
    }

    inner class FooterViewHolder(
        val binding: ListAddItemFooterBinding
    ) : BaseViewHolder<UserListDetailViewModel.UiModel>(binding.root) {

        init {
            binding.root.setOnClickListener { onItemClickListener?.onAddCardClick() }
        }

        override fun bind(item: UserListDetailViewModel.UiModel) {
            binding.listAddItemFooterText.text = itemView.context.getString(R.string.user_list_detail_add_new_card)
        }
    }

    companion object {
        private const val VIEW_TYPE_USER_LIST_ENTRY = 1
        private const val VIEW_TYPE_HEADER = 2
        private const val VIEW_TYPE_FOOTER = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<UserListDetailViewModel.UiModel> {
        return when (viewType) {
            VIEW_TYPE_USER_LIST_ENTRY -> UserListEntryViewHolder(
                ItemUserListEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_HEADER -> HeaderViewHolder(
                ListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            VIEW_TYPE_FOOTER -> FooterViewHolder(
                ListAddItemFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            UserListDetailViewModel.UiModel.Footer -> VIEW_TYPE_FOOTER
            is UserListDetailViewModel.UiModel.Header -> VIEW_TYPE_HEADER
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun setInActionMode(active: Boolean) {
        inActionMode = active

        notifyItemRangeChanged(0, itemCount)
    }
}
