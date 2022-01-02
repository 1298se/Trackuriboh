package sam.g.trackuriboh.ui.database

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardListBinding
import sam.g.trackuriboh.ui.common.VerticalSpaceItemDecoration
import sam.g.trackuriboh.ui.database.CardListFragment.Companion.FRAGMENT_RESULT_REQUEST_KEY
import sam.g.trackuriboh.ui.database.CardListFragment.Companion.newInstance
import sam.g.trackuriboh.ui.database.adapters.CardListAdapter
import sam.g.trackuriboh.ui.database.viewmodels.BaseSearchViewModel
import sam.g.trackuriboh.ui.database.viewmodels.CardListViewModel
import sam.g.trackuriboh.utils.*

/**
 * !IMPORTANT
 * A fragment displaying a list of cards. Navigation destinations should use this as an embedded fragment
 * and get results via [FRAGMENT_RESULT_REQUEST_KEY]. We do it this way because if this was a destination by itself,
 * there are two main issues we run into:
 * 1. It is hard for other destinations to receive results as both Fragment Result API and Navigation SavedStateHandle API require
 * lifecycle owner observers. However,if this is a destination they won't be able the result until this fragment is removed from the back stack,
 * but we don't always want to remove it from the back stack.
 * Hence, it is hard for the navigation logic to be handled by other destination (it could be handled by the Activity but that solution is very
 * spaghetti code).
 *
 * 2. Only one set of NavigationDirections will be created, which doesn't work well because this fragment will be a destination in multiple graphs.
 * We can use the NavigationAction id directly to navigate but then we don't get the benefits of SafeArgs, and it will also be spaghetti code
 * because it's better to delegate the navigation logic elsewhere instead of passing in flags to determine which navigation action to take.
 *
 * 3. Reduces use of magic strings for navigation because we call [newInstance] to create the fragment.
 */
@AndroidEntryPoint
class CardListFragment : BaseSearchListFragment<ProductWithCardSetAndSkuIds>(), CardListAdapter.OnItemClickListener {
    private lateinit var cardListAdapter: CardListAdapter

    private val binding by viewBinding(FragmentCardListBinding::inflate)

    private val viewModel: CardListViewModel by viewModels()

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "CardListFragment_fragmentResultRequestKey"
        const val CARD_ID_DATA_KEY = "CardListFragment_cardId"

        // This is the same value as the navArg name so that the SavedStateHandle can acess from either
        /**
         * Set this value to true for this fragment to display its own search toolbar.
         */
        const val ARG_SET_ID = "CardListFragment_argSetId"

        fun newInstance(setId: Long? = null) =
            CardListFragment().apply {
                if (setId != null) {
                    arguments = Bundle().apply {
                        putLong(ARG_SET_ID, setId)
                    }
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun getViewModel(): BaseSearchViewModel<ProductWithCardSetAndSkuIds> = viewModel

    override fun getListView(): RecyclerView = binding.cardList

    override fun getAdapter(): PagingDataAdapter<ProductWithCardSetAndSkuIds, out RecyclerView.ViewHolder> = cardListAdapter

    override fun onCardItemClick(cardId: Long) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(CARD_ID_DATA_KEY to cardId))
    }

    private fun initRecyclerView() {
        cardListAdapter = CardListAdapter().apply {
            setOnItemClickListener(this@CardListFragment)
        }

        binding.cardList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cardListAdapter
            addItemDecoration(VerticalSpaceItemDecoration(resources.getDimension(R.dimen.list_item_large_row_spacing)))
        }
    }
}
