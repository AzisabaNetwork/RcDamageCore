package takumi3s.rcdamagecore.util;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class UsefulKey<T> {
    private final NamespacedKey nsKey;
    private final PersistentDataType<?, T> dataType;
    private final T defaultValue;

    public UsefulKey(Plugin plugin, String key, PersistentDataType<?, T> dataType) {
        this(new NamespacedKey(plugin, key), dataType, null);
    }

    public UsefulKey(Plugin plugin, String key, PersistentDataType<?, T> dataType, @Nullable T defaultValue) {
        this(new NamespacedKey(plugin, key), dataType, defaultValue);
    }

    public UsefulKey(String namespace, String key, PersistentDataType<?, T> dataType) {
        this(new NamespacedKey(namespace, key), dataType, null);
    }

    public UsefulKey(String namespace, String key, PersistentDataType<?, T> dataType, @Nullable T defaultValue) {
        this(new NamespacedKey(namespace, key), dataType, defaultValue);
    }

    public UsefulKey(NamespacedKey nsKey, PersistentDataType<?, T> dataType) {
        this(nsKey, dataType, null);
    }

    public UsefulKey(NamespacedKey nsKey, PersistentDataType<?, T> dataType, @Nullable T defaultValue) {
        this.nsKey = nsKey;
        this.dataType = dataType;
        this.defaultValue = defaultValue;
    }

    public T getOrDefault(PersistentDataContainerView dataContainerView) {
        T result = get(dataContainerView);
        if(result == null) {
            return defaultValue;
        }
        return result;
    }

    @NotNull
    public T getOrDefault(PersistentDataContainerView dataContainerView, @NotNull T defaultValue) {
        T result = get(dataContainerView);
        if(result == null) {
            return defaultValue;
        }
        return result;
    }

    @Nullable
    public T get(PersistentDataContainerView dataContainerView) {
        return dataContainerView.get(nsKey, dataType);
    }

    public void set(PersistentDataContainer dataContainer, @NotNull T value) {
        dataContainer.set(nsKey, dataType, value);
    }

    public void computeWithDefault(PersistentDataContainer dataContainer, Function<T, T> computeFunc) {
        compute(dataContainer, v -> computeFunc.apply(v == null ? defaultValue : v));
    }


    public void compute(PersistentDataContainer dataContainer, Function<T, T> computeFunc) {
        set(dataContainer, computeFunc.apply(get(dataContainer)));
    }

    /**
     * CAUTION: We don't recommend using this. But you can use this if method really needs {@link NamespacedKey}.
     * @return NamespacedKey of target data
     */
    public NamespacedKey getNamespacedKey() {
        return nsKey;
    }
}
