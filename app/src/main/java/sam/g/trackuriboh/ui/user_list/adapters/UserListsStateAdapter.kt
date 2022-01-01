package sam.g.trackuriboh.ui.user_list.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.ui.user_list.UserListDetailFragment

class UserListsStateAdapter(
    fragment: Fragment,
) : FragmentStateAdapter(fragment) {
    private var lists: List<UserList> = mutableListOf()


    override fun getItemCount(): Int = lists.size

    override fun createFragment(position: Int): Fragment = UserListDetailFragment.newInstance(lists[position])

    fun setUserLists(data: List<UserList>) {
        lists = data
        notifyDataSetChanged()
    }

    /*
     * Says in the docs we should override this method and containsItem https://developer.android.com/training/animation/vp2-migration#diffutil
     *
     * Not sure what it does because we're not using DiffUtil? Dug a little bit and don't see DiffUtil.
     */
    override fun getItemId(position: Int): Long {
        return lists[position].id
    }

    override fun containsItem(itemId: Long): Boolean {
        return lists.any { it.id == itemId }
    }
}
