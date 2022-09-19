package sam.g.trackuriboh.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commitNow
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.BottomSheetCardFilterBinding
import sam.g.trackuriboh.ui.search.viewmodels.CardFilterViewModel
import sam.g.trackuriboh.utils.setFullScreen
import sam.g.trackuriboh.utils.viewBinding

class CardFilterBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding by viewBinding(BottomSheetCardFilterBinding::inflate)

    companion object {
        // TODO: Fix magic strings
        const val ARG_QUERY = "query"
        const val ARG_FILTERS = "filter"

        fun newInstance(query: String?, filters: CardFilterViewModel.FilterUiModel? = null) =
            CardFilterBottomSheetFragment().apply {

                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                    putParcelable(ARG_FILTERS, filters)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setFullScreen()

        val navHostFragment = NavHostFragment()
        childFragmentManager.commitNow {
            replace(binding.cardFilterNavHost.id, navHostFragment)
        }

        navHostFragment.navController.setGraph(R.navigation.card_filter, arguments)
    }
}