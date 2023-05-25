package sam.g.trackuriboh.ui.transaction.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.entities.TransactionType
import sam.g.trackuriboh.data.db.relations.ProductWithCardSetAndSkuIds
import sam.g.trackuriboh.data.db.relations.SkuWithMetadata
import sam.g.trackuriboh.data.repository.InventoryRepository
import sam.g.trackuriboh.data.repository.InventoryTransactionRepository
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.ProductRepository
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class AddTransactionFormViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val inventoryRepository: InventoryRepository,
    private val inventoryTransactionRepository: InventoryTransactionRepository,
    private val priceRepository: PriceRepository,
    state: SavedStateHandle
) : ViewModel() {

    val inventoryId = state.get<Long>("inventoryId")!!

    data class AddTransactionFormState(
        val formData: AddTransactionFormData = AddTransactionFormData(),
        val canSave: Boolean = false,
    )

    data class AddTransactionFormData(
        val type: TransactionType = TransactionType.PURCHASE,
        val date: Date = Date(),
        val productWithCardSetAndSkuIds: ProductWithCardSetAndSkuIds? = null,
        val skuWithMetadata: SkuWithMetadata? = null,
        val price: String? = null,
        val quantity: Int = 1,
    )

    private val _formState = MutableLiveData(
        AddTransactionFormState(formData = AddTransactionFormData())
    )

    init {
        viewModelScope.launch {
            inventoryRepository.getInventoryWithSkuMetadata(inventoryId)?.skuWithMetadata?.let {
                val productWithCardSetAndSkuIds =
                    productRepository.getProductWithSkusById(it.productWithCardSet.product.id)

                _formState.value = AddTransactionFormState(
                    AddTransactionFormData(
                        productWithCardSetAndSkuIds = productWithCardSetAndSkuIds,
                        skuWithMetadata = it
                    )
                )
            }
        }
    }

    private fun validate(formDataState: AddTransactionFormData): Boolean {
        with(formDataState) {
            return productWithCardSetAndSkuIds != null && skuWithMetadata != null && price != null && price.toDoubleOrNull() != null
        }
    }

    val formState = Transformations.map(_formState) {
        if (validate(it.formData)) {
            it.copy(canSave = true)
        } else {
            it.copy(canSave = false)
        }
    }

    fun setProduct(productId: Long) {
        viewModelScope.launch {
            val productWithCardSetAndSkuIds = productRepository.getProductWithSkusById(productId)

            _formState.value = _formState.value?.let {
                it.copy(
                    formData = it.formData.copy(
                        productWithCardSetAndSkuIds = productWithCardSetAndSkuIds.copy(
                            skusWithMetadata = productWithCardSetAndSkuIds.getOrderedSkusByPrintingAndCondition()
                        ),
                        skuWithMetadata = null,
                    )
                )
            }
        }
    }

    fun setSku(skuWithMetadata: SkuWithMetadata?) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(skuWithMetadata = skuWithMetadata))
        }
    }

    fun setTransactionType(type: TransactionType) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(type = type))
        }
    }

    fun setDate(date: Date) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(date = date))
        }
    }

    fun setPrice(price: String) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(price = price))
        }
    }

    fun setQuantity(quantity: Int) {
        _formState.value = _formState.value?.let {
            it.copy(formData = it.formData.copy(quantity = quantity.coerceIn(1, 99)))
        }
    }

    suspend fun addTransaction() {
        val type = formState.value?.formData?.type
        val skuId = formState.value?.formData?.skuWithMetadata?.sku?.id
        val date = formState.value?.formData?.date
        val price = formState.value?.formData?.price?.toDouble()
        val quantity = formState.value?.formData?.quantity

        if (type != null && date != null && price != null && quantity != null && skuId != null) {
            inventoryTransactionRepository.insertTransaction(
                type,
                date,
                skuId,
                price,
                quantity,
            )

            priceRepository.updatePricesForSkus(listOf(skuId))
        }
    }
}
