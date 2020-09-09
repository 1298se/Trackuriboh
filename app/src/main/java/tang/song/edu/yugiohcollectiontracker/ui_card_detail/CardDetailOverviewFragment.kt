package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.models.CardType
import tang.song.edu.yugiohcollectiontracker.databinding.CardDetailTypeMonsterBinding
import tang.song.edu.yugiohcollectiontracker.databinding.CardDetailTypeSpellTrapBinding
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailOverviewBinding
import tang.song.edu.yugiohcollectiontracker.viewBinding

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
            val descriptionView: View = when (card.type) {
                CardType.SPELL_CARD, CardType.TRAP_CARD -> {
                    val spellBinding = CardDetailTypeSpellTrapBinding.inflate(LayoutInflater.from(context), binding.cardDetailOverviewContainer, false).apply {
                        cardNameTextView.text = card.name
                        cardTypeTextView.text = card.type.value
                        cardRaceTextView.text = card.race
                        cardDescriptionTextView.text = card.desc
                    }

                    spellBinding.root
                }
                CardType.UNKNOWN -> View(context)
                else -> {
                    val monsterBinding = CardDetailTypeMonsterBinding.inflate(LayoutInflater.from(context), binding.cardDetailOverviewContainer, false).apply {
                        cardNameTextView.text = card.name
                        cardTypeTextView.text = card.type.value
                        cardLevelTextView.text = card.level?.toString()
                        cardRaceTextView.text = card.race
                        cardAttributeTextView.text = card.attribute
                        cardAtkDefTextView.text = getString(R.string.card_detail_atk_def, card.atk, card.def)
                        cardDescriptionTextView.text = card.desc
                    }

                    monsterBinding.root
                }
            }

            binding.cardDetailOverviewContainer.addView(descriptionView)
        }
    }
}
