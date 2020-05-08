package tang.song.edu.yugiohcollectiontracker.ui_card_detail

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import androidx.navigation.fragment.findNavController
import com.google.android.material.textview.MaterialTextView
import tang.song.edu.yugiohcollectiontracker.BaseFragment

import tang.song.edu.yugiohcollectiontracker.R
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardSetInfo
import tang.song.edu.yugiohcollectiontracker.databinding.FragmentCardDetailSetInfoBinding
import tang.song.edu.yugiohcollectiontracker.viewBinding

private const val ARG_CARD_SET_INFO = "ARG_CARD_SET_INFO"

class CardDetailSetInfoFragment : BaseFragment(R.layout.fragment_card_detail_set_info) {
    private var mCardSetInfoList: List<CardSetInfo>? = null

    private val binding by viewBinding(FragmentCardDetailSetInfoBinding::bind)

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initTableRows()
    }

    private fun initTableRows() {
        mCardSetInfoList?.forEach { cardSetInfo ->
            val setNameTextView = createTableTextView(SpannableString(cardSetInfo.setName).apply {
                setSpan(object : ClickableSpan() {
                    override fun onClick(p0: View) {
                        val action = CardDetailFragmentDirections.actionCardDetailFragmentToCardSetDetailFragment(cardSetInfo.setCode)
                        findNavController().navigate(action)
                    }
                }, 0, cardSetInfo.setName.length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
            }).apply {
                movementMethod = LinkMovementMethod.getInstance()
            }

            val setCodeTextView = createTableTextView(cardSetInfo.setCode)

            val rarityTextView = createTableTextView(cardSetInfo.rarity)

            val priceTextView = createTableTextView(cardSetInfo.price)

            val tableRow = TableRow(context).apply {
                addView(setNameTextView)
                addView(setCodeTextView)
                addView(rarityTextView)
                addView(priceTextView)
            }

            binding.cardDetailSetInfoTable.addView(tableRow)
        }
    }

    private fun createTableTextView(displayString: CharSequence?) = MaterialTextView(requireContext()).apply {
        text = displayString
        layoutParams = TableRow.LayoutParams(0, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f)
    }
}
