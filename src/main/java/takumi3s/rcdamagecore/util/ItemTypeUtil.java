package takumi3s.rcdamagecore.util;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ItemTypeUtil {
    public static int extractTypeData(@NotNull NamespacedKey key, @Nullable ItemStack stack) {
        if(stack != null && stack.hasItemMeta()) {
            return stack.getItemMeta()
                    .getPersistentDataContainer()
                    .getOrDefault(key, PersistentDataType.INTEGER, 0);
        }
        return 0;
    }

    public static int extractTypeDataFromAll(@NotNull NamespacedKey key, @Nullable ItemStack...stacks) {
        return Arrays.stream(stacks).mapToInt(s -> extractTypeData(key, s)).sum();
    }
}
