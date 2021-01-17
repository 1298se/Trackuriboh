package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
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
    lateinit var mRequestManager: RequestManager

    private val args: CardDetailFragmentArgs by navArgs()

    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val mViewModel: CardDetailViewModel by viewModels()
    private lateinit var mImagePagerAdapter: CardImagePagerAdapter
    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    private lateinit var mCard: CardWithSetInfo

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()

        lifecycleScope.launch {
            mCard = mViewModel.getCardDetailsById(args.cardId).also {
                mImagePagerAdapter.setImageList(it.card.cardImageURLList)
                mCardDetailPagerAdapter.setCard(it)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add_to_inventory -> {
                val action = CardDetailFragmentDirections.actionCardDetailFragmentToTransactionDialogFragment(mCard.card.cardId)
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
                    binding.cardDetailToolbar.title = "Card Details"
                } else {
                    binding.cardDetailToolbar.title = null
                }
            }
        })

        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.cardDetailCollapsingToolbarLayout.setupWithNavController(binding.cardDetailToolbar, navController, appBarConfiguration)
    }

    private fun initImageViewPager() {
        binding.cardImageViewPager.adapter = CardImagePagerAdapter(mRequestManager).also {
            mImagePagerAdapter = it
        }

        TabLayoutMediator(binding.cardImageTabLayout, binding.cardImageViewPager) { _, _ -> }.attach()
    }

    private fun initCardDetailViewPager() {
        binding.cardDetailViewPager.adapter = CardDetailPagerAdapter(this).also {
            mCardDetailPagerAdapter = it
        }

        TabLayoutMediator(binding.cardDetailTabLayout, binding.cardDetailViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Set Details"
                else -> null
            }
        }.attach()
    }
}
