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
                            cardId = card.id,
                            name = card.name,
                            type = CardType.fromString(card.type),
                            desc = card.desc,
                            atk = card.atk,
                            def = card.def,
                            level = card.level,
                            race = card.race,
                            attribute = card.attribute,
                            archetype = card.archetype,
                            scale = card.scale,
                            linkval = card.linkval,
                            linkmarkers = card.linkmarkers,
                            cardImageList = cardImageList
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
                            setCode = set.setCode,
                            setName = set.setName,
                            numOfCards = set.numOfCards,
                            releaseDate = set.releaseDate
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
                        result.add(
                            CardXCardSetRef(
                                cardNumber = cardSet.setCode,
                                cardId = card.id,
                                setCode = parseCardSet(cardSet.setCode),
                                rarity = cardSet.setRarity,
                                price = cardSet.setPrice
                            )
                        )
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