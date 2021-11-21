package sam.g.trackuriboh.ui_price.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.ui_common.UiState
import sam.g.trackuriboh.ui_price.CardPricesBottomSheetDialogFragment.Companion.ARG_SKU_IDS
import javax.inject.Inject

@HiltViewModel
class CardPricesViewModel @Inject constructor(
    private val priceRepository: PriceRepository,
    private val savedState: SavedStateHandle
) : ViewModel() {
    private val skuIds = savedState.getLiveData<LongArray>(ARG_SKU_IDS)

    val state: LiveData<UiState<Map<String?, List<SkuWithConditionAndPrinting>>>> = Transformations.switchMap(skuIds) {
        liveData {
            emit(UiState.Loading)
            when (val resource = priceRepository.getPricesForSkus(it.toList())) {
                is Resource.Success -> emit(UiState.Success(buildPrintingToSkuMap(resource.data)))
                is Resource.Failure -> emit(UiState.Failure(
                    resource.exception.message,
                    resource.data?.let { buildPrintingToSkuMap(it) }
                ))
            }
        }
    }

    private fun buildPrintingToSkuMap(skus: List<SkuWithConditionAndPrinting>): Map<String?, List<SkuWithConditionAndPrinting>> {
        // Sort by printing, then prices. If null, we set to MAX_VALUE so it's at the end

        val sortedList = skus.sortedWith(compareBy(
            { it.printing?.order ?: Integer.MAX_VALUE },
            { it.condition?.order ?: Integer.MAX_VALUE }
        ))

        val map = mutableMapOf<String?, MutableList<SkuWithConditionAndPrinting>>()

        sortedList.run {
            forEach {
                val skuList = map[it.printing?.name]

                if (skuList != null) {
                    skuList.add(it)
                } else {
                    map[it.printing?.name] = mutableListOf(it)
                }
            }
        }

        return map
    }
}
