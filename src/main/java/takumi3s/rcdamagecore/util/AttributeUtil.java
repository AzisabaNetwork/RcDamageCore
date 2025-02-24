package takumi3s.rcdamagecore.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class AttributeUtil {
    public static double getOrZero(@NotNull LivingEntity entity, @NotNull Attribute targetAttribute) {
        return getOrDefault(entity, targetAttribute, 0);
    }

    public static double getOrDefault(@NotNull LivingEntity entity, @NotNull Attribute targetAttribute, double defaultValue) {
        AttributeInstance attribute = entity.getAttribute(targetAttribute);
        if(attribute != null) {
            return attribute.getValue();
        }
        return defaultValue;
    }
}
