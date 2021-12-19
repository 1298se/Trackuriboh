package sam.g.trackuriboh.data.network.responses

interface BaseTCGPlayerResponse<T: Any> {
    companion object {
        const val ERROR_FIELD_NAME = "errors"
        const val RESULTS_FIELD_NAME = "results"
    }

    val errors: List<String>
    val results: List<T>
}
