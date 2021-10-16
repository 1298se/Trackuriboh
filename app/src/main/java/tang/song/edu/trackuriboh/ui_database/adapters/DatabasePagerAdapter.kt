package tang.song.edu.trackuriboh.ui_database.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tang.song.edu.trackuriboh.ui_database.CardListFragment
import tang.song.edu.trackuriboh.ui_database.CardSetListFragment

class DatabasePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardListFragment()
            1 -> CardSetListFragment()
            else -> throw Exception("view pager out of bounds")
        }
    }
}
