package net.azisaba.rcdamagecore.extension

import net.azisaba.rcdamagecore.data.ItemData
import net.azisaba.rcdamagecore.util.LevelCalculator
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

fun ItemStack?.isAvailable() = this != null && hasItemMeta()

fun ItemStack.checkItemLevel(owner: Player): ItemStack {
    val exp = ItemData.EXP.get(persistentDataContainer)
    val nextRequire = ItemData.EXP_NEXT.get(persistentDataContainer)
    if (exp >= nextRequire) {
        LevelCalculator
            .doItemLeveling(
                exp,
                ItemData.LEVEL.get(persistentDataContainer),
                ItemData.MAX_LEVEL.get(persistentDataContainer),
                ItemData.LEVEL.get(owner.persistentDataContainer),
            ).also {
                editMeta { meta ->
                    ItemData.EXP.set(meta.persistentDataContainer, it.exp)
                    ItemData.LEVEL.set(meta.persistentDataContainer, it.level)
                    if (it.nextRequireExp != -1) {
                        ItemData.EXP_NEXT.set(meta.persistentDataContainer, it.nextRequireExp)
                    }
                }
            }
    }
    return this
}
