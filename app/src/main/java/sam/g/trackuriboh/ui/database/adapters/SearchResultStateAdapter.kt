package sam.g.trackuriboh.ui.database.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.ui.database.CardListFragment
import sam.g.trackuriboh.ui.database.CardSetListFragment

class SearchResultStateAdapter(fragment: Fragment, private val query: String?) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardListFragment.newInstance(query)
            1 -> CardSetListFragment.newInstance(query)
            else -> throw Exception("view pager out of bounds")
        }
    }
}
