package net.azisaba.rcdamagecore.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Subcommand
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender
import takumi3s.rcdamagecore.RcDamageCore
import takumi3s.rcdamagecore.config.RDCConfig
import takumi3s.rcdamagecore.util.LevelUtil
import java.util.function.Function
import kotlin.math.min

@CommandAlias("rcexp")
class ExpCommand(
    private val plugin: RcDamageCore,
) : BaseCommand() {
    @Default
    fun onDefault(sender: CommandSender) {
        sender.sendMessage("fmm... there is nothing...")
    }

    @Subcommand("reload-config")
    @CommandPermission("rcdamagecore.cmd.rcexp.reload")
    fun reloadConfig(sender: CommandSender) {
        sender.sendMessage("Reloading now...")
        plugin.reloadConfig()
        RDCConfig.loadConfig(plugin.config)
        sender.sendMessage("Reload completed.")
    }

    @Subcommand("give")
    @CommandCompletion("@players @range:0-100")
    @CommandPermission("rcdamagecore.cmd.rcexp.give")
    fun giveExp(
        sender: CommandSender,
        playerName: String,
        @Default("0") amount: Int,
    ) {
        val player = plugin.server.getPlayer(playerName)
        if (player == null) {
            sender.sendMessage(Component.text("Player not found. mcid: $playerName"))
            return
        }

        if (amount <= 0) {
            sender.sendMessage(Component.text("経験値量は1以上の整数である必要があります。"))
            return
        }

        val dataContainer = player.persistentDataContainer

        LevelUtil.EXP.computeWithDefault(dataContainer, Function { v: Int? -> v!! + amount })

        var nowExp = LevelUtil.EXP.getOrDefault(dataContainer)
        var nowLevel = LevelUtil.LVL.getOrDefault(dataContainer)
        val maxLevel = min(MAX_LEVEL.toDouble(), RDCConfig.playerMaxLevel.toDouble()).toInt()
        var count = 0

        if (nowLevel < maxLevel) {
            // increment loop
            while (true) {
                // calc require exp for next level
                val requireExp = LevelUtil.calcRequireExp(nowLevel + 1)

                // if nowExp is not enough to level up, exit while loop
                if (nowExp < requireExp) break

                // if reaches max level, skip incrementation.
                if (nowLevel == maxLevel) break

                // if it can level up
                nowExp -= requireExp
                nowLevel++

                // counter increment
                count++
            }
        }

        LevelUtil.EXP.set(dataContainer, nowExp)
        LevelUtil.LVL.set(dataContainer, nowLevel)

        //        player.sendMessage(Component.text("Updated! difference: " + count));
        sender.sendMessage(Component.text(String.format("%sの経験値を%d増やしました。", playerName, amount)))
    }

    companion object {
        const val MAX_LEVEL: Int = 10000
    }
}
