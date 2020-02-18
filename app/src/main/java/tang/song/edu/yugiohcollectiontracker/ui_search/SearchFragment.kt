package tang.song.edu.yugiohcollectiontracker.ui_search

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import tang.song.edu.yugiohcollectiontracker.R

class SearchFragment : Fragment(), MenuItem.OnActionExpandListener {
    private lateinit var mSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_actionbar_menu, menu)

        menu.findItem(R.id.open_search).apply {
            mSearchView = this.actionView as SearchView
            setOnActionExpandListener(this@SearchFragment)

            expandActionView()
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
        return true
    }

    override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
        activity?.findNavController(R.id.nav_host_fragment)?.navigateUp()

        return true
    }

}
