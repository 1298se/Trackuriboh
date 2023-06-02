package sam.g.trackuriboh.ui.price.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.relations.SkuWithMetadata
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.data.repository.SkuRepository
import sam.g.trackuriboh.ui.price.CardPricesBottomSheetFragment.Companion.ARG_PRODUCT_ID
import javax.inject.Inject
import kotlin.collections.set

@HiltViewModel
class CardPricesViewModel @Inject constructor(
    skuRepository: SkuRepository,
    private val priceRepository: PriceRepository,
    savedState: SavedStateHandle,
) : ViewModel() {
    private val productId = savedState.get<Long>(ARG_PRODUCT_ID)!!

    init {
        viewModelScope.launch {
            priceRepository.updatePricesForSkus(skuRepository.getSkus(productId).map { it.id })
        }
    }

    val printingToSkuMap = skuRepository.getSkusWithMetadataObservable(productId).map {
        buildPrintingToSkuMap(SkuWithMetadata.getOrderedSkusByPrintingAndCondition(it))
    }.asLiveData()

    private fun buildPrintingToSkuMap(skus: List<SkuWithMetadata>): Map<String?, List<SkuWithMetadata>> {
        val map = mutableMapOf<String?, MutableList<SkuWithMetadata>>()

        // Add each sku to the corresponding printing
        skus.forEach {
            val skuList = map[it.printing?.name]

            if (skuList != null) {
                skuList.add(it)
            } else {
                map[it.printing?.name] = mutableListOf(it)
            }
        }

        return map
    }
}
