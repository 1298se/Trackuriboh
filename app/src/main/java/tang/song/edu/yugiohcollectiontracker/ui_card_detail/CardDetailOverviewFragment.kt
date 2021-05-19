package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import tang.song.edu.yugiohcollectiontracker.BaseFragment
import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.types.CardType
import tang.song.edu.yugiohcollectiontracker.databinding.CardDetailOverviewRowBinding
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
            val viewMap: Map<Int, String?> = when (card.type) {
                CardType.UNKNOWN -> emptyMap()
                CardType.SPELL_CARD, CardType.TRAP_CARD -> mapOf(
                    R.string.lbl_name to card.name,
                    R.string.lbl_type to card.type.value,
                    R.string.lbl_race to card.race,
                    R.string.lbl_description to card.desc
                )
                CardType.EFFECT_MONSTER,
                CardType.FLIP_EFFECT_MONSTER,
                CardType.FLIP_EFFECT_TUNER_MONSTER,
                CardType.GEMINI_MONSTER,
                CardType.NORMAL_MONSTER,
                CardType.NORMAL_TUNER_MONSTER,
                CardType.PENDULUM_EFFECT_MONSTER,
                CardType.PENDULUM_FLIP_EFFECT_MONSTER,
                CardType.PENDULUM_NORMAL_MONSTER,
                CardType.PENDULUM_TUNER_EFFECT_MONSTER,
                CardType.RITUAL_EFFECT_MONSTER,
                CardType.RITUAL_MONSTER,
                CardType.SKILL_CARD,
                CardType.SPIRIT_MONSTER,
                CardType.TOKEN,
                CardType.TOON_MONSTER,
                CardType.TUNER_MONSTER,
                CardType.UNION_EFFECT_MONSTER,
                CardType.FUSION_MONSTER,
                CardType.LINK_MONSTER,
                CardType.PENDULUM_EFFECT_FUSION_MONSTER,
                CardType.SYNCHRO_MONSTER,
                CardType.SYNCHRO_PENDULUM_EFFECT_MONSTER,
                CardType.SYNCHRO_TUNER_MONSTER,
                CardType.XYZ_MONSTER,
                CardType.XYZ_PENDULUM_EFFECT_MONSTER -> mapOf(
                    R.string.lbl_name to card.name,
                    R.string.lbl_type to card.type.value,
                    R.string.lbl_levelrank to card.level?.toString(),
                    R.string.lbl_race to card.race,
                    R.string.lbl_attribute to card.attribute,
                    R.string.lbl_atkdef to getString(R.string.card_detail_atk_def, card.atk, card.def),
                    R.string.lbl_description to card.desc
                )
            }

            for ((titleResId, content) in viewMap) {
                CardDetailOverviewRowBinding.inflate(layoutInflater, binding.cardDetailOverviewContainer, true).apply {
                    cardDetailOverviewRowTitleTextview.text = getString(titleResId)
                    cardDetailOverviewRowDescriptionTextview.text = content
                }
            }
        }
    }
}
