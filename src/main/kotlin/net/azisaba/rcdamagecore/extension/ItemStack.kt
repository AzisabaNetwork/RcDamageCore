package net.azisaba.rcdamagecore.extension

import org.bukkit.inventory.ItemStack

fun ItemStack?.isAvailable() = this != null && hasItemMeta()
