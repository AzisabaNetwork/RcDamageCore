package takumi3s.rcdamagecore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicDamageEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffectType;
import takumi3s.rcdamagecore.util.ArmorDataUtil;
import takumi3s.rcdamagecore.util.AttributeUtil;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Logger;

public class RcDamageListener implements Listener {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final Logger logger;
    public RcDamageListener(Logger logger) {
        this.logger = logger;
    }

//    @EventHandler
//    public void onMythicDamage(MythicDamageEvent e) {
//        logger.info("Parsed damage meta: " + gson.toJson(e.getDamageMetadata()));
//        logger.info(e.getDamageMetadata().toString());
//    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDamaged(MythicDamageEvent e) {
        // ダメージを与えた人のデータ取得
        double attack = 0;
        String mmType = "none";

        var abstractDamager = e.getCaster().getEntity();

        if (abstractDamager instanceof LivingEntity damager) {
            // 攻撃力
            attack = AttributeUtil.getOrZero(damager, Attribute.GENERIC_ATTACK_DAMAGE);
            EntityEquipment equipment = damager.getEquipment();
            if(equipment != null) {
                mmType = e.getDamageMetadata().getElement();
            }
        }

        double damage = e.getDamage();
        UUID uuid = abstractDamager.getUniqueId();


        // ダメージを受けた人のデータ取得
        double armorToughness = 0;
        double armor = 0;
        double armorEc = 0;
        double resistance = 0;

        if (e.getTarget() instanceof LivingEntity damaged) {
            armor = AttributeUtil.getOrZero(damaged, Attribute.GENERIC_ARMOR);
            armorToughness = AttributeUtil.getOrZero(damaged, Attribute.GENERIC_ARMOR_TOUGHNESS);

            EntityEquipment equipment = damaged.getEquipment();
            if (equipment != null) {
                armorEc += ArmorDataUtil.getProtectionValueFromAll(
                        equipment.getHelmet(),
                        equipment.getChestplate(),
                        equipment.getLeggings(),
                        equipment.getBoots()
                );
            }

            // 耐性効果
            if (damaged.hasPotionEffect(PotionEffectType.RESISTANCE)) {
                resistance = (Objects.requireNonNull(damaged.getPotionEffect(PotionEffectType.RESISTANCE)).getAmplifier() + 1);
            }
        }

        //クリティカルの計算。
        int critValue = (int) (Math.random() * 1000);
        int critPer = 10 + RcBuff.armorBuff(uuid, "critper");
        double critDamage = 5 + RcBuff.armorBuff(uuid, "critdmg");

        // 全体へダメージ値を送信
        Bukkit.broadcast(Component.text(String.format("Damage: %.2f (%s)", critDamage, mmType)));

        // バフがある場合
        if (RcBuff.map.containsKey(uuid)) {
            critPer += RcBuff.map.get(uuid).getOrDefault("critper", 0);
            critDamage += RcBuff.map.get(uuid).getOrDefault("critdmg", 0);
        }

        if (critPer >= critValue) {
            damage = damage * (1 + (critDamage / 1000));
        }

        //属性バフの計算
        Bukkit.broadcast(Component.text("testtttt").clickEvent(ClickEvent.copyToClipboard(new Gson().toJson(MythicBukkit.inst().getItemManager().getItem(mmType).get()))));
        int buff = 0;
        if (RcBuff.map.containsKey(uuid)) {
            if (RcBuff.map.containsKey(mmType)) {
                buff = RcBuff.map.get(uuid).get(mmType);
                damage = damage * (1 + ((double) buff / 1000));
            }
        }
        damage = damage*(1+((double) buff /1000));

        //エンチャントバフの計算

        //最終的なダメージの計算式
        double finalDamage = (damage * (1 - armorToughness * 0.01) * (1 - armorEc / 128) + (attack - 1 - armor)) * (1 - resistance / 10);

        if (finalDamage <= 0) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(0.0);
        e.setDamage(finalDamage);

    }

}
