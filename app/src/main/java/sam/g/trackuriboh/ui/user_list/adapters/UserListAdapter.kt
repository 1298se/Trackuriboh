package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.data.db.relations.UserListWithCountAndTotal
import sam.g.trackuriboh.databinding.ItemUserListBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder

class UserListAdapter
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

    private var onItemClickListener: OnItemClickListener? = null

    inner class UserListViewHolder(private val binding: ItemUserListBinding) : BaseViewHolder<UserListWithCountAndTotal>(binding.root) {
        init {
            binding.root.setOnClickListener {
                onItemClickListener?.onItemClick(getItem(bindingAdapterPosition).userList)
            }

            binding.itemUserListRenameButton.setOnClickListener {
                onItemClickListener?.onRenameClick(getItem(bindingAdapterPosition).userList)
            }

            binding.itemUserListDeleteButton.setOnClickListener {
                onItemClickListener?.onDeleteClick(getItem(bindingAdapterPosition).userList)
            }
        }

        override fun bind(item: UserListWithCountAndTotal) {
            Glide.with(itemView)
                .load(item.productImageUrl)
                .circleCrop()
                .placeholder(R.drawable.ic_outline_collections_24)
                .into(binding.itemUserListImage)

            val titleText = itemView.context.getString(R.string.item_user_list_title, item.userList.name, item.totalCount)
            binding.itemUserListTitleTextview.text = HtmlCompat.fromHtml(titleText, HtmlCompat.FROM_HTML_MODE_LEGACY)
            binding.itemUserListValueTextview.text = itemView.context.getString(R.string.item_user_list_total_value, item.totalValue)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListViewHolder {
        return UserListViewHolder(ItemUserListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: UserListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }
}
