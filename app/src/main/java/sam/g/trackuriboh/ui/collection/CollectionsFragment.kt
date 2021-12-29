package sam.g.trackuriboh.ui.collection

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.FragmentCollectionsBinding
import sam.g.trackuriboh.ui.collection.adapters.CollectionsStateAdapter
import sam.g.trackuriboh.ui.collection.viewmodels.CollectionsViewModel
import sam.g.trackuriboh.utils.safeNavigate
import sam.g.trackuriboh.utils.setupAsTopLevelDestinationToolbar
import sam.g.trackuriboh.utils.viewBinding

/**
 * Fragment accessed from clicking "Watchlist" in bottom nav
 */
@AndroidEntryPoint
class CollectionsFragment : Fragment() {
    private val binding: FragmentCollectionsBinding by viewBinding(FragmentCollectionsBinding::inflate)

    private val viewModel: CollectionsViewModel by hiltNavGraphViewModels(R.id.collections_nav)

    private lateinit var collectionsStateAdapter: CollectionsStateAdapter

    private val navigationDestinationChangedListener = NavController.OnDestinationChangedListener { navController, destination, _ ->
        if (destination.parent?.id != R.id.collections_nav) {
            // When we change tabs we want to remove actionmode
            finishActionMode()
        }
    }

    companion object {
        const val ACTION_FINISH_ACTION_MODE = "CollectionsFragment_actionFinishActionMode"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initFab()
        initViewModelObservers()
        initFragmentResultListeners()
        initNavigationObservers()
    }

    private fun initToolbar() {
        binding.collectionsToolbar.setupAsTopLevelDestinationToolbar()
    }

    private fun initFab() {
        with(binding.collectionsFab) {
            setOnClickListener {
                findNavController().safeNavigate(
                    CollectionsFragmentDirections.actionCollectionsFragmentToCreateCollectionBottomSheetDialogFragment()
                )
            }
        }
    }

    private fun initViewPager(lists: List<UserList>) {
        with(binding.collectionsViewPager) {
            adapter = CollectionsStateAdapter(this@CollectionsFragment).also {
                it.setCollections(lists)
                collectionsStateAdapter = it
            }

            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    viewModel.setCurrentTabPosition(position)
                }
            })
        }

        TabLayoutMediator(binding.collectionsTabLayout, binding.collectionsViewPager) { tab, position ->
            tab.text = viewModel.collections.value?.get(position)?.name
        }.attach()
    }

    private fun initFragmentResultListeners() {

        with(parentFragmentManager) {
            setFragmentResultListener(
                CreateCollectionBottomSheetDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
                viewLifecycleOwner
            ) { _, bundle ->
                val collection = bundle.getParcelable<UserList>(CreateCollectionBottomSheetDialogFragment.COLLECTION_DATA_KEY)!!
                viewModel.createCollection(collection)
            }
        }
    }

    private fun initViewModelObservers() {
        viewModel.state.observe(viewLifecycleOwner) {
            with(binding.collectionsViewPager) {
                // When we change tabs we want to remove actionmode
                finishActionMode()

                if (binding.collectionsViewPager.currentItem != it.currentSelectedTabPosition) {
                    setCurrentItem(it.currentSelectedTabPosition, true)
                }
            }
        }

        viewModel.collections.observe(viewLifecycleOwner) {
            if (binding.collectionsViewPager.adapter == null) {
                initViewPager(it)
            } else {
                collectionsStateAdapter.setCollections(it)
            }
        }
    }

    private fun initNavigationObservers() {
        findNavController().addOnDestinationChangedListener(navigationDestinationChangedListener)
    }

    private fun finishActionMode() {
        // Scope to main nav because if we use the current back stack entry, it will be removed when onDestinationChanged
        // gets called
        findNavController().getBackStackEntry(R.id.main_nav).savedStateHandle.set(ACTION_FINISH_ACTION_MODE, true)
    }

    override fun onStop() {
        super.onStop()

        findNavController().removeOnDestinationChangedListener(navigationDestinationChangedListener)
    }
}