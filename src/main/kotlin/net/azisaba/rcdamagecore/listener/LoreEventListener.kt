package net.azisaba.rcdamagecore.listener

import net.azisaba.loreeditor.api.event.EventBus
import net.azisaba.loreeditor.api.event.ItemEvent
import net.azisaba.loreeditor.libs.net.kyori.adventure.text.Component
import net.azisaba.rcdamagecore.RcDamageCore
import java.util.function.Consumer

object LoreEventListener {
    fun register(plugin: RcDamageCore) {
        EventBus.INSTANCE.register<ItemEvent>(
            plugin,
            ItemEvent::class.java,
            0,
            Consumer { event: ItemEvent ->
                val stack = event.bukkitItem
                val level = LevelUtil.LVL.getOrDefault(stack.persistentDataContainer)
                if (level > 0) {
                    event.addLore(Component.text("武器レベル: $level"))
                }
            },
        )
    }
}
