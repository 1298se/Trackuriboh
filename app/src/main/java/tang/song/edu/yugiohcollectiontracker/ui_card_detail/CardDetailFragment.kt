package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_base.CollapseToolbarStateChangeListener
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.adapters.CardDetailPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardImagePagerAdapter
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

@AndroidEntryPoint
class CardDetailFragment : BaseFragment(), Toolbar.OnMenuItemClickListener {
    @Inject
    lateinit var mImagePagerAdapter: CardImagePagerAdapter

    private val args: CardDetailFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val mViewModel: CardDetailViewModel by viewModels()
    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    private var mCardWithSetInfo: CardWithSetInfo? = null

    init {
        lifecycleScope.launchWhenStarted {
            mCardWithSetInfo = mViewModel.getCardDetailsById(args.cardId).also {

                mImagePagerAdapter.setImageList(it?.card?.cardImageURLList)
                mCardDetailPagerAdapter.setCard(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (binding.cardDetailViewPager.currentItem == 0) {
                if (!findNavController().popBackStack()) {
                    activity?.onBackPressed()
                }
            } else {
                binding.cardDetailViewPager.currentItem = binding.cardDetailViewPager.currentItem - 1
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add_to_inventory -> {
                val action = CardDetailFragmentDirections.actionCardDetailFragmentToTransactionDialogFragment(mCardWithSetInfo?.card?.cardId ?: -1)
                findNavController().navigate(action)
                true
            }
            else -> false
        }
    }

    private fun initToolbar() {
        binding.cardDetailToolbar.apply {
            inflateMenu(R.menu.card_detail_toolbar_menu)

            setOnMenuItemClickListener(this@CardDetailFragment)
        }

        binding.cardDetailAppBarLayout.addOnOffsetChangedListener(object : CollapseToolbarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State) {
                if (state == State.COLLAPSED) {
                    binding.cardDetailToolbar.title = getString(R.string.lbl_card_details)
                } else {
                    binding.cardDetailToolbar.title = null
                }
            }
        })

        binding.cardDetailCollapsingToolbarLayout.setupWithNavController(binding.cardDetailToolbar, findNavController())
    }

    private fun initImageViewPager() {
        binding.cardImageViewPager.adapter = mImagePagerAdapter

        TabLayoutMediator(binding.cardImageTabLayout, binding.cardImageViewPager) { _, _ -> }.attach()
    }

    private fun initCardDetailViewPager() {
        binding.cardDetailViewPager.apply {
            adapter = CardDetailPagerAdapter(this@CardDetailFragment).also {
                mCardDetailPagerAdapter = it
                it.setCard(mCardWithSetInfo)
            }
        }

        TabLayoutMediator(binding.cardDetailTabLayout, binding.cardDetailViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.lbl_overview)
                1 -> getString(R.string.lbl_set_details)
                else -> null
            }
        }.attach()
    }
}
