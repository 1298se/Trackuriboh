package tang.song.edu.trackuriboh.ui_transaction_form

import tang.song.edu.trackuriboh.R
import tang.song.edu.trackuriboh.data.db.relations.CardWithSetInfo
import tang.song.edu.trackuriboh.data.types.*
import tang.song.edu.trackuriboh.databinding.BottomSheetTransactionBinding

class TransactionFormValidatorImpl(
    private val binding: BottomSheetTransactionBinding,
    private val cardWithSetInfo: CardWithSetInfo?,
) : TransactionFormValidator {
    override val isValid
        get() = binding.newTransactionCardNameLayout.error == null
                && binding.newTransactionCardNumberLayout.error == null
                && binding.newTransactionRarityLayout.error == null
                && binding.newTransactionEditionLayout.error == null
                && binding.newTransactionPriceLayout.error == null
                && binding.newTransactionQuantityLayout.error == null
                && binding.newTransactionTypeLayout.error == null
                && binding.newTransactionPlatformLayout.error == null
                && binding.newTransactionConditionLayout.error == null

    override fun validateCardNumber(text: String?) {
        binding.newTransactionCardNumberLayout.apply {
            error = when {
                cardWithSetInfo?.sets?.any { cardSetInfo -> cardSetInfo.cardNumber == text?.trim() } == true -> {
                    null
                }
                text.isNullOrBlank() -> {
                    context.getString(R.string.lbl_required)
                }
                else -> {
                    context.getString(R.string.lbl_invalid)
                }
            }
        }
    }

    override fun validateRarity(text: String?) {
        val raritySet = mutableSetOf<String?>()

        binding.newTransactionRarityLayout.apply {
            error = if (text.isNullOrBlank()) {
                context.getString(R.string.lbl_required)
            } else {
                cardWithSetInfo?.sets?.forEach { cardSetInfo ->
                    run {
                        if (cardSetInfo.cardNumber == binding.newTransactionCardNumberTextview.text.toString()) {
                            raritySet.add(cardSetInfo.rarity)
                        }
                    }
                }
                if (raritySet.contains(text.trim())) null else context.getString(R.string.lbl_invalid)
            }
        }
    }

    override fun validateEdition(type: StringResourceEnum?) {
        binding.newTransactionEditionLayout.apply {
            error = when (type) {
                null -> {
                    context.getString(R.string.lbl_required)
                }
                !is EditionType -> {
                    context.getString(R.string.lbl_invalid)
                }
                else -> {
                    null
                }
            }
        }
    }

    override fun validatePrice(text: String?) {
        binding.newTransactionPriceLayout.apply {
            error = if (text.isNullOrBlank()) {
                context.getString(R.string.lbl_required)
            } else {
                try {
                    text.toDouble()
                    null
                } catch (e: NumberFormatException) {
                    context.getString(R.string.lbl_invalid)
                }
            }
        }
    }

    override fun validateQuantity(text: String?) {
        binding.newTransactionQuantityLayout.apply {
            error = if (text.isNullOrBlank()) {
                context.getString(R.string.lbl_required)
            } else {
                try {
                    text.toInt()
                    null
                } catch (e: NumberFormatException) {
                    context.getString(R.string.lbl_invalid)
                }
            }
        }
    }

    override fun validateTransaction(type: StringResourceEnum?) {
        binding.newTransactionTypeLayout.apply {
            error = when (type) {
                null -> {
                    context.getString(R.string.lbl_required)
                }
                !is TransactionType -> {
                    context.getString(R.string.lbl_invalid)
                }
                else -> {
                    null
                }
            }
        }
    }

    override fun validatePlatform(type: StringResourceEnum?) {
        binding.newTransactionPlatformLayout.apply {
            error = when (type) {
                null -> {
                    context.getString(R.string.lbl_required)
                }
                !is PlatformType -> {
                    context.getString(R.string.lbl_invalid)
                }
                else -> {
                    null
                }
            }
        }
    }

    override fun validateCondition(type: StringResourceEnum?) {
        binding.newTransactionConditionLayout.apply {
            error = when (type) {
                null -> {
                    context.getString(R.string.lbl_required)
                }
                !is ConditionType -> {
                    context.getString(R.string.lbl_invalid)
                }
                else -> {
                    null
                }
            }
        }
    }
}
