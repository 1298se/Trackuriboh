package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabaseViewPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_search.SearchActivity

class DatabaseFragment : Fragment() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var mTabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_database, container, false)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.database_actionbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.open_search -> {
                startActivity(Intent(context, SearchActivity::class.java))
                true
            }
            else -> false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTabLayoutWithViewPager(view)
    }

    private fun initTabLayoutWithViewPager(view: View) {
        mViewPager = view.findViewById(R.id.database_view_pager)
        mViewPager.adapter = DatabaseViewPagerAdapter(requireActivity())

        mTabLayout = view.findViewById(R.id.database_tab_layout)
        TabLayoutMediator(mTabLayout, mViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_card_title)
                1 -> getString(R.string.tab_set_title)
                else -> null
            }

        }.attach()
    }
}
