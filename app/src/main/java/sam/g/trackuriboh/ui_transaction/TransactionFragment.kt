package sam.g.trackuriboh.ui_transaction

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentTransactionBinding
import sam.g.trackuriboh.viewBinding

@AndroidEntryPoint
class TransactionFragment : Fragment() {
    private val binding by viewBinding(FragmentTransactionBinding::inflate)
    private val mViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.apply {
            fragment = this@TransactionFragment
        }

        mViewModel.authenticationState.observe(viewLifecycleOwner) {
            print(it)
        }


    }
}
