package sam.g.trackuriboh.ui_card_detail

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
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.CardWithSetInfo
import sam.g.trackuriboh.databinding.FragmentCardDetailBinding
import sam.g.trackuriboh.ui_card_detail.adapters.CardDetailPagerAdapter
import sam.g.trackuriboh.ui_common.CollapseToolbarStateChangeListener
import sam.g.trackuriboh.ui_database.adapters.CardImagePagerAdapter
import sam.g.trackuriboh.viewBinding
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

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewPagerBackPressBehaviour(binding.cardDetailViewPager)

        initToolbar()
        initImageViewPager()
        initCardDetailViewPager()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_add_to_inventory -> {
                val action = CardDetailFragmentDirections.actionCardDetailFragmentToTransactionDialogFragment(mCardWithSetInfo?.product?.id ?: -1)
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
