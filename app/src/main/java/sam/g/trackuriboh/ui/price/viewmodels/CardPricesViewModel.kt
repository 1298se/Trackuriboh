package sam.g.trackuriboh.ui.price.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.relations.SkuWithConditionAndPrinting
import sam.g.trackuriboh.data.network.responses.Resource
import sam.g.trackuriboh.data.repository.PriceRepository
import sam.g.trackuriboh.ui.common.utils.UiState
import sam.g.trackuriboh.ui.price.CardPricesBottomSheetFragment.Companion.ARG_PRODUCT_ID
import javax.inject.Inject

@HiltViewModel
class CardPricesViewModel @Inject constructor(
    private val priceRepository: PriceRepository,
    savedState: SavedStateHandle,
    private val application: Application,
) : ViewModel() {
    private val productId = savedState.get<Long>(ARG_PRODUCT_ID)!!

    val state = liveData {
            emit(UiState.Loading())
            when (val resource = priceRepository.getPricesForProductSkusOrdered(productId)) {
                is Resource.Success -> emit(UiState.Success(buildPrintingToSkuMap(resource.data)))
                is Resource.Failure -> emit(
                    UiState.Failure(
                    message = application.getString(R.string.error_message_generic),
                    data = resource.data?.let { buildPrintingToSkuMap(it) }
                ))
                else -> return@liveData
            }
        }

    private fun buildPrintingToSkuMap(skus: List<SkuWithConditionAndPrinting>): Map<String?, List<SkuWithConditionAndPrinting>> {
        val map = mutableMapOf<String?, MutableList<SkuWithConditionAndPrinting>>()

        skus.run {
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
