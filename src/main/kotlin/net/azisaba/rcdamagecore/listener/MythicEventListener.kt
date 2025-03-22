package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.events.MythicDamageEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.placeholders.Placeholder
import net.azisaba.rcdamagecore.mythic.MythicApi
import net.azisaba.rcdamagecore.mythic.mechanics.RcBuffMechanic
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class MythicEventListener : Listener {
    @EventHandler
    fun onMythicReloaded(e: MythicReloadedEvent) {
        val placeholderManager = MythicApi.placeholderManager
        types.forEach {
            placeholderManager.register(
                "caster.buff.$it",
                Placeholder.meta { meta, s ->
                    // TODO: impl this
                    return@meta ""
                },
            )
        }
    }

    @EventHandler
    fun onMechanicLoad(e: MythicMechanicLoadEvent) {
        when (e.mechanicName.lowercase()) {
            "rcbuff" -> e.register(RcBuffMechanic(e.config))
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamaged(e: MythicDamageEvent) {
        // TODO: impl this
    }

    companion object {
        private val types =
            listOf(
                "none",
                "fire",
                "water",
                "wind",
                "lightning",
                "dark",
                "photon",
                "critper",
                "critdmg",
                "nocritdmg",
            )
    }
}
