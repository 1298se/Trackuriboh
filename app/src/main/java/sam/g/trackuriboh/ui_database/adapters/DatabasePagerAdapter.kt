package sam.g.trackuriboh.ui_database.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import sam.g.trackuriboh.ui_database.CardSetListFragment
import sam.g.trackuriboh.ui_price.CardDetailPricesFragment

class DatabasePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            // TODO: We are doing this temporarily for Google bug tracker
            0 -> CardDetailPricesFragment.newInstance(listOf(263276))
            1 -> CardSetListFragment()
            else -> throw Exception("view pager out of bounds")
        }
    }
}
