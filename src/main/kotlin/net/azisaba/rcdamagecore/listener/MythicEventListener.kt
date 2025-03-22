package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.events.MythicDamageEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.placeholders.Placeholder
import net.azisaba.rcdamagecore.mythic.MythicApi
import net.azisaba.rcdamagecore.mythic.mechanics.RcBuffMechanic
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

class MythicEventListener : Listener {
    @EventHandler
    fun onMythicReloaded(e: MythicReloadedEvent) {
        val pm = MythicApi.placeholderManager
        types.forEach {
            pm.register(
                "caster.buff.$it",
                Placeholder.meta { meta, s ->
                    val uuid = meta.trigger.uniqueId
                    val buffMap = RcBuffMechanic.getBuffMap(uuid)
                    if (buffMap.containsKey(it)) {
                        return@meta buffMap[it].toString() +
                            RcBuffMechanic.armorBuff(meta.trigger.asPlayer() as Player, it)
                    } else {
                        "0"
                    }
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
