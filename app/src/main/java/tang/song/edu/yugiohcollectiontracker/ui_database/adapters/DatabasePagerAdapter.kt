package tang.song.edu.yugiohcollectiontracker.ui_database.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.CardListFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.CardSetListFragment
import tang.song.edu.yugiohcollectiontracker.ui_database.DatabaseFragment

class DatabasePagerAdapter(private val fragment: DatabaseFragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardListFragment.newInstance(fragment.getQueryString())
            1 -> CardSetListFragment.newInstance(fragment.getQueryString())
            else -> throw Exception("view pager out of bounds")
        }
    }
}
