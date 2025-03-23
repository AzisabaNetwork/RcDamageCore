package net.azisaba.rcdamagecore.data

import net.azisaba.rcdamagecore.util.IntKey
import org.bukkit.NamespacedKey

object ItemData {
    internal const val NAMESPACE = "rcpve"

    // current exp value
    val EXP = IntKey(NamespacedKey(NAMESPACE, "exp"), 0)

    // to check next require exp value
    val EXP_NEXT = IntKey(NamespacedKey(NAMESPACE, "exp_next"), 0)

    // current level
    val LEVEL = IntKey(NamespacedKey(NAMESPACE, "level"), 0)

    // max level
    val MAX_LEVEL = IntKey(NamespacedKey(NAMESPACE, "max_level"), 0)

    // for experience item
    val EXP_AMOUNT = IntKey(NamespacedKey(NAMESPACE, "exp_amount"), 0)
}
