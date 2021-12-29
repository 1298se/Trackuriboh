package sam.g.trackuriboh.ui.user_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.BottomSheetUserListSelectionBinding
import sam.g.trackuriboh.ui.user_list.adapters.UserListsAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.viewBinding

/**
 * Displays a list of watchlists for user selection
 */
@AndroidEntryPoint
class UserListSelectionBottomSheetDialogFragment : BottomSheetDialogFragment(), UserListsAdapter.OnItemClickListener {

    private val binding: BottomSheetUserListSelectionBinding by viewBinding(BottomSheetUserListSelectionBinding::inflate)

    private val viewModel: UserListsViewModel by viewModels()

    private lateinit var userListsAdapter: UserListsAdapter

    companion object {
        const val FRAGMENT_RESULT_REQUEST_KEY = "UserListSelectionFragment_fragmentResultRequestKey"
        const val SELECTED_USER_LIST_DATA_KEY = "UserListSelectionFragment_selectedUserList"
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
        initRecyclerView()

        viewModel.userLists.observe(viewLifecycleOwner) {
            userListsAdapter.submitList(it)
        }
    }

    override fun onUserListItemClick(list: UserList) {
        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(SELECTED_USER_LIST_DATA_KEY to list))

        findNavController().popBackStack()
    }

    private fun initToolbar() {
        binding.userListSelectionToolbar.setTitle(R.string.add_to_user_list_action_title)
    }

    private fun initRecyclerView() {
        binding.userListSelectionList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = UserListsAdapter().apply {
                setOnItemClickListener(this@UserListSelectionBottomSheetDialogFragment)
                userListsAdapter = this
            }
        }
    }
}