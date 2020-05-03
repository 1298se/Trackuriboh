package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_base.CollapseToolbarStateChangeListener
import tang.song.edu.yugiohcollectiontracker.ui_card_detail.adapters.CardDetailPagerAdapter
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardImagePagerAdapter
import javax.inject.Inject

class CardDetailFragment : BaseFragment(), View.OnClickListener {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardDetailViewModelFactory

    private val args by navArgs<CardDetailActivityArgs>()

    private var _binding: FragmentCardDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardDetailViewModel
    private lateinit var mImagePagerAdapter: CardImagePagerAdapter
    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity?.application as BaseApplication).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCardDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardDetailViewModel::class.java)

        mViewModel.getCardDetailsById(args.cardId).observe(viewLifecycleOwner) {
            mImagePagerAdapter.setImageList(it.card.cardImageList)
            mCardDetailPagerAdapter.setCard(it.card)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    // Back button
    override fun onClick(view: View?) {
        activity?.onBackPressed()
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

        binding.cardDetailToolbar.apply {
            setNavigationOnClickListener(this@CardDetailFragment)
        }
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
