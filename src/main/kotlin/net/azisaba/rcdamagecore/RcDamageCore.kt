package net.azisaba.rcdamagecore

import net.azisaba.rcdamagecore.extension.registerEvents
import net.azisaba.rcdamagecore.listener.MythicEventListener
import net.azisaba.rcdamagecore.listener.PlayerEventListener
import org.bukkit.plugin.java.JavaPlugin

class RcDamageCore : JavaPlugin() {
    override fun onEnable() {
        instance = this
        // notify enable sequence completed.
        logger.info("$PL_NAME was enabled!")
        registerEvents(
            PlayerEventListener(),
            MythicEventListener(),
        )
    }

    override fun onDisable() {
        instance = null
        logger.info("See you!")
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
