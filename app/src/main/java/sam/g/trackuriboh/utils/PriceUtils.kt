package sam.g.trackuriboh.utils

import android.content.Context
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.repository.PriceRepository
import java.util.concurrent.TimeUnit

fun getListingAndShippingPriceString(
    context: Context,
    listingPrice: Double?,
    shippingPrice: Double?,
) = if (listingPrice != null) {
    if (shippingPrice != null && shippingPrice > 0.0) {
        context.resources.getString(R.string.base_price_with_shipping, listingPrice, shippingPrice)
    } else {
        context.resources.getString(R.string.base_price_with_free_shipping, listingPrice)
    }
} else {
    context.resources.getString(R.string.lbl_not_available)
}

fun Double.formattedPriceString() = String.format("%.2f", this)

suspend fun refreshPricesIfNecessary(
    skuIds: List<Long>,
    updateTimer: UpdateTimer,
    priceRepository: PriceRepository,
) {
    if (updateTimer.hasTimeElapsed(TimeUnit.MINUTES.toMillis(5))) {
        priceRepository.updatePricesForSkus(skuIds)

        updateTimer.refreshLastUpdateTime()
    }
}
