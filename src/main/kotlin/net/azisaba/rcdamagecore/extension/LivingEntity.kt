package net.azisaba.rcdamagecore.extension

import org.bukkit.attribute.Attribute
import org.bukkit.entity.LivingEntity

fun LivingEntity.getAttributeValue(
    targetAttribute: Attribute,
    defaultValue: Int = 0,
): Double = (getAttribute(targetAttribute)?.value ?: defaultValue) as Double
