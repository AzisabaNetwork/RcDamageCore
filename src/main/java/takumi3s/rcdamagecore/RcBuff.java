package takumi3s.rcdamagecore;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RcBuff implements ISkillMechanic, ITargetedEntitySkill {

    public static final Map<UUID, Map<String, Integer>> map = new HashMap<>();
    private final String type;
    private final int dulation;
    private final int amount;


    //MMのymlから呼び出し。
    public RcBuff(MythicLineConfig config) {
        type = config.getString(new String[]{"type", "t"}).toLowerCase();
        dulation = config.getInteger(new String[]{"dulation", "d"});
        amount = config.getInteger(new String[]{"amount", "a"});
    }

    public static Integer armorBuff(UUID uuid, String type) {
        Integer h = 0;
        Integer c = 0;
        Integer l = 0;
        Integer b = 0;
        if (Bukkit.getEntity(uuid) instanceof LivingEntity e) {
            NamespacedKey nskey = new NamespacedKey("rc", type);
            if (e.getEquipment() == null) return 0;
            EntityEquipment equipment = e.getEquipment();
            ItemStack helmet = equipment.getHelmet();
            if (helmet != null && helmet.hasItemMeta() && helmet.getItemMeta().getPersistentDataContainer().has(nskey, PersistentDataType.INTEGER)) {
                h = helmet.getItemMeta().getPersistentDataContainer().get(nskey, PersistentDataType.INTEGER);
            }
            ItemStack chest = equipment.getChestplate();
            if (chest != null && chest.hasItemMeta() && chest.getItemMeta().getPersistentDataContainer().has(nskey, PersistentDataType.INTEGER)) {
                c = chest.getItemMeta().getPersistentDataContainer().get(nskey, PersistentDataType.INTEGER);
            }
            ItemStack leg = equipment.getLeggings();
            if (leg != null && leg.hasItemMeta() && leg.getItemMeta().getPersistentDataContainer().has(nskey, PersistentDataType.INTEGER)) {
                l = leg.getItemMeta().getPersistentDataContainer().get(nskey, PersistentDataType.INTEGER);
            }
            ItemStack boots = equipment.getBoots();
            if (boots != null && boots.hasItemMeta() && boots.getItemMeta().getPersistentDataContainer().has(nskey, PersistentDataType.INTEGER)) {
                b = boots.getItemMeta().getPersistentDataContainer().get(nskey, PersistentDataType.INTEGER);
            }

        }
        return h + c + l + b;
    }

    //amountの量のtypeのバフをdulation時間する。
    @Override
    public SkillResult castAtEntity(SkillMetadata skillMetadata, AbstractEntity abstractEntity) {
        try {
            Entity entity = BukkitAdapter.adapt(abstractEntity);
            UUID uuid = entity.getUniqueId();
            int int1 = amount;
            if (map.containsKey(uuid)) {
                Map<String, Integer> map1 = map.get(uuid);
                if (map1.containsKey(type)) {
                    int1 += map1.get(type);
                }
                map1.put(type, int1);
            } else {
                Map<String, Integer> map2 = new HashMap<>();
                map2.put(type, int1);
                map.put(uuid, map2);
            }

            Bukkit.getScheduler().runTaskLater(RcDamageCore.getInstance(), () -> {
                Map<String, Integer> map1 = isDefind(uuid, type);
                if (map1 != null) {
                    map1.put(type, map1.get(type) - amount);
                    if (map1.get(type) <= 0) {
                        map.remove(uuid, map1);
                    }
                }
            }, dulation);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return SkillResult.SUCCESS;
    }

    public Map<String, Integer> isDefind(UUID uuid, String type) {
        if (map.containsKey(uuid)) {
            if (map.get(uuid).containsKey(type)) {
                return map.get(uuid);
            }
        }
        return null;
    }
}
