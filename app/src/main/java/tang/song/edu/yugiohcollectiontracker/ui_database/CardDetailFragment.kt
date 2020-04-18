package tang.song.edu.yugiohcollectiontracker.ui_database

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import tang.song.edu.yugiohcollectiontracker.BaseApplication
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailBinding
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardDetailViewModel
import tang.song.edu.yugiohcollectiontracker.ui_database.viewmodels.CardDetailViewModelFactory
import javax.inject.Inject

class CardDetailFragment : BaseFragment() {
    @Inject
    lateinit var mRequestManager: RequestManager

    @Inject
    lateinit var mViewModelFactory: CardDetailViewModelFactory

    private val args: CardDetailFragmentArgs by navArgs()

    private var _binding: FragmentCardDetailBinding? = null
    private val binding
        get() = _binding!!

    private lateinit var mViewModel: CardDetailViewModel

    private lateinit var listDataHeader: List<String>
    private lateinit var listDataChild: HashMap<String, List<String>>


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

        prepareListData()
        binding.cardSetExpandList.apply {
            setAdapter(CardDetailExpandableListAdapter(listDataHeader, listDataChild))
        }

        mViewModel = ViewModelProvider(this, mViewModelFactory).get(CardDetailViewModel::class.java)

        mViewModel.getCardById(args.cardId).observe(viewLifecycleOwner, Observer {
            mRequestManager.load(it.cardImage).into(binding.cardImage)
        })
    }

    private fun prepareListData() {
        listDataHeader = mutableListOf("Top 250", "Now Showing", "Coming Soon")
        listDataChild = HashMap()

        // Adding child data
        val top250 = ArrayList<String>()
        top250.add("The Shawshank Redemption")
        top250.add("The Godfather")
        top250.add("The Godfather: Part II")
        top250.add("Pulp Fiction")
        top250.add("The Good, the Bad and the Ugly")
        top250.add("The Dark Knight")
        top250.add("12 Angry Men")

        val nowShowing = ArrayList<String>()
        nowShowing.add("The Conjuring")
        nowShowing.add("Despicable Me 2")
        nowShowing.add("Turbo")
        nowShowing.add("Grown Ups 2")
        nowShowing.add("Red 2")
        nowShowing.add("The Wolverine")

        val comingSoon = ArrayList<String>()
        comingSoon.add("2 Guns")
        comingSoon.add("The Smurfs 2")
        comingSoon.add("The Spectacular Now")
        comingSoon.add("The Canyons")
        comingSoon.add("Europa Report")

        listDataChild[listDataHeader[0]] = top250 // Header, Child data
        listDataChild[listDataHeader[1]] = nowShowing
        listDataChild[listDataHeader[2]] = comingSoon
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
