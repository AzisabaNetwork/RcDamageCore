package net.azisaba.rcdamagecore.extension

import org.bukkit.entity.Player
import org.bukkit.inventory.PlayerInventory

fun PlayerInventory.checkAllItemLevel() {
    val player = this.holder ?: return
    if (player !is Player) return
    contents.forEach {
        it?.checkItemLevel(player)
    }
}
