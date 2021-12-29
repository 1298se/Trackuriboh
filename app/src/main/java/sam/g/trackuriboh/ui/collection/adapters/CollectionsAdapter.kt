package sam.g.trackuriboh.ui.collection.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.ItemCollectionBinding
import sam.g.trackuriboh.ui.common.BaseViewHolder

class CollectionsAdapter : ListAdapter<UserList, CollectionsAdapter.CollectionViewHolder>(
    object : DiffUtil.ItemCallback<UserList>() {
        override fun areItemsTheSame(oldItem: UserList, newItem: UserList): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: UserList, newItem: UserList): Boolean =
            oldItem == newItem
    }
) {

    inner class CollectionViewHolder(val binding: ItemCollectionBinding) : BaseViewHolder<UserList>(binding.root) {
        init {
           binding.root.setOnClickListener {
                onItemClickListener?.onCollectionItemClick(getItem(bindingAdapterPosition))
            }
        }

        override fun bind(item: UserList) {
            binding.itemCollectionTitleTextview.text = item.name
        }
    }

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onCollectionItemClick(list: UserList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionViewHolder {
        return CollectionViewHolder(ItemCollectionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CollectionViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

}