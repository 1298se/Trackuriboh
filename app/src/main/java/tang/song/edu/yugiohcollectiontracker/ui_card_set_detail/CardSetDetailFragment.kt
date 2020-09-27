package tang.song.edu.yugiohcollectiontracker.ui_card_set_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardSetDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels.CardSetDetailViewModel
import tang.song.edu.yugiohcollectiontracker.ui_card_set_detail.viewmodels.CardSetDetailViewModelFactory
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardListAdapter
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

class CardSetDetailFragment : BaseFragment(), CardListAdapter.OnItemClickListener {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardSetDetailViewModelFactory

    private val args: CardSetDetailFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentCardSetDetailBinding::inflate)
    private lateinit var mViewModel: CardSetDetailViewModel

    private lateinit var mAdapter: CardListAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardSetDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initRecyclerView()

        mViewModel.getCardListBySet(args.setCode).observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }

        mViewModel.getCardSetByCode(args.setCode).observe(viewLifecycleOwner) {
            binding.cardSetDetailCollapseToolbar.title = it.setName
        }
    }

    override fun onItemClick(cardId: Long) {
        val action = CardSetDetailFragmentDirections.actionCardSetDetailFragmentToCardDetailFragment(cardId)
        findNavController().navigate(action)
    }

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.cardSetDetailCollapseToolbar.apply {
            setupWithNavController(binding.cardSetDetailToolbar, navController, appBarConfiguration)
        }
    }

    private fun initRecyclerView() {
        mAdapter = CardListAdapter(this, mRequestManager)
        val layoutManager = LinearLayoutManager(requireContext())

        binding.cardList.apply {
            this.layoutManager = layoutManager
            this.adapter = mAdapter
        }
    }
}
