package net.azisaba.rcdamagecore.extension

import org.bukkit.NamespacedKey
import org.bukkit.inventory.EntityEquipment
import org.bukkit.persistence.PersistentDataType

fun EntityEquipment.getArmorStackAsArray() =
    arrayOf(
        helmet,
        chestplate,
        leggings,
        boots,
    ).removeNulls()

fun EntityEquipment.extractTypeDataFromAll(key: NamespacedKey): Int =
    getArmorStackAsArray()
        .map {
            if (it != null && it.hasItemMeta()) {
                it.itemMeta
                    .persistentDataContainer
                    .getOrDefault(key, PersistentDataType.INTEGER, 0)
            } else {
                0
            }
        }.sum()
