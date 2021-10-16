package tang.song.edu.trackuriboh.ui_card_detail.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import tang.song.edu.trackuriboh.data.db.relations.CardWithSetInfo
import tang.song.edu.trackuriboh.ui_card_detail.CardDetailOverviewFragment
import tang.song.edu.trackuriboh.ui_card_detail.CardDetailSetInfoFragment

class CardDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var mCardWithSetInfo: CardWithSetInfo? = null

    override fun getItemCount(): Int {
        return if (mCardWithSetInfo != null) 2 else 0
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CardDetailOverviewFragment.newInstance(mCardWithSetInfo?.card)
            1 -> CardDetailSetInfoFragment.newInstance(mCardWithSetInfo?.sets)
            else -> throw Exception("view pager out of bounds")
        }
    }

    fun setCard(card: CardWithSetInfo?) {
        mCardWithSetInfo = card
        notifyDataSetChanged()
    }
}
