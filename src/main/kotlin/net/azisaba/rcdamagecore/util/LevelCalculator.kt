package net.azisaba.rcdamagecore.util

import net.azisaba.rcdamagecore.config.RDCConfig
import org.jetbrains.annotations.VisibleForTesting
import kotlin.math.min

data class LevelInfo(
    val exp: Int,
    val level: Int,
    val changeAmount: Int,
    val nextRequireExp: Int = -1,
)

object LevelCalculator {
    fun getLevelExp(
        targetLevel: Int,
        maxLevel: Int,
    ): Int {
        // basic exp calc
        var requireExp = targetLevel * 1000

        if (maxLevel - targetLevel >= 100) {
            // 必要経験値量を下げる処理
            // 200 -> 160 に低下させたい場合に80%を指定する。
            requireExp *= (RDCConfig.getLevelGapAdjustment() / 100)
        }

        return requireExp
    }

    @VisibleForTesting
    fun doLeveling(
        currentExp: Int,
        currentLevel: Int,
        maxLevel: Int,
    ): LevelInfo {
        // do nothing
        if (currentLevel == maxLevel) {
            // reaches the limit
            return LevelInfo(
                currentExp,
                currentLevel,
                0,
            )
        }

        var exp = currentExp
        var level = currentLevel
        var changes = 0

        if (currentLevel > maxLevel) {
            // Level down
            while (level > maxLevel) {
                exp += getLevelExp(level, maxLevel)
                level--
                changes--
            }
        } else {
            // Level up challenge
            while (true) {
                val nextLevelExp = getLevelExp(level + 1, maxLevel)
                if (nextLevelExp > exp || level + 1 > maxLevel) break
                exp -= nextLevelExp
                level++
                changes++
            }
        }

        return LevelInfo(
            exp,
            level,
            changes,
            getLevelExp(level + 1, maxLevel),
        )
    }

    fun doItemLeveling(
        currentExp: Int,
        currentLevel: Int,
        maxItemLevel: Int,
        playerLevel: Int,
    ): LevelInfo =
        doLeveling(
            currentExp,
            currentLevel,
            min(maxItemLevel, playerLevel),
        )

    fun doPlayerLeveling(
        currentExp: Int,
        currentLevel: Int,
    ): LevelInfo =
        doLeveling(
            currentExp,
            currentLevel,
            RDCConfig.getPlayerMaxLevel(),
        )
}
