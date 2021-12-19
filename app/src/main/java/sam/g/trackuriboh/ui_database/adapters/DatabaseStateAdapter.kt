package sam.g.trackuriboh.ui_database.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.ui_database.CardListFragment
import sam.g.trackuriboh.ui_database.CardSetListFragment
import sam.g.trackuriboh.ui_database.DatabaseFragment.Companion.CARD_PAGE_POSITION
import sam.g.trackuriboh.ui_database.DatabaseFragment.Companion.SET_PAGE_POSITION

class DatabaseStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CARD_PAGE_POSITION -> CardListFragment()
            SET_PAGE_POSITION -> CardSetListFragment()
            else -> throw Exception("view pager out of bounds")
        }
    }
}
