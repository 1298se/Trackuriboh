package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_base.CollapseToolbarStateChangeListener
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.adapters.CardDetailPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardImagePagerAdapter
import tang.song.edu.yugiohcollectiontracker.viewBinding
import javax.inject.Inject

class CardDetailFragment : BaseFragment(R.layout.fragment_card_detail) {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardDetailViewModelFactory

    private val args: CardDetailFragmentArgs by navArgs()

    private var _binding: FragmentCardDetailBinding? = null
    private val binding by viewBinding(FragmentCardDetailBinding::bind)

    private lateinit var mViewModel: CardDetailViewModel
    private lateinit var mImagePagerAdapter: CardImagePagerAdapter
    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardDetailViewModel::class.java)

        mViewModel.getCardDetailsById(args.cardId).observe(viewLifecycleOwner) {
            mImagePagerAdapter.setImageList(it.card.cardImageList)
            mCardDetailPagerAdapter.setCard(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initToolbar() {
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
