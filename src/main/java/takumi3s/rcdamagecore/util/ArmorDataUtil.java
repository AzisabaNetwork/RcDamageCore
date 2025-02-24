package takumi3s.rcdamagecore.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class ArmorDataUtil {
    public static int getProtectionValue(@Nullable ItemStack stack) {
        if(stack == null) return 0;
        return stack.getEnchantmentLevel(Enchantment.PROTECTION);
    }

    public static int getProtectionValueFromAll(@Nullable ItemStack ...stacks) {
        return Arrays.stream(stacks).mapToInt(ArmorDataUtil::getProtectionValue).sum();
    }
}
