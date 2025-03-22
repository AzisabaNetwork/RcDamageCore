package net.azisaba.rcdamagecore.config

import org.bukkit.configuration.ConfigurationSection

object RDCConfig {
    private var playerMaxLevel: Int = 0

    fun getPlayerMaxLevel(): Int = playerMaxLevel

    fun loadConfig(section: ConfigurationSection) {
        playerMaxLevel = section.getInt("playerMaxLevel", 10)
    }
}
