package sam.g.trackuriboh.ui_card_set_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.databinding.FragmentCardSetDetailBinding
import sam.g.trackuriboh.ui_card_set_detail.viewmodels.CardSetDetailViewModel
import sam.g.trackuriboh.ui_database.adapters.CardListAdapter
import sam.g.trackuriboh.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardSetDetailFragment : BaseFragment(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mAdapter: CardListAdapter

    private val args: CardSetDetailFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentCardSetDetailBinding::inflate)
    private val mViewModel: CardSetDetailViewModel by viewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()

       binding.cardSetDetailCollapseToolbar.title = args.setName

        lifecycleScope.launch {
        }
    }

    override fun onItemClick(cardId: Long) {
        val action = CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    private fun initToolbar() {
        binding.cardSetDetailCollapseToolbar.setupWithNavController(binding.cardSetDetailToolbar, findNavController())
    }

    private fun initRecyclerView() {
        mAdapter.setOnItemClickListener(this)

        binding.cardList.apply {
            this.layoutManager = LinearLayoutManager(context)
            this.adapter = mAdapter
        }
    }
}
