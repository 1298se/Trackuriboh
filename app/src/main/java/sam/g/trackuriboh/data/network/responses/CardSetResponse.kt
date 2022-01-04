package sam.g.trackuriboh.data.network.responses

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.annotations.SerializedName
import sam.g.trackuriboh.data.db.AppDatabase
import sam.g.trackuriboh.data.db.entities.CardSet
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.ERROR_FIELD_NAME
import sam.g.trackuriboh.data.network.responses.BaseTCGPlayerResponse.Companion.RESULTS_FIELD_NAME
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

data class CardSetResponse(
    @SerializedName(ERROR_FIELD_NAME)
    override val errors: List<String>,
    @SerializedName(RESULTS_FIELD_NAME)
    override val results: List<CardSetItem>,
    val totalItems: Int,
) : BaseTCGPlayerResponse<CardSetResponse.CardSetItem> {

    data class CardSetItem(
        @SerializedName("groupId")
        val id: Long,
        val name: String,
        @SerializedName("abbreviation")
        val code: String?,
        @SerializedName("publishedOn")
        val releaseDate: String?
    ) : AppDatabase.DatabaseEntity<CardSet> {

        override fun toDatabaseEntity(): CardSet {
            val releaseDate = try {
                releaseDate?.let { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(releaseDate) }
            } catch (e: ParseException) {
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }

            return CardSet(id, name, code, releaseDate)
        }
    }
}
