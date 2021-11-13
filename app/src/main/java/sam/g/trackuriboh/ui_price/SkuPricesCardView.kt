package sam.g.trackuriboh.ui_price

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.google.android.material.card.MaterialCardView
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.databinding.SkuPricesCardViewBinding
import sam.g.trackuriboh.databinding.SkuPricesRowBinding
import sam.g.trackuriboh.setTextOrHideIfNull

/**
 * TODO: document your custom view class.
 */
class SkuPricesCardView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : MaterialCardView(context, attrs, defStyle) {

    private val binding = SkuPricesCardViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setHeader(header: String?) {
        binding.skuPricesHeaderText.setTextOrHideIfNull(header)
    }

    fun setRowItems(skuList: List<SkuWithConditionAndPrinting>) {
        skuList.forEach {
            val rowBinding = SkuPricesRowBinding.inflate(
                LayoutInflater.from(context),
                binding.skuPricesContainer,
                false
            )

            rowBinding.apply {
                skuPricesRowConditionText.text = it.condition?.name
                skuPricesRowPriceText.text = it.sku.lowestListingPrice?.toString()
            }

            binding.skuPricesContainer.addView(rowBinding.root)
        }

    }
}