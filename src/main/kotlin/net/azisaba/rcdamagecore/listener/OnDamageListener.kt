package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.events.MythicDamageEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class OnDamageListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamaged(e: MythicDamageEvent) {
        // TODO: impl this
    }
}
