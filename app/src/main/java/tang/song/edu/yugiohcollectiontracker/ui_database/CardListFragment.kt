package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardViewModelFactory
import javax.inject.Inject

class CardListFragment : Fragment() {
    @Inject
    lateinit var mRequestManager: RequestManager
    @Inject
    lateinit var mViewModelFactory: CardViewModelFactory

    private lateinit var mAdapter: CardListAdapter
    private lateinit var mViewModel: CardViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel =
            ViewModelProvider(requireActivity(), mViewModelFactory).get(CardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        mAdapter = CardListAdapter(mRequestManager)
        val layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<RecyclerView>(R.id.card_list).apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
