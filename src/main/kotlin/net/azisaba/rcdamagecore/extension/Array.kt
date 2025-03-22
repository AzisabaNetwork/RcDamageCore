package net.azisaba.rcdamagecore.extension

inline fun <reified T> Array<T>.removeNulls(): Array<T> = filter { it != null }.toTypedArray()
