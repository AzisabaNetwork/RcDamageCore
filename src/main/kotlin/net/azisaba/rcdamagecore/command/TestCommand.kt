package net.azisaba.rcdamagecore.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import net.azisaba.rcdamagecore.data.ItemData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.entity.Player

@CommandAlias("rdctest")
class TestCommand : BaseCommand() {
    @Subcommand("adddata")
    @CommandPermission("rcdamagecore.cmd.test.adddata")
    fun addDataForLevelingItem(player: Player) {
        player.equipment.itemInMainHand.editMeta {
            // apply data {exp: 0, max_level: 100} to held item
            ItemData.EXP.set(it.persistentDataContainer, 0)
            ItemData.MAX_LEVEL.set(it.persistentDataContainer, 100)
        }
        player.sendMessage(Component.text("Success to apply data.").color(NamedTextColor.GREEN))
    }
}
