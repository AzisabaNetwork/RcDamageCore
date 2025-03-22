package net.azisaba.rcdamagecore.listener

import net.azisaba.rcdamagecore.mythic.mechanics.RcBuffMechanic
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerEventListener : Listener {
    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        RcBuffMechanic.removeUserBuff(e.player.uniqueId)
    }
}
