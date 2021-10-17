package sam.g.trackuriboh.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sam.g.trackuriboh.BaseFragment
import sam.g.trackuriboh.data.db.entities.Card
import sam.g.trackuriboh.databinding.CardDetailOverviewRowBinding
import sam.g.trackuriboh.databinding.FragmentCardDetailOverviewBinding
import sam.g.trackuriboh.viewBinding

private const val ARG_CARD = "ARG_CARD"

class CardDetailOverviewFragment : BaseFragment() {
    private val binding by viewBinding(FragmentCardDetailOverviewBinding::inflate)

    private var mCard: Card? = null

    companion object {
        fun newInstance(card: Card?) =
            CardDetailOverviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CARD, card)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mCard = it.getParcelable(ARG_CARD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateDescriptionView(mCard)
    }

    private fun generateDescriptionView(card: Card?) {
        card?.let {
            for ((titleResId, content) in emptyMap<Int, String>()) {
                CardDetailOverviewRowBinding.inflate(layoutInflater, binding.cardDetailOverviewContainer, true).apply {
                    cardDetailOverviewRowTitleTextview.text = getString(titleResId)
                    cardDetailOverviewRowDescriptionTextview.text = content
                }
            }
        }
    }
}
