package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.SetListAdapter
import javax.inject.Inject

class SetListFragment : Fragment() {
    @Inject
    lateinit var mViewModelFactory: SetViewModelFactory

    private lateinit var mViewModel: SetViewModel
    private lateinit var mAdapter: SetListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(SetViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_set_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)

        mViewModel.allSets.observe(this) { response ->
            if (response.isSuccess()) {
                mAdapter.setItems(response.data)
            }
        }
    }

    private fun initRecyclerView(view: View) {
        mAdapter = SetListAdapter()
        val layoutManager = LinearLayoutManager(requireContext())

        view.findViewById<RecyclerView>(R.id.set_list).apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
