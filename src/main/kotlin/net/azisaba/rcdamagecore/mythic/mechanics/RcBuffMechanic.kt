package net.azisaba.rcdamagecore.mythic.mechanics

import io.lumine.mythic.api.adapters.AbstractEntity
import io.lumine.mythic.api.config.MythicLineConfig
import io.lumine.mythic.api.skills.ISkillMechanic
import io.lumine.mythic.api.skills.ITargetedEntitySkill
import io.lumine.mythic.api.skills.SkillMetadata
import io.lumine.mythic.api.skills.SkillResult
import io.lumine.mythic.bukkit.BukkitAdapter
import net.azisaba.rcdamagecore.LOGGER
import net.azisaba.rcdamagecore.RcDamageCore
import net.azisaba.rcdamagecore.extension.extractTypeDataFromAll
import net.azisaba.rcdamagecore.extension.getInt
import net.azisaba.rcdamagecore.extension.getStr
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.entity.LivingEntity
import java.util.UUID

class RcBuffMechanic(
    config: MythicLineConfig,
) : ISkillMechanic,
    ITargetedEntitySkill {
    val type: String = config.getStr("type", "t").lowercase()
    val duration: Int = config.getInt("duration", "d")
    val amount: Int = config.getInt("amount", "a")

    override fun castAtEntity(
        skillMetadata: SkillMetadata?,
        abstractEntity: AbstractEntity?,
    ): SkillResult? {
        try {
            val entity = BukkitAdapter.adapt(abstractEntity)
            val uuid = entity.uniqueId
            getBuffMap(uuid).compute(type) { k, v ->
                if (v == null) {
                    amount
                } else {
                    v + amount
                }
            }

            Bukkit.getScheduler().runTaskLater(
                RcDamageCore.getInstance(),
                Runnable {
                    val userBuffMap = getUserBuffIfDefined(uuid, type)
                    if (userBuffMap == null) return@Runnable

                    userBuffMap.compute(type) { k, v ->
                        if (v == null) {
                            0
                        } else {
                            v - amount
                        }
                    }

                    userBuffMap.compute(type) { k, v ->
                        if (v != null && v <= 0) {
                            null
                        } else {
                            v
                        }
                    }
                },
                duration.toLong(),
            )
        } catch (e: Exception) {
            LOGGER.error("Failed to cast skill", e)
        }
        return SkillResult.SUCCESS
    }

    companion object {
        private val buffStatusMap = mutableMapOf<UUID, MutableMap<String, Int>>()

        fun removeUserBuff(uuid: UUID) {
            buffStatusMap.remove(uuid)
        }

        fun getBuffMap(uuid: UUID): MutableMap<String, Int> {
            if (!buffStatusMap.contains(uuid)) {
                buffStatusMap.put(uuid, HashMap())
            }
            return buffStatusMap[uuid]!!
        }

        fun getUserBuffIfDefined(
            uuid: UUID,
            type: String,
        ): MutableMap<String, Int>? {
            buffStatusMap[uuid]?.apply {
                if (get(type) != null) return this
            }
            return null
        }

        fun armorBuff(
            player: LivingEntity,
            type: String,
        ): Int {
            val key = NamespacedKey("rc", type)
            (player.equipment ?: return 0).apply {
                return extractTypeDataFromAll(key)
            }
        }
    }
}
