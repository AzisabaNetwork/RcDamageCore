package takumi3s.rcdamagecore;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.potion.PotionEffectType;
import takumi3s.rcdamagecore.util.ArmorDataUtil;
import takumi3s.rcdamagecore.util.AttributeUtil;

import java.util.Objects;
import java.util.UUID;

public class RcDamageListener implements Listener {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e) {
        double damage = e.getDamage();
        UUID uuid = e.getDamager().getUniqueId();


        // ダメージを受けた人のデータ取得
        double armorToughness = 0;
        double armor = 0;
        double armorEc = 0;
        double resistance = 0;

        if (e.getEntity() instanceof LivingEntity damaged) {
            armor = AttributeUtil.getOrZero(damaged, Attribute.GENERIC_ARMOR);
            armorToughness = AttributeUtil.getOrZero(damaged, Attribute.GENERIC_ARMOR_TOUGHNESS);

            EntityEquipment equipment = damaged.getEquipment();
            if(equipment != null) {
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

        // ダメージを与えた人のデータ取得
        double attack = 0;

        if (e.getDamager() instanceof LivingEntity damager) {
            // 攻撃力
            attack = AttributeUtil.getOrZero(damager, Attribute.GENERIC_ATTACK_DAMAGE);
        }

        //クリティカルの計算。
        int critValue = (int) (Math.random() * 1000);
        int critPer = 10 + RcBuff.armorBuff(uuid, "critper");
        double critDamage = 5 + RcBuff.armorBuff(uuid, "critdmg");

        // 全体へダメージ値を送信
        Bukkit.broadcast(Component.text(critDamage));

        // バフがある場合
        if (RcBuff.map.containsKey(uuid)) {
            critPer += RcBuff.map.get(uuid).getOrDefault("critper", 0);
            critDamage += RcBuff.map.get(uuid).getOrDefault("critdmg", 0);
        }

        if (critPer >= critValue) {
            damage = damage * (1 + (critDamage / 1000));
        }

        //属性バフの計算
        //EntityDamageEvent.@NotNull DamageCause damagetype = e.getCause();
        //int buff = 0;
        //if(RcBuff.map.containsKey(uuid)){
        //    if(RcBuff.map.containsKey(damagetype)){
        //        buff = RcBuff.map.get(uuid).get(damagetype);
        //        }
        //    }
        //damage = damage*(1+(buff/1000));

        //エンチャントバフの計算

        //最終的なダメージの計算式
        double finalDamage = (damage * (1 - armorToughness * 0.01) * (1 - armorEc / 128) + (attack - 1 - armor)) * (1 - resistance / 10);

        if (finalDamage <= 0) {
            e.setCancelled(true);
            return;
        }

        e.setDamage(0.0);
        e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, finalDamage);

    }

}
