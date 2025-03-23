package net.azisaba.rcdamagecore

import co.aikar.commands.PaperCommandManager
import io.lumine.mythic.bukkit.utils.Schedulers
import net.azisaba.rcdamagecore.command.ExpCommand
import net.azisaba.rcdamagecore.command.TestCommand
import net.azisaba.rcdamagecore.config.RDCConfig
import net.azisaba.rcdamagecore.extension.registerEvents
import net.azisaba.rcdamagecore.listener.InventoryListener
import net.azisaba.rcdamagecore.listener.ItemCheckListener
import net.azisaba.rcdamagecore.listener.LoreEventListener
import net.azisaba.rcdamagecore.listener.MythicEventListener
import net.azisaba.rcdamagecore.listener.PlayerEventListener
import net.azisaba.rcdamagecore.mythic.MythicApi
import org.bukkit.plugin.java.JavaPlugin
import java.util.concurrent.TimeUnit

class RcDamageCore : JavaPlugin() {
    lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        // load config
        RDCConfig.loadConfig(config.getConfigurationSection("leveling") ?: error("Failed to find leveling in $PL_NAME config."))

        // register event listeners
        registerEvents(
            PlayerEventListener(),
            MythicEventListener(),
            InventoryListener(),
            ItemCheckListener(),
        )

        LoreEventListener.register(this)

        Schedulers.sync().runLater({
            MythicApi.bootstrap.dispatchCommand("mythicmobs reload -a")
            slF4JLogger.info("MythicMobsを再読み込みしました。")
        }, 5, TimeUnit.SECONDS)

        commandManager = PaperCommandManager(this)
        commandManager.registerCommand(ExpCommand(this))
        commandManager.registerCommand(TestCommand())

        // notify enable sequence completed.
        logger.info("$PL_NAME was enabled!")

        // set default instance = initialization succeeded
        instance = this
    }

    override fun onDisable() {
        if (instance != null) {
            instance = null
            commandManager.unregisterCommands()

            logger.info("See you!")
        }
    }

    companion object {
        private var instance: RcDamageCore? = null

        fun getInstance(): RcDamageCore {
            if (instance == null) {
                error("$PL_NAME was not initialized!")
            }
            return instance!!
        }
    }
}
