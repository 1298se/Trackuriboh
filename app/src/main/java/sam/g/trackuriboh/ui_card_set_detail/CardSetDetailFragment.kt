package sam.g.trackuriboh.ui_card_set_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.databinding.FragmentCardSetDetailBinding
import sam.g.trackuriboh.ui_card_set_detail.viewmodels.CardSetDetailViewModel
import sam.g.trackuriboh.ui_database.adapters.CardListAdapter
import sam.g.trackuriboh.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardSetDetailFragment : Fragment(), CardListAdapter.OnItemClickListener {
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
    }

    override fun onCardItemClick(cardId: Long) {
        // val action = CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardDetailFragment(cardId)
        // findNavController().navigate(action)
    }

    override fun onViewPricesItemClick(skuIds: List<Long>) {
        // TODO("Not yet implemented")
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
