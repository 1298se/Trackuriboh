package sam.g.trackuriboh.ui.user_list

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.BottomSheetUserListSelectionBinding
import sam.g.trackuriboh.databinding.ItemSimpleOneLineBinding
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListsViewModel
import sam.g.trackuriboh.utils.viewBinding

/**
 * Displays a list of watchlists for user selection
 */
@AndroidEntryPoint
class UserListSelectionBottomSheetFragment : BottomSheetDialogFragment() {

    private val binding: BottomSheetUserListSelectionBinding by viewBinding(BottomSheetUserListSelectionBinding::inflate)

    private val viewModel: UserListsViewModel by viewModels()

    private lateinit var userListAdapter: UserListAdapter

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
        // We can use viewLifecycleOwner here because we use onCreateView
        viewModel.userLists.observe(viewLifecycleOwner) { list ->
            val userLists = list.map { it.userList }

            initListView(userLists)
        }
    }

    private fun initToolbar() {

        with(binding.userListSelectionToolbar) {
            setTitle(R.string.user_list_selection_title)
        }
    }

    private fun initListView(userLists: List<UserList>) {
        binding.userListSelectionList.adapter = UserListAdapter(requireContext(), userLists).apply {
            userListAdapter = this
        }
    }

    inner class UserListAdapter(
        context: Context,
        userLists: List<UserList>,
    ) : ArrayAdapter<UserList>(context, 0, userLists) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var curView = convertView

            if (curView == null) {
                val binding = ItemSimpleOneLineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                val userList = getItem(position)

                with(binding) {
                    binding.simpleOneLineText.text = userList?.name

                    root.setOnClickListener {
                        setFragmentResult(FRAGMENT_RESULT_REQUEST_KEY, bundleOf(SELECTED_USER_LIST_DATA_KEY to userList))

                        dismiss()
                    }
                }

                curView = binding.root
            }

            return curView
        }
    }
}