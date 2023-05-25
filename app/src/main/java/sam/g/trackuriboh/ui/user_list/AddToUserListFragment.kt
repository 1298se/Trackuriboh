package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentAddToUserListBinding
import sam.g.trackuriboh.ui.card_selection.CardSelectionFragment
import sam.g.trackuriboh.utils.showSnackbar
import sam.g.trackuriboh.utils.viewBinding

class AddToUserListFragment : Fragment() {
    private val binding: FragmentAddToUserListBinding by viewBinding(FragmentAddToUserListBinding::inflate)

    private val args: AddToUserListFragmentArgs by navArgs()

    companion object {
        private val CARD_SELECTION_FRAGMENT_TAG: String = CardSelectionFragment::class.java.name
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (childFragmentManager.findFragmentByTag(CARD_SELECTION_FRAGMENT_TAG) == null) {
            childFragmentManager.commit {
                replace(
                    binding.addToUserListFragmentContainer.id,
                    CardSelectionFragment(),
                    CARD_SELECTION_FRAGMENT_TAG
                )
            }
        }

        initFragmentResultListeners()

    }

    private fun initFragmentResultListeners() {
        childFragmentManager.setFragmentResultListener(
            CardSelectionFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val cardId = bundle.getLong(CardSelectionFragment.CARD_ID_DATA_KEY)

            AddToUserListFormDialogFragment.newInstance(cardId, args.userList)
                .show(childFragmentManager, null)
        }

        childFragmentManager.setFragmentResultListener(
            AddToUserListFormDialogFragment.FRAGMENT_RESULT_REQUEST_KEY,
            viewLifecycleOwner
        ) { _, bundle ->
            val userListName =
                bundle.getString(AddToUserListFormDialogFragment.ADDED_USER_LIST_NAME_DATA_KEY)

            showSnackbar(getString(R.string.add_to_user_list_success_message, userListName))
        }
    }
}
