package sam.g.trackuriboh.utils

fun joinStringsWithDelimiter(separator: String?, vararg strings: String?) =
    strings.filterNotNull().joinToString(" $separator ")

fun joinStringsWithInterpunct(vararg strings: String?) =
    joinStringsWithDelimiter(" \u2022 ", *strings)