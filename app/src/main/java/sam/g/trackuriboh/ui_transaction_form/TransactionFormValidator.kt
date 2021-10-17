package sam.g.trackuriboh.ui_transaction_form

import sam.g.trackuriboh.data.types.StringResourceEnum

interface TransactionFormValidator {
    val isValid: Boolean
    fun validateCardNumber(text: String?)
    fun validateRarity(text: String?)
    fun validateEdition(type: StringResourceEnum?)
    fun validatePrice(text: String?)
    fun validateQuantity(text: String?)
    fun validateTransaction(type: StringResourceEnum?)
    fun validatePlatform(type: StringResourceEnum?)
    fun validateCondition(type: StringResourceEnum?)
}