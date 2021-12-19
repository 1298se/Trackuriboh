package sam.g.trackuriboh.ui_card_detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.databinding.FragmentCardDetailOverviewBinding
import sam.g.trackuriboh.ui_common.TwoLineAttributeCardView
import sam.g.trackuriboh.utils.viewBinding

private const val ARG_CARD = "ARG_CARD"

class CardDetailOverviewFragment : Fragment() {
    private val binding by viewBinding(FragmentCardDetailOverviewBinding::inflate)

    private var mProduct: ProductWithCardSetAndSkuIds? = null

    companion object {
        fun newInstance(product: ProductWithCardSetAndSkuIds?) =
            CardDetailOverviewFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CARD, product)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mProduct = it.getParcelable(ARG_CARD)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        generateDescriptionView(mProduct)
    }

    private fun generateDescriptionView(productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds?) {
        productWithCardSetAndSkuIds?.let {
            val attributeMap = mapOf<String?, String?>(
                getString(R.string.lbl_name) to it.product.name,
                getString(R.string.lbl_number) to it.product.number,
                getString(R.string.lbl_set) to it.cardSet.name,
                getString(R.string.lbl_rarity) to it.product.rarity,
                getString(R.string.lbl_attribute) to it.product.attribute,
                getString(R.string.lbl_type) to it.product.cardType,
                getString(R.string.lbl_atkdef) to getString(R.string.card_detail_atk_def, it.product.attack ?: 0, it.product.defense ?: 0),
                getString(R.string.lbl_description) to it.product.description
            )

            binding.cardDetailContainer.addView(TwoLineAttributeCardView(context).apply {
                layoutParams = ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                setRowItems(attributeMap)
            })
        }
    }
}
