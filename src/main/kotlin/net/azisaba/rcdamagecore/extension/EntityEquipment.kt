package net.azisaba.rcdamagecore.extension

import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.EntityEquipment
import org.bukkit.persistence.PersistentDataType

fun EntityEquipment.getArmorsAsArray() =
    arrayOf(
        helmet,
        chestplate,
        leggings,
        boots,
    ).removeNulls()

fun EntityEquipment.getHandsAsArray() =
    arrayOf(
        itemInMainHand,
        itemInOffHand,
    ).removeNulls()

fun EntityEquipment.extractTypeDataFromAll(key: NamespacedKey): Int =
    getArmorsAsArray()
        .map {
            if (it != null && it.hasItemMeta()) {
                it.itemMeta
                    .persistentDataContainer
                    .getOrDefault(key, PersistentDataType.INTEGER, 0)
            } else {
                0
            }
        }.sum()

fun EntityEquipment.getProtectionValueFromAll(): Int = getArmorsAsArray().sumOf { it.getEnchantmentLevel(Enchantment.PROTECTION) }
