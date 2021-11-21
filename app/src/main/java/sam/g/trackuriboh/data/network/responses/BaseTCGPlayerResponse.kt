package sam.g.trackuriboh.data.network.responses

interface BaseTCGPlayerResponse<T: Any> {
    val errors: List<String>
    val results: List<T>
}
