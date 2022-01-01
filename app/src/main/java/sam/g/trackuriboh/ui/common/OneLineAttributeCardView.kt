package sam.g.trackuriboh.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.card.MaterialCardView
import sam.g.trackuriboh.R
import sam.g.trackuriboh.databinding.OneLineAttributeCardViewBinding
import sam.g.trackuriboh.databinding.OneLineAttributeRowBinding

class OneLineAttributeCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.style.AppTheme
) : MaterialCardView(context, attrs, defStyle) {

    private val binding = OneLineAttributeCardViewBinding.inflate(LayoutInflater.from(context), this, true)

    fun setHeader(title: String?, value: String?) {
        if (title != null) {
            binding.oneLineAttributeHeaderRow.visibility = View.VISIBLE
            binding.oneLineAttributeHeaderTitleTextview.text = title
            binding.oneLineAttributeHeaderValueTextview.text = value
        } else {
            binding.oneLineAttributeHeaderRow.visibility = View.GONE
        }
    }

    fun setRowItems(attributeMap: Map<String?, String?>) {
        for ((title, content) in attributeMap) {

            val rowBinding = OneLineAttributeRowBinding.inflate(
                LayoutInflater.from(context),
                binding.attributeContentContainer,
                false
            )

            rowBinding.apply {
                oneLineAttributeTitleTextview.text = title
                oneLineAttributeValueTextview.text = content
            }

            binding.attributeContentContainer.addView(rowBinding.root)
        }
    }
}