package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.UserListWithCountAndTotal
import sam.g.trackuriboh.databinding.ItemUserListBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder

class UserListAdapter(private val onItemClickListener: OnItemClickListener)
    : ListAdapter<UserListWithCountAndTotal, UserListAdapter.UserListViewHolder>(USER_LIST_COMPARATOR) {

    interface OnItemClickListener {
        fun onItemClick(userList: UserList)
        fun onRenameClick(userList: UserList)
        fun onDeleteClick(userList: UserList)
    }

    companion object {
        private val USER_LIST_COMPARATOR = object : DiffUtil.ItemCallback<UserListWithCountAndTotal>() {
            override fun areItemsTheSame(
                oldItem: UserListWithCountAndTotal,
                newItem: UserListWithCountAndTotal
            ): Boolean =
                oldItem.userList.id == newItem.userList.id

            override fun areContentsTheSame(
                oldItem: UserListWithCountAndTotal,
                newItem: UserListWithCountAndTotal
            ): Boolean =
                oldItem == newItem
        }
    }

    inner class UserListViewHolder(private val binding: ItemUserListBinding) : BaseViewHolder<UserListWithCountAndTotal>(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(getItem(bindingAdapterPosition).userList)
            }

            binding.itemUserListMoreActions.setOnClickListener { v ->
                val popup = PopupMenu(itemView.context, v)
                popup.menuInflater.inflate(R.menu.item_user_list_actions, popup.menu)

                popup.setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.action_rename_user_list -> {
                            onItemClickListener.onRenameClick(getItem(bindingAdapterPosition).userList)

                            true
                        }
                        R.id.action_delete_user_list -> {
                            onItemClickListener.onDeleteClick(getItem(bindingAdapterPosition).userList)

                            true
                        }
                        else -> {
                            false
                        }
                    }
                }

                popup.show()
            }
        }

        override fun bind(item: UserListWithCountAndTotal) {
            Glide.with(itemView)
                .load(item.productImageUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_outline_collections_24)
                .into(binding.itemUserListImage)

            val titleText = itemView.context.getString(R.string.item_user_list_title, item.userList.name)
            binding.itemUserListTitleTextview.text = HtmlCompat.fromHtml(titleText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.itemUserListValueTextview.text = itemView.context.getString(R.string.item_user_list_total_value, item.totalValue)
            binding.itemUserListCountTextview.text = itemView.context.getString(R.string.item_user_list_card_count, item.totalCount)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
