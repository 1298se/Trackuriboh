package sam.g.trackuriboh.ui_price.viewmodels

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.ui_price.CardDetailPricesFragment
import javax.inject.Inject

@HiltViewModel
class CardPricesViewModel @Inject constructor(
    private val priceRepository: PriceRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val skuIds = state.get<LongArray>(CardDetailPricesFragment.ARG_SKU_IDS)?.toList() ?: emptyList()

    val printingToSkuMap: LiveData<Map<String?, List<SkuWithConditionAndPrinting>>>
        get() = _skuPrices

    private val _skuPrices: MutableLiveData<Map<String?, List<SkuWithConditionAndPrinting>>> = MutableLiveData()

    init {
        viewModelScope.launch {
            priceRepository.getPricesForSkus(skuIds).collect { result ->
                _skuPrices.value = buildPrintingToSkuMap(result)
            }
        }
    }

    private fun buildPrintingToSkuMap(skus: List<SkuWithConditionAndPrinting>): Map<String?, List<SkuWithConditionAndPrinting>>? {
        val map = mutableMapOf<String?, MutableList<SkuWithConditionAndPrinting>>()

        skus.run {
            // Sort by printing, then prices. If null, we set to MAX_VALUE so it's at the end
            sortedWith(compareBy({ it.printing?.order ?: Integer.MAX_VALUE }, { it.condition?.order ?: Integer.MAX_VALUE }))
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
