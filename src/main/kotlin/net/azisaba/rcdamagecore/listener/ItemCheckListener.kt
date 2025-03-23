package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.utils.events.extra.ArmorEquipEvent
import io.papermc.paper.event.player.PlayerArmSwingEvent
import net.azisaba.rcdamagecore.extension.checkAllItemLevel
import net.azisaba.rcdamagecore.extension.checkItemLevel
import net.azisaba.rcdamagecore.extension.getHandsAsArray
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerJoinEvent

class ItemCheckListener : Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onInventoryDrag(e: InventoryDragEvent) {
        val player = e.whoClicked
        if (player is Player) {
            e.cursor?.checkItemLevel(player)
            e.newItems.forEach { _, stack -> stack.checkItemLevel(player) }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPickupItem(e: EntityPickupItemEvent) {
        val player = e.entity
        if (player is Player) {
            e.item.apply {
                itemStack = itemStack.checkItemLevel(player)
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onInventoryClick(e: InventoryClickEvent) {
        e.currentItem?.checkItemLevel(e.whoClicked as Player)
        e.cursor.checkItemLevel(e.whoClicked as Player)
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onInteract(e: PlayerInteractEvent) {
        e.player.equipment
            .getHandsAsArray()
            .forEach { it.checkItemLevel(e.player) }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onArmorEquip(e: ArmorEquipEvent) {
        e.oldArmorPiece.checkItemLevel(e.player)
        e.newArmorPiece.checkItemLevel(e.player)
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.player.inventory.checkAllItemLevel()
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    fun onArmSwing(e: PlayerArmSwingEvent) {
        e.player.equipment
            .getHandsAsArray()
            .forEach { it.checkItemLevel(e.player) }
    }
}
