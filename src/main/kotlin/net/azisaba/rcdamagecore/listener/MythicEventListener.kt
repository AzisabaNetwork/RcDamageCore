package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.placeholders.Placeholder
import net.azisaba.rcdamagecore.integration.MythicApi
import org.bukkit.event.EventHandler
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
