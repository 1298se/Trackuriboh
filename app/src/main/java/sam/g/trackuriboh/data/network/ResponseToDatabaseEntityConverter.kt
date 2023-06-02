package sam.g.trackuriboh.data.network

import com.google.firebase.crashlytics.FirebaseCrashlytics
import sam.g.trackuriboh.data.db.entities.CardRarity
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.db.entities.Condition
import sam.g.trackuriboh.data.db.entities.Printing
import sam.g.trackuriboh.data.db.entities.Product
import sam.g.trackuriboh.data.db.entities.Sku
import sam.g.trackuriboh.data.network.responses.CardRarityResponse
import sam.g.trackuriboh.data.network.responses.CardResponse
import sam.g.trackuriboh.data.network.responses.CardSetResponse
import sam.g.trackuriboh.data.network.responses.ConditionResponse
import sam.g.trackuriboh.data.network.responses.PrintingResponse
import sam.g.trackuriboh.data.network.responses.SkuResponse
import sam.g.trackuriboh.data.repository.CatalogRepository
import sam.g.trackuriboh.data.types.ProductType
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseToDatabaseEntityConverter @Inject constructor(
    private val catalogRepository: CatalogRepository
) {
    companion object {
        private const val cardSetResponseDatePattern = "yyyy-MM-dd'T'HH:mm:ss"
    }
    fun toSku(response: SkuResponse.SkuItem) =
        Sku(response.id, response.productId, response.printingId, response.conditionId)

    fun toPrinting(response: PrintingResponse.CardPrintingItem) =
        Printing(response.id, response.name, response.order)

    fun toCondition(response: ConditionResponse.CardConditionItem) =
        Condition(response.id, response.name, response.abbreviation, response.order)

    fun toCardSet(response: CardSetResponse.CardSetItem): CardSet {
        val releaseDate = try {
            response.releaseDate?.let {
                SimpleDateFormat(
                    cardSetResponseDatePattern,
                    Locale.US,
                ).parse(it)
            }
        } catch (e: ParseException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }

        // 2022-07-20T20:42:36.44
        val modifiedDate = try {
            response.modifiedDate?.let {
                SimpleDateFormat(
                    cardSetResponseDatePattern,
                    Locale.US,
                ).parse(it)
            }
        } catch (e: ParseException) {
            FirebaseCrashlytics.getInstance().recordException(e)
            null
        }

        return CardSet(response.id, response.name, response.code, releaseDate, modifiedDate)
    }

    fun toCardRarity(response: CardRarityResponse.CardRarityItem): CardRarity {
        // Common / Short Print is very misleading... I thought it meant the common was short printed.
        // Let's just use Common
        val rarityName =
            if (response.name == "Common / Short Print" && response.simpleName == "Common") response.simpleName else response.name

        return CardRarity(id = response.id, name = rarityName)
    }

    private enum class CardItemExtendedData(val field: String) {
        NUMBER("Number"),
        RARITY("Rarity"),
        ATTRIBUTE("Attribute"),
        CARD_TYPE("Card Type"),
        ATTACK("Attack"),
        DEFENSE("Defense"),
        DESCRIPTION("Description")
    }

    suspend fun toCardProduct(response: CardResponse.CardItem): Product {

        val extendedDataMap = response.extendedData?.associate { it.name to it.value }
        val rarity = extendedDataMap?.get(CardItemExtendedData.RARITY.field)?.let {
            catalogRepository.getCardRarityByName(it)
        } ?: catalogRepository.getCardRarityByName("Unconfirmed")

        return Product(
            id = response.id,
            name = response.name,
            cleanName = response.cleanName,
            type = ProductType.CARD,
            imageUrl = response.imageUrl,
            setId = response.setId,
            number = extendedDataMap?.get(CardItemExtendedData.NUMBER.field),
            rarityId = rarity?.id,
            attribute = extendedDataMap?.get(CardItemExtendedData.ATTRIBUTE.field),
            cardType = extendedDataMap?.get(CardItemExtendedData.CARD_TYPE.field),
            attack = extendedDataMap?.get(CardItemExtendedData.ATTACK.field)?.toInt(),
            defense = extendedDataMap?.get(CardItemExtendedData.DEFENSE.field)?.toInt(),
            description = extendedDataMap?.get(CardItemExtendedData.DESCRIPTION.field),
        )
    }
}