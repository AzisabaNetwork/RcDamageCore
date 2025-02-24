package takumi3s.rcdamagecore.skill;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ISkillMechanic;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.BukkitAdapter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.Nullable;
import takumi3s.rcdamagecore.RcDamageCore;
import takumi3s.rcdamagecore.util.ItemDataUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RcBuff implements ISkillMechanic, ITargetedEntitySkill {

    public static final Map<UUID, Map<String, Integer>> map = new HashMap<>();
    private final String type;
    private final int duration;
    private final int amount;


    //MMのymlから呼び出し。
    public RcBuff(MythicLineConfig config) {
        type = config.getString(new String[]{"type", "t"}).toLowerCase();
        duration = config.getInteger(new String[]{"dulation", "d"});
        amount = config.getInteger(new String[]{"amount", "a"});
    }

    public static int armorBuff(UUID uuid, String type) {
        NamespacedKey key = new NamespacedKey("rc", type);

        if (!(Bukkit.getEntity(uuid) instanceof LivingEntity entity)) return 0;
        if (entity.getEquipment() == null) return 0;

        EntityEquipment equipment = entity.getEquipment();

        return ItemDataUtil.extractTypeDataFromAll(
                key,
                equipment.getHelmet(),
                equipment.getChestplate(),
                equipment.getLeggings(),
                equipment.getBoots()
        );
    }

    //amountの量のtypeのバフをduration時間する。
    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        try {
            Entity entity = BukkitAdapter.adapt(abstractEntity);
            UUID uuid = entity.getUniqueId();

            if (!map.containsKey(uuid)) map.put(uuid, new HashMap<>());
            map.get(uuid).compute(type, (k, v) -> v == null ? amount : v + amount);

            Bukkit.getScheduler().runTaskLater(RcDamageCore.getInstance(), () -> {
                Map<String, Integer> map1 = isDefined(uuid, type);
                if (map1 == null) return;

                map1.compute(type, (k, v) -> v == null ? 0 : v - amount);

                if (map1.get(type) <= 0) {
                    map.remove(uuid, map1);
                }
            }, duration);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return SkillResult.SUCCESS;
    }

    @Nullable
    public Map<String, Integer> isDefined(UUID uuid, String type) {
        if (map.containsKey(uuid)) {
            if (map.get(uuid).containsKey(type)) {
                return map.get(uuid);
            }
        }
        return null;
    }
}
