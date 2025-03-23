package net.azisaba.rcdamagecore.listener

import net.azisaba.rcdamagecore.config.RDCConfig
import net.azisaba.rcdamagecore.data.ItemData
import net.azisaba.rcdamagecore.extension.isAvailable
import net.azisaba.rcdamagecore.util.LevelCalculator
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryAction
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import kotlin.math.min

class InventoryListener : Listener {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        if (event.action != InventoryAction.SWAP_WITH_CURSOR ||
            event.action != InventoryAction.HOTBAR_SWAP
        ) {
            return
        }

        val player = event.whoClicked.also { if (it !is Player) return }
        val slotStack = event.currentItem.also { if (!it.isAvailable()) return }!!
        val cursorStack = event.cursor.also { if (!it.isAvailable()) return }

        // get item data
        val itemMaxLevel = ItemData.MAX_LEVEL.get(slotStack.persistentDataContainer).also { if (it == 0) return }
        val expItemValue = ItemData.EXP_AMOUNT.get(cursorStack.persistentDataContainer)

        // get player data
        val playerMaxLevel = RDCConfig.getPlayerMaxLevel()

        // actual max level for leveling item
        val maxLevel = min(itemMaxLevel, playerMaxLevel)

        // for checking level changes
        var changeAmount = 0

        slotStack.editMeta { meta ->
            // get item data
            var nowItemExp = ItemData.EXP.get(meta.persistentDataContainer)
            var nowItemLevel = ItemData.LEVEL.get(meta.persistentDataContainer)

            while (slotStack.amount > 0 && nowItemLevel != maxLevel) {
                // add exp
                cursorStack.subtract()
                nowItemExp += expItemValue

                // calc level
                LevelCalculator
                    .doItemLeveling(
                        nowItemExp,
                        nowItemLevel,
                        itemMaxLevel,
                        maxLevel,
                    ).also {
                        nowItemLevel = it.level
                        nowItemExp = it.exp
                        changeAmount += it.changeAmount
                    }
            }

            ItemData.EXP.set(meta.persistentDataContainer, nowItemExp)
            ItemData.LEVEL.set(meta.persistentDataContainer, nowItemLevel)
        }

        player.sendMessage("Level Changed! amount: $changeAmount")

        if (cursorStack.amount == 0) {
            event.setCursor(ItemStack.empty())
        }

        event.isCancelled = true
    }
}
