package tang.song.edu.yugiohcollectiontracker

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.models.CardType
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse

class ResponseUtils {

    companion object {
        suspend fun convertCardResponseListToCardList(cardList: List<CardResponse>): List<Card> =
            withContext(Dispatchers.Default) {
                val result = ArrayList<Card>()

                for (card in cardList) {
                    val cardImageList = ArrayList<String>()

                    card.cardImages?.forEach { cardImageResponse ->
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
                    card.cardSetDetails?.forEach { cardSet ->
                        result.add(CardXCardSetRef(card.id, parseCardSet(cardSet.setCode), cardSet.setCode, cardSet.setRarity, cardSet.setPrice))
                    }
                }

                result
            }

        private fun parseCardSet(cardNumber: String): String {
            val hyphenIndex = cardNumber.indexOf('-')

            return if (hyphenIndex != -1) cardNumber.substring(0, hyphenIndex) else cardNumber
        }
    }
}