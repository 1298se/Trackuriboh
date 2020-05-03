package tang.song.edu.yugiohcollectiontracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.models.CardType
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetDetailResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse
import java.util.*
import kotlin.collections.ArrayList

class ResponseUtils {

    companion object {
        suspend fun convertCardResponseListToCardList(cardList: List<CardResponse>): List<Card> =
            withContext(Dispatchers.Default) {
                val result = ArrayList<Card>()

                for (card in cardList) {
                    val cardImageList = ArrayList<String>()

                    for (cardImageResponse in card.cardImages ?: emptyList()) {
                        cardImageList.add(cardImageResponse.imageUrl)
                    }

                    result.add(
                        Card(
                            card.id,
                            card.name,
                            CardType.fromString(card.type),
                            card.desc,
                            card.atk,
                            card.def,
                            card.level,
                            card.race,
                            card.attribute,
                            card.archetype,
                            card.scale,
                            cardImageList
                        )
                    )
                }
                result
            }

        suspend fun convertSetResponseListToSetList(cardSetList: List<CardSetResponse>): List<CardSet> =
            withContext(Dispatchers.Default) {

                val result = ArrayList<CardSet>()

                for (set in cardSetList) {
                    result.add(
                        CardSet(
                            set.setCode,
                            set.setName,
                            set.numOfCards,
                            set.releaseDate
                        )
                    )
                }
                result
            }

        suspend fun createJoins(cardList: List<CardResponse>): List<CardXCardSetRef> =
            withContext(Dispatchers.Default) {

                val result = ArrayList<CardXCardSetRef>()

                for (card in cardList) {
                    for (cardSet in card.cardSetDetails ?: Collections.emptyList()) {
                        result.add(CardXCardSetRef(card.id, parseCardSet(cardSet), cardSet.setRarity))
                    }
                }

                result
            }

        private fun parseCardSet(cardSetDetail: CardSetDetailResponse): String {
            val hyphenIndex = cardSetDetail.setCode.indexOf('-')

            return if (hyphenIndex != -1) cardSetDetail.setCode.substring(0, hyphenIndex) else cardSetDetail.setCode
        }
    }
}