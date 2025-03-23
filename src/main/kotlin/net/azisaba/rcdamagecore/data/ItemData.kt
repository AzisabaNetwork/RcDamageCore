package net.azisaba.rcdamagecore.data

import net.azisaba.rcdamagecore.util.IntKey
import org.bukkit.NamespacedKey

object ItemData {
    internal const val NAMESPACE = "rcpve"

    // for leveling item
    val EXP = IntKey(NamespacedKey(NAMESPACE, "exp"), 0)
    val LEVEL = IntKey(NamespacedKey(NAMESPACE, "level"), 0)
    val MAX_LEVEL = IntKey(NamespacedKey(NAMESPACE, "max_level"), 0)

    // for experience item
    val EXP_AMOUNT = IntKey(NamespacedKey(NAMESPACE, "exp_amount"), 0)
}
