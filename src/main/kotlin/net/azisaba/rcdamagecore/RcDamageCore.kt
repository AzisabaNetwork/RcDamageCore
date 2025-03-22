package net.azisaba.rcdamagecore

import net.azisaba.rcdamagecore.extension.registerEvents
import org.bukkit.plugin.java.JavaPlugin

class RcDamageCore : JavaPlugin() {
    override fun onEnable() {
        // notify enable sequence completed.
        logger.info("$PL_NAME was enabled!")
        registerEvents()
    }

    override fun onDisable() {
        logger.info("See you!")
    }
}
