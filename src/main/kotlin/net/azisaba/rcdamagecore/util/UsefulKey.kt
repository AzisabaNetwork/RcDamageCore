package net.azisaba.rcdamagecore.util

import io.papermc.paper.persistence.PersistentDataContainerView
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType

abstract class UsefulKey<C>(
    internal val key: NamespacedKey,
    internal val dataType: PersistentDataType<*, C>,
    internal val defaultValue: C,
) {
    abstract fun get(dataContainerView: PersistentDataContainerView): C

    abstract fun set(
        dataContainer: PersistentDataContainer,
        newValue: C,
    )
}

class IntKey(
    key: NamespacedKey,
    defaultValue: Int,
) : UsefulKey<Int>(
        key,
        PersistentDataType.PrimitivePersistentDataType.INTEGER,
        defaultValue,
    ) {
    override fun get(dataContainerView: PersistentDataContainerView): Int = dataContainerView.getOrDefault(key, dataType, defaultValue)

    override fun set(
        dataContainer: PersistentDataContainer,
        newValue: Int,
    ) {
        dataContainer.set(key, dataType, newValue)
    }
}
