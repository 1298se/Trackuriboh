package sam.g.trackuriboh.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.data.db.relations.CardSetInfo
import sam.g.trackuriboh.databinding.FragmentCardDetailSetInfoBinding
import sam.g.trackuriboh.databinding.ItemCardDetailSetInfoBinding
import sam.g.trackuriboh.viewBinding

private const val ARG_CARD_SET_INFO = "ARG_CARD_SET_INFO"

class CardDetailSetInfoFragment : BaseFragment() {
    private var mCardSetInfoList: List<CardSetInfo>? = null

    private val binding by viewBinding(FragmentCardDetailSetInfoBinding::inflate)

    companion object {
        fun newInstance(cardSetInfoList: List<CardSetInfo>?) =
            CardDetailSetInfoFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CARD_SET_INFO, ArrayList(cardSetInfoList ?: emptyList()))
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCardSetInfoList = it.getParcelableArrayList(ARG_CARD_SET_INFO)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTableRows()
    }

    private fun initTableRows() {
        mCardSetInfoList?.forEach { cardSetInfo ->
            ItemCardDetailSetInfoBinding.inflate(layoutInflater, binding.cardDetailSetInfoTable, true).apply {
                itemCardDetailSetInfoCardnumberTextview.text = cardSetInfo.cardNumber
                itemCardDetailSetInfoRarityTextview.text = cardSetInfo.rarity

                root.setOnClickListener {
                    val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardSetDetailFragment(cardSetInfo.setName)
                    findNavController().navigate(action)
                }
            }
        }
    }
}
