package net.azisaba.rcdamagecore.config

import org.bukkit.configuration.ConfigurationSection

object RDCConfig {
    private var playerMaxLevel: Int = 0
    private var levelGapAdjustment: Int = 0

    fun getPlayerMaxLevel(): Int = playerMaxLevel

    fun getLevelGapAdjustment(): Int = levelGapAdjustment

    fun loadConfig(section: ConfigurationSection) {
        playerMaxLevel = section.getInt("playerMaxLevel", 10)
        levelGapAdjustment = section.getInt("levelGapAdjustment", 80)
    }
}
