package sam.g.trackuriboh.ui.inventory_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import sam.g.trackuriboh.R
import sam.g.trackuriboh.data.db.entities.*
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadata
import sam.g.trackuriboh.data.db.relations.InventoryWithSkuMetadataAndTransactions
import sam.g.trackuriboh.data.db.relations.ProductWithCardSet
import sam.g.trackuriboh.data.db.relations.SkuWithMetadata
import sam.g.trackuriboh.data.types.ProductType
import sam.g.trackuriboh.utils.formatDate
import sam.g.trackuriboh.utils.getListingAndShippingPriceString
import sam.g.trackuriboh.utils.joinStringsWithInterpunct
import java.util.*

@Composable
fun InventoryDetailScreen(
    inventoryWithSkuMetadataAndTransactions: InventoryWithSkuMetadataAndTransactions,
    modifier: Modifier = Modifier,
    onTransactionSwiped: (InventoryTransaction) -> Unit,
    onAddTransactionClick: () -> Unit,
    onSkuDetailClick: (productId: Long) -> Unit,
) {

    Column {
        SkuDetailInfoSection(
            skuWithMetadata = inventoryWithSkuMetadataAndTransactions.inventoryWithSkuMetadata.skuWithMetadata,
            onSkuDetailClick = onSkuDetailClick,
        )

        LazyColumn(
            modifier = modifier
                .background(MaterialTheme.colors.surface),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.material_border_padding)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.list_item_row_spacing))
        ) {
            item {
                PriceInfoSection(inventoryWithSkuMetadata = inventoryWithSkuMetadataAndTransactions.inventoryWithSkuMetadata)

                Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.heading_text_spacing)))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text = stringResource(id = R.string.lbl_transactions),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        style = MaterialTheme.typography.subtitle2,
                    )

                    IconButton(
                        onClick = onAddTransactionClick,
                        modifier = Modifier.align(Alignment.CenterVertically),
                    ) {
                        Icon(imageVector = Icons.Filled.Add, contentDescription = "")
                    }
                }

            }

            items(inventoryWithSkuMetadataAndTransactions.transactions) { transaction ->
                TransactionItemRow(transaction, onTransactionSwiped)
            }
        }
    }
}

@Composable
fun SkuDetailInfoSection(
    skuWithMetadata: SkuWithMetadata,
    onSkuDetailClick: (productId: Long) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable {
                onSkuDetailClick(skuWithMetadata.productWithCardSet.product.id)
            }
            .padding(dimensionResource(id = R.dimen.material_border_padding)),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(skuWithMetadata.productWithCardSet.product.imageUrl)
                .fallback(R.drawable.img_cardback)
                .build(),
            contentDescription = "",
            modifier = Modifier
                .fillMaxWidth(0.25f)
        )

        Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.list_item_image_content_spacing)))

        Column {
            Text(
                text = skuWithMetadata.productWithCardSet.product.name,
                style = MaterialTheme.typography.h6
            )

            Text(
                text = joinStringsWithInterpunct(
                    skuWithMetadata.productWithCardSet.cardSet.name ?: "",
                    skuWithMetadata.productWithCardSet.product.number
                ),
                style = MaterialTheme.typography.body1
            )

            Text(
                text = joinStringsWithInterpunct(
                    skuWithMetadata.productWithCardSet.rarity.name,
                    skuWithMetadata.printing?.name,
                    skuWithMetadata.condition?.name
                ),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun PriceInfoSection(inventoryWithSkuMetadata: InventoryWithSkuMetadata) {
    Column(
        Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.text_spacing_large))
    ) {
        val skuWithMetadata = inventoryWithSkuMetadata.skuWithMetadata
        val marketPrice = skuWithMetadata.sku.marketPrice

        Text(
            text = marketPrice?.let {
                stringResource(
                    id = R.string.market_price_placeholder,
                    it
                )
            } ?: stringResource(
                id = R.string.lbl_not_available
            ),
            style = MaterialTheme.typography.caption
        )

        Text(
            text = stringResource(
                id = R.string.lowest_listing_price_with_shipping_price_placeholder,
                getListingAndShippingPriceString(
                    LocalContext.current,
                    skuWithMetadata.sku.lowestBasePrice,
                    skuWithMetadata.sku.lowestShippingPrice
                )
            ),
            style = MaterialTheme.typography.caption
        )

        Text(
            text = stringResource(
                id = R.string.quantity_at_average_purchase_price_placeholder,
                stringResource(
                    id = R.string.quantity_avg_price_oneline,
                    inventoryWithSkuMetadata.inventory.quantity,
                    inventoryWithSkuMetadata.inventory.avgPurchasePrice
                )
            ),
            style = MaterialTheme.typography.caption
        )

        val unrealizedProfitPerCard = inventoryWithSkuMetadata.getUnrealizedProfitPerCard()

        Text(
            text = stringResource(
                id = R.string.unrealized_profit_per_card_placeholder,
                if (unrealizedProfitPerCard != null) {
                    stringResource(
                        id = R.string.item_user_list_profit_with_percentage,
                        unrealizedProfitPerCard,
                        inventoryWithSkuMetadata.getUnrealizedProfitPercentagePerCard().toString(),
                    )
                } else {
                    stringResource(id = R.string.lbl_not_available)
                }
            ),
            style = MaterialTheme.typography.caption,
        )

        Text(
            text = stringResource(
                id = R.string.total_unrealized_profit_placeholder,
                inventoryWithSkuMetadata.getTotalUnrealizedProfit() ?: 0.0
            ),
            style = MaterialTheme.typography.caption,
        )

        Text(
            text = stringResource(
                id = R.string.total_realized_profit_placeholder,
                inventoryWithSkuMetadata.inventory.totalRealizedProfit
            ),
            style = MaterialTheme.typography.caption,
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TransactionItemRow(
    transaction: InventoryTransaction,
    onTransactionSwiped: (InventoryTransaction) -> Unit
) {
    val dismissState = rememberDismissState(initialValue = DismissValue.Default)

    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onTransactionSwiped(transaction)
    }

    /*SwipeToDismiss(
        state = dismissState,
        background = { SwipeToDismissDeleteBackground() },
        directions = setOf(DismissDirection.EndToStart),
    ) {*/
    Card(
        Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.material_border_padding)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.text_spacing))
            ) {
                Text(
                    text = transaction.type.getDisplayStringResId(LocalContext.current),
                    style = MaterialTheme.typography.subtitle2,
                )
                transaction.date.formatDate()?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.caption
                    )
                }
            }

            Text(
                text = stringResource(
                    id = R.string.quantity_avg_price_oneline,
                    transaction.quantity,
                    transaction.price
                ),
                modifier = Modifier.align(Alignment.Top),
                style = MaterialTheme.typography.caption
            )
        }
    }
    //}
}

@Preview
@Composable
private fun InventoryDetailScreen() {
    val skuWithMetadata = SkuWithMetadata(
        sku = Sku(0, 0, 0, 0, 69.69, 69.00, 0.69),
        printing = Printing(0, "1st Edition", null),
        condition = Condition(0, "Near Mint", null, null),
        productWithCardSet = ProductWithCardSet(
            product = Product(
                0,
                "Blue-Eyes White Dragon",
                "Blue Eyes White Dragon",
                ProductType.CARD,
                "https://tcgplayer-cdn.tcgplayer.com/product/21715_200w.jpg",
                null,
                number = "LOB-001"
            ),
            cardSet = CardSet(0, "Legend of Blue-Eyes White Dragon", null, null, null),
            rarity = CardRarity(0, "Ultra Rare")
        )
    )

    val transaction = InventoryTransaction(
        0,
        TransactionType.PURCHASE,
        Date(),
        0,
        69.69,
        69,
        0
    )

    val inventory = Inventory(
        0,
        0,
        Date(),
        69,
        69.69,
        696969.69
    )

    val inventoryWithSkuMetadataAndTransactions = InventoryWithSkuMetadataAndTransactions(
        InventoryWithSkuMetadata(inventory, skuWithMetadata),
        MutableList(10) { transaction }
    )
    InventoryDetailScreen(
        inventoryWithSkuMetadataAndTransactions,
        Modifier,
        { },
        { },
        { }
    )
}
