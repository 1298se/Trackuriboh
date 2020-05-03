package tang.song.edu.yugiohcollectiontracker.ui_card_detail.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.CardDetailOverviewFragment

class CardDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var mCard: Card? = null

    override fun getItemCount(): Int {
        return if (mCard != null) 2 else 0
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardDetailOverviewFragment.newInstance(mCard)
            1 -> CardDetailOverviewFragment.newInstance(mCard)
            else -> throw Exception("view pager out of bounds")
        }
    }

    fun setCard(card: Card) {
        mCard = card
        notifyDataSetChanged()
    }
}
