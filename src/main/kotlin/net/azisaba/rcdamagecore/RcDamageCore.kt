package net.azisaba.rcdamagecore

import co.aikar.commands.PaperCommandManager
import net.azisaba.rcdamagecore.command.ExpCommand
import net.azisaba.rcdamagecore.command.TestCommand
import net.azisaba.rcdamagecore.config.RDCConfig
import net.azisaba.rcdamagecore.extension.registerEvents
import net.azisaba.rcdamagecore.listener.MythicEventListener
import net.azisaba.rcdamagecore.listener.PlayerEventListener
import org.bukkit.plugin.java.JavaPlugin

class RcDamageCore : JavaPlugin() {
    lateinit var commandManager: PaperCommandManager

    override fun onEnable() {
        // load config
        RDCConfig.loadConfig(config.getConfigurationSection("leveling") ?: error("Failed to find leveling in $PL_NAME config."))

        // register event listeners
        registerEvents(
            PlayerEventListener(),
            MythicEventListener(),
        )

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
