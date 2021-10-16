package tang.song.edu.trackuriboh.ui_transaction_form.adapters

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputLayout
import tang.song.edu.trackuriboh.R
import tang.song.edu.trackuriboh.data.types.StringResourceEnum
import tang.song.edu.trackuriboh.data.types.TransactionType
import tang.song.edu.trackuriboh.ui_transaction_form.TransactionFormValidator
import tang.song.edu.trackuriboh.ui_transaction_form.views.EnumAutoCompleteTextView


@BindingAdapter("entries")
fun bindAdapter(view: EnumAutoCompleteTextView, entries: List<StringResourceEnum>) {
    view.setAdapter(EnumArrayAdapter(view.context, entries))
}

@BindingAdapter("selectedItemAttrChanged")
fun selectedItemAttrChanged(view: EnumAutoCompleteTextView, listener: InverseBindingListener?) {
    view.setOnItemClickListener { parent, _, position, _ ->
        view.selectedItem = parent.adapter.getItem(position) as StringResourceEnum
        listener?.onChange()
    }
}

@BindingAdapter("selectedItem")
fun setSelectedItem(view: EnumAutoCompleteTextView, value: StringResourceEnum?) {
    view.selectedItem = value?.also {
        view.setText(it.getResourceId())
    }
}

@InverseBindingAdapter(attribute = "selectedItem")
fun getSelectedItem(view: EnumAutoCompleteTextView): StringResourceEnum? = view.selectedItem

@BindingAdapter("partyHint")
fun setPartyHint(view: TextInputLayout, hint: StringResourceEnum?) {
    view.hint = if (hint == null) {
        ""
    } else {
        if (hint.value == TransactionType.PURCHASE.value) {
            view.context.getString(R.string.lbl_seller)
        } else {
            view.context.getString(R.string.lbl_buyer)
        }
    }
}

/** Data Binding does not work well with lambdas, so we do it this way */
@BindingAdapter("validator")
fun bindValidator(textView: TextView, validator: TransactionFormValidator?) {
    validate(textView, validator)

    textView.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { return }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            validate(textView, validator)
        }

        override fun afterTextChanged(editable: Editable?) { return }
    })
}

private fun validate(textView: TextView, validator: TransactionFormValidator?) {
    val editable = textView.text

    when (textView.id) {
        R.id.new_transaction_cardNumber_textview -> validator?.validateCardNumber(editable?.toString())
        R.id.new_transaction_rarity_textview -> validator?.validateRarity(editable?.toString())
        R.id.new_transaction_edition_textview -> validator?.validateEdition((textView as EnumAutoCompleteTextView).selectedItem)
        R.id.new_transaction_price_edittext -> validator?.validatePrice(editable?.toString())
        R.id.new_transaction_quantity_edittext -> validator?.validateQuantity(editable?.toString())
        R.id.new_transaction_type_textview -> validator?.validateTransaction((textView as EnumAutoCompleteTextView).selectedItem)
        R.id.new_transaction_platform_textview -> validator?.validatePlatform((textView as EnumAutoCompleteTextView).selectedItem)
        R.id.new_transaction_condition_textview -> validator?.validateCondition((textView as EnumAutoCompleteTextView).selectedItem)
    }

}
