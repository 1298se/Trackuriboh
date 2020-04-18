package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.work.WorkInfo
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentDatabaseBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.DatabaseViewPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.DatabaseViewModel

class DatabaseFragment : BaseFragment() {
    private var _binding: FragmentDatabaseBinding? = null
    private val binding get() = _binding!!

    private lateinit var mViewModel: DatabaseViewModel
    private lateinit var mAdapter: DatabaseViewPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDatabaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(DatabaseViewModel::class.java)

        initTabLayoutWithViewPager()
        initObservers()
    }

    override fun onDestroyView() {
        _binding = null

        super.onDestroyView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the options menu from XML
        inflater.inflate(R.menu.database_actionbar_menu, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_filter -> {
                findNavController().navigate(R.id.action_databaseFragment_to_filterBottomSheetDialogFragment)
                true
            }
            R.id.action_database_sync -> {
                mViewModel.syncDatabase()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initObservers() {
        mViewModel.syncWorkInfo.observe(viewLifecycleOwner) { listOfWorkInfo ->
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@observe
            }

            val workInfo = listOfWorkInfo[0]

            if (workInfo.state.isFinished && workInfo.state == WorkInfo.State.FAILED) {
                showError(
                    R.string.database_sync_error_title,
                    R.string.database_sync_error_message
                )
            }
        }
    }

    private fun initTabLayoutWithViewPager() {
        binding.databaseViewPager.apply {
            adapter = DatabaseViewPagerAdapter(requireActivity()).also {
                mAdapter = it
            }


            TabLayoutMediator(binding.databaseTabLayout, this) { tab, position ->
                tab.text = when (position) {
                    0 -> getString(R.string.tab_card_title)
                    1 -> getString(R.string.tab_set_title)
                    else -> null
                }

            }.attach()
        }
    }
}
