package sam.g.trackuriboh.ui.common

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.card.MaterialCardView
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.TwoLineAttributeCardViewBinding
import sam.g.trackuriboh.databinding.TwoLineAttributeRowBinding

class TwoLineAttributeCardView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : MaterialCardView(context, attrs, defStyle) {

    private val binding = TwoLineAttributeCardViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setHeader(title: String? = null, value: String? = null) {
        if (title != null) {
            binding.twoLineAttributeHeaderRow.visibility = View.VISIBLE
            binding.twoLineAttributeHeaderTitleTextview.text = title
            binding.twoLineAttributeHeaderValueTextview.text = value
        } else {
            binding.twoLineAttributeHeaderRow.visibility = View.GONE
        }
    }

    fun setRowItems(attributeMap: Map<CharSequence?, CharSequence?>) {
        for ((title, content) in attributeMap) {

            val rowBinding = TwoLineAttributeRowBinding.inflate(
                LayoutInflater.from(context),
                binding.attributeContentContainer,
                false
            )

            rowBinding.apply {
                twoLineAttributeHeaderTitleTextview.text = title
                twoLineAttributeHeaderValueTextview.text = content
            }

            binding.attributeContentContainer.addView(rowBinding.root)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
    }
}