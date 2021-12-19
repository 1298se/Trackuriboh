package sam.g.trackuriboh.data.db.dao

fun getFuzzySearchQuery(query: String?) = "%${(query ?: "").replace(' ', '%')}%"
