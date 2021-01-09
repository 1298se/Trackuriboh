package tang.song.edu.yugiohcollectiontracker.data.db

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.paging.PagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import tang.song.edu.yugiohcollectiontracker.data.db.entities.Card
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardSet
import tang.song.edu.yugiohcollectiontracker.data.db.entities.CardXCardSetRef
import tang.song.edu.yugiohcollectiontracker.data.db.relations.CardWithSetInfo
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardResponse
import tang.song.edu.yugiohcollectiontracker.data.network.response.CardSetResponse
import tang.song.edu.yugiohcollectiontracker.data.types.CardType
import javax.inject.Inject

class CardLocalCache @Inject constructor(
    private val cardDatabase: CardDatabase
) {
    suspend fun populateDatabase(
        tag: String,
        cardList: List<CardResponse>?,
        cardSetList: List<CardSetResponse>?
    ) {
        withContext(Dispatchers.Default) {
            cardList?.map { convertCardResponseToCard(it) }?.also {
                cardDatabase.cardDao().insertCards(it)
            }
            cardSetList?.map { convertCardSetResponseToCardSet(it) }?.also {
                cardDatabase.cardSetDao().insertCardSets(it)
            }

            createJoins(cardList ?: emptyList()).also {
                for (join in it) {
                    try {
                        cardDatabase.cardXCardSetDao().insertJoin(join)
                    } catch (exception: SQLiteConstraintException) {
                        // Sometimes card set has small typos, try to create the join with fuzzy search
                        try {
                            cardDatabase.cardXCardSetDao().fuzzyInsertJoin(join)
                        } catch (exception: SQLiteConstraintException) {
                            Log.d(tag, "join with cardnumber ${join.cardNumber} and setname ${join.setName} not inserted")
                        }
                    }
                }
            }
        }
    }

    private fun convertCardResponseToCard(card: CardResponse): Card {
        val cardImageList = ArrayList<String>()

        card.cardImages?.forEach { cardImageResponse ->
            cardImageList.add(cardImageResponse.imageUrl)

        }

        return Card(
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
                cardImageURLList = cardImageList
        )
    }

    private fun convertCardSetResponseToCardSet(cardSet: CardSetResponse): CardSet {
        return CardSet(
            setCode = cardSet.setCode,
            setName = cardSet.setName,
            numOfCards = cardSet.numOfCards,
            releaseDate = cardSet.releaseDate
        )
    }

    private fun createJoins(cardList: List<CardResponse>): List<CardXCardSetRef> {
            val result = ArrayList<CardXCardSetRef>()

            for (card in cardList) {
                card.cardSetDetails?.forEach { cardSet ->
                    result.add(
                        CardXCardSetRef(
                            cardNumber = cardSet.setCode,
                            cardId = card.id,
                            setName = cardSet.setName,
                            rarity = cardSet.setRarity,
                            price = cardSet.setPrice
                        )
                    )
                }
            }

            return result
    }

    fun clearDatabase() {
        return cardDatabase.clearAllTables()
    }

    suspend fun getCardDetails(cardId: Long): CardWithSetInfo {
        return cardDatabase.cardXCardSetDao().getCardWithSetInfo(cardId)
    }

    suspend fun getCardSet(setName: String): CardSet {
        return cardDatabase.cardSetDao().getCardSet(setName)
    }

    fun getCardSetList(): PagingSource<Int, CardSet> {
        return cardDatabase.cardSetDao().getCardSetList()
    }

    fun getCardListBySet(setName: String): PagingSource<Int, Card> {
        return cardDatabase.cardXCardSetDao().getCardListBySet(setName)
    }

    fun searchCardByName(name: String?): PagingSource<Int, Card> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return cardDatabase.cardDao().searchCardByName(query)
    }

    fun searchCardSetByName(name: String?): PagingSource<Int, CardSet> {
        val query = "%${(name ?: "").replace(' ', '%')}%"
        return cardDatabase.cardSetDao().searchCardSetByName(query)
    }
}