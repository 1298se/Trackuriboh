package tang.song.edu.yugiohcollectiontracker.data.repository

import tang.song.edu.yugiohcollectiontracker.data.db.CardDatabase
import tang.song.edu.yugiohcollectiontracker.data.network.CardRetrofitService

class CardSearchBoundaryCallback(
    private val queryString: String,
    private val service: CardRetrofitService,
    private val database: CardDatabase
)
