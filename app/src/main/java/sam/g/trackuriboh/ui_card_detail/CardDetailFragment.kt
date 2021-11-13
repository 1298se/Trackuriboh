package sam.g.trackuriboh.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.FragmentCardDetailBinding
import sam.g.trackuriboh.setViewPagerBackPressBehaviour
import sam.g.trackuriboh.ui_card_detail.adapters.CardDetailPagerAdapter
import sam.g.trackuriboh.ui_card_detail.viewmodels.CardDetailViewModel
import sam.g.trackuriboh.ui_common.CollapseToolbarStateChangeListener
import sam.g.trackuriboh.ui_database.adapters.ImagePagerAdapter
import sam.g.trackuriboh.viewBinding

@AndroidEntryPoint
class CardDetailFragment : Fragment(), Toolbar.OnMenuItemClickListener {
    private val binding by viewBinding(FragmentCardDetailBinding::inflate)

    private val mViewModel: CardDetailViewModel by viewModels()

    private lateinit var mImagePagerAdapter: ImagePagerAdapter

    private lateinit var mCardDetailPagerAdapter: CardDetailPagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.cardDetailViewPager)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()

        mViewModel.cardWithSetAndSkuIds.observe(viewLifecycleOwner) { productWithSkus ->
            mImagePagerAdapter.setImageList(listOf(productWithSkus?.product?.imageUrl))
            mCardDetailPagerAdapter.setSkuIds(productWithSkus?.skuIds ?: emptyList())
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return true
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

        binding.cardDetailCollapsingToolbarLayout.setupWithNavController(
            binding.cardDetailToolbar,
            findNavController(),
            AppBarConfiguration.Builder().setFallbackOnNavigateUpListener {
                activity?.finish()
                true
            }.build()
        )
    }

    private fun initImageViewPager() {
        binding.cardImageViewPager.adapter = ImagePagerAdapter().also { mImagePagerAdapter = it }

        TabLayoutMediator(binding.cardImageTabLayout, binding.cardImageViewPager) { _, _ -> }.attach()
    }

    private fun initCardDetailViewPager() {
        binding.cardDetailViewPager.adapter = CardDetailPagerAdapter(this@CardDetailFragment).also {
            mCardDetailPagerAdapter = it
        }

        TabLayoutMediator(binding.cardDetailTabLayout, binding.cardDetailViewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Prices"
                else -> null
            }
        }.attach()
    }
}
