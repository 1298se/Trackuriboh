package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.RequestManager
import com.google.android.material.tabs.TabLayoutMediator
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.adapters.CardImagePagerAdapter
import javax.inject.Inject

class CardDetailFragment : BaseFragment() {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardDetailViewModelFactory

    private var _binding: FragmentCardDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardDetailViewModel
    private lateinit var mAdapter: CardImagePagerAdapter

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
        initTabLayoutWithViewPager()

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardDetailViewModel::class.java)

        mViewModel.getCardById(9).observe(viewLifecycleOwner) {
            binding.cardDetailToolbar.title = it.name
            mAdapter.setImageList(it.cardImageList)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    private fun initToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.cardDetailToolbar.apply {
            setupWithNavController(navController, appBarConfiguration)
        }
    }

    private fun initTabLayoutWithViewPager() {
        binding.cardImageViewPager.apply {
            adapter = CardImagePagerAdapter(mRequestManager).also {
                mAdapter = it
            }

            TabLayoutMediator(binding.cardImageTabLayout, this) { _, _ ->
            }.attach()
        }
    }
}