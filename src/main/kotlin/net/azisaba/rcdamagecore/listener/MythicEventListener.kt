package net.azisaba.rcdamagecore.listener

import io.lumine.mythic.bukkit.events.MythicDamageEvent
import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent
import io.lumine.mythic.bukkit.events.MythicReloadedEvent
import io.lumine.mythic.core.skills.placeholders.Placeholder
import net.azisaba.rcdamagecore.extension.getAttributeValue
import net.azisaba.rcdamagecore.extension.getProtectionValueFromAll
import net.azisaba.rcdamagecore.mythic.MythicApi
import net.azisaba.rcdamagecore.mythic.mechanics.RcBuffMechanic
import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.inventory.EntityEquipment
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import java.util.Objects

class MythicEventListener : Listener {
    @EventHandler
    fun onMythicReloaded(e: MythicReloadedEvent) {
        val pm = MythicApi.placeholderManager
        types.forEach {
            pm.register(
                "caster.buff.$it",
                Placeholder.meta { meta, s ->
                    val uuid = meta.trigger.uniqueId
                    val buffMap = RcBuffMechanic.getBuffMap(uuid)
                    if (buffMap.containsKey(it)) {
                        return@meta buffMap[it].toString() +
                            RcBuffMechanic.armorBuff(meta.trigger.asPlayer() as Player, it)
                    } else {
                        "0"
                    }
                },
            )
        }
    }

    @EventHandler
    fun onMechanicLoad(e: MythicMechanicLoadEvent) {
        when (e.mechanicName.lowercase()) {
            "RcBuffMechanic" -> e.register(RcBuffMechanic(e.config))
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamaged(e: MythicDamageEvent) {
        // ダメージを与えた人のデータ取得
        var attack = 0.0
        val mmType = e.damageMetadata.element

        val damager = e.caster.entity

        if (damager !is LivingEntity) return

        // 攻撃力
        attack = damager.getAttributeValue(Attribute.GENERIC_ATTACK_DAMAGE)

        var damage = e.damage
        val uuid = damager.uniqueId

        // ダメージを受けた人のデータ取得
        var armorToughness = 0.0
        var armor = 0.0
        var armorEc = 0.0
        var resistance = 0.0

        var damaged = e.target
        if (damaged is LivingEntity) {
            armor = damaged.getAttributeValue(Attribute.GENERIC_ARMOR)
            armorToughness = damaged.getAttributeValue(Attribute.GENERIC_ARMOR_TOUGHNESS)

            val equipment: EntityEquipment? = damaged.equipment
            if (equipment != null) {
                armorEc += equipment.getProtectionValueFromAll()
            }

            // 耐性効果
            if (damaged.hasPotionEffect(PotionEffectType.RESISTANCE)) {
                resistance =
                    (
                        Objects
                            .requireNonNull<PotionEffect?>(damaged.getPotionEffect(PotionEffectType.RESISTANCE))
                            .amplifier + 1
                    ).toDouble()
            }
        }

        // クリティカルの計算。
        var critValue = (Math.random() * 1000).toInt()
        var critPer: Int = 10 + RcBuffMechanic.armorBuff(damager, "critper")
        var critDamage: Int = 5 + RcBuffMechanic.armorBuff(damager, "critdmg")
        var nocritDamage = 0.0

        // バフがある場合
        RcBuffMechanic.getBuffMap(uuid).apply {
            critPer += getOrDefault("critper", 0)
            critDamage += getOrDefault("critdmg", 0)
            nocritDamage += getOrDefault("nocritdmg", 0)
        }

        damage *=
            if (critPer >= critValue) {
                (1 + (critDamage / 1000))
            } else {
                (1 + (nocritDamage / 1000))
            }.toDouble()

        // 属性バフの計算

        var buff = 0
        RcBuffMechanic.getBuffMap(uuid).apply {
            buff = get(mmType) ?: return@apply
            damage *= (1 + (buff.toDouble() / 1000))
        }

        // 最終的なダメージの計算式
        val finalDamage =
            (damage * (1 - armorToughness * 0.01) * (1 - armorEc / 128) + (attack - 1 - armor)) * (1 - resistance / 10)

        if (finalDamage <= 0) {
            e.isCancelled = true
            return
        }

        e.damage = finalDamage
    }

    companion object {
        private val types =
            listOf(
                "none",
                "fire",
                "water",
                "wind",
                "lightning",
                "dark",
                "photon",
                "critper",
                "critdmg",
                "nocritdmg",
            )
    }
}
