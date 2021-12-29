package sam.g.trackuriboh.ui.user_list.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.ItemUserListBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder

class UserListsAdapter : ListAdapter<UserList, UserListsAdapter.UserListViewHolder>(
    object : DiffUtil.ItemCallback<UserList>() {
        override fun areItemsTheSame(oldItem: UserList, newItem: UserList): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserList, newItem: UserList): Boolean =
            oldItem == newItem
    }
) {

    inner class UserListViewHolder(val binding: ItemUserListBinding) : BaseViewHolder<UserList>(binding.root) {
        init {
           binding.root.setOnClickListener {
                onItemClickListener?.onUserListItemClick(getItem(bindingAdapterPosition))
            }
        }

        override fun bind(item: UserList) {
            binding.itemUserListTitleTextview.text = item.name
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onUserListItemClick(list: UserList)
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