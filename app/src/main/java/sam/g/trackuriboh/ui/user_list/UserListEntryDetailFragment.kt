package sam.g.trackuriboh.ui.user_list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.UserList
import sam.g.trackuriboh.databinding.FragmentUserListDetailBinding
import sam.g.trackuriboh.ui.user_list.adapters.TransactionAdapter
import sam.g.trackuriboh.ui.user_list.adapters.UserListCardAdapter
import sam.g.trackuriboh.ui.user_list.adapters.UserTransactionAdapter
import sam.g.trackuriboh.ui.user_list.viewmodels.UserListDetailViewModel
import sam.g.trackuriboh.utils.viewBinding

class UserListEntryDetailFragment : Fragment() {
    private val args: UserListDetailFragmentArgs by navArgs()

    private lateinit var userList: UserList

    private val binding: FragmentUserListDetailBinding by viewBinding(FragmentUserListDetailBinding::inflate)

    private val viewModel: UserListEntryDetailViewModel by viewModels()

    private lateinit var userTransactionAda: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_list_entry_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UserListEntryDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}