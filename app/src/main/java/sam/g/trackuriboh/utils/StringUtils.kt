package sam.g.trackuriboh.utils

fun joinStringsWithDelimiter(separator: String?, vararg strings: String?) =
    strings.filterNotNull().joinToString(" \u2022 ")

fun joinStringsWithInterpunct(vararg strings: String?) =
    joinStringsWithDelimiter(" \u2022 ", *strings)