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
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;
import java.util.UUID;

public class RcDamageListener implements Listener {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e) {
        double damage = e.getDamage();
        UUID uuid = e.getDamager().getUniqueId();


        //ダメージ受けた人の防具値防具強度値取得する。
        double armorToughness = 0;
        double armor = 0;
        double armorEc = 0;

        if (e.getEntity() instanceof LivingEntity damaged) {
            if (damaged.getAttribute(Attribute.GENERIC_ARMOR) != null) {
                armor = Objects.requireNonNull(damaged.getAttribute(Attribute.GENERIC_ARMOR)).getValue();
            }
            if (damaged.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS) != null) {
                armorToughness = Objects.requireNonNull(damaged.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS)).getValue();
            }
            if (damaged.getEquipment().getHelmet() != null) {
                armorEc += damaged.getEquipment().getHelmet().getEnchantmentLevel(Enchantment.PROTECTION);
            }
            if (damaged.getEquipment().getChestplate() != null) {
                armorEc += damaged.getEquipment().getChestplate().getEnchantmentLevel(Enchantment.PROTECTION);
            }
            if (damaged.getEquipment().getLeggings() != null) {
                armorEc += damaged.getEquipment().getLeggings().getEnchantmentLevel(Enchantment.PROTECTION);
            }
            if (damaged.getEquipment().getBoots() != null) {
                armorEc += damaged.getEquipment().getBoots().getEnchantmentLevel(Enchantment.PROTECTION);
            }
        }

        //ダメージ与えた人の攻撃力取得する。
        double attack = 0;

        if (e.getDamager() instanceof LivingEntity damager) {
            if (damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE) != null) {
                attack = Objects.requireNonNull(damager.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE)).getValue();
            }
        }

        //ダメージ受けた人の耐性効果を取得する。
        double resistance = 0;
        if (e.getEntity() instanceof LivingEntity damaged) {
            if (damaged.hasPotionEffect(PotionEffectType.RESISTANCE)) {
                resistance = (Objects.requireNonNull(damaged.getPotionEffect(PotionEffectType.RESISTANCE)).getAmplifier() + 1);
            }
        }

        //クリティカルの計算。
        int critValue = (int) (Math.random() * 1000);
        int critPer = 10 + RcBuff.armorBuff(uuid, "critper");
        double critDamage = 5 + RcBuff.armorBuff(uuid, "critdmg");
        Bukkit.broadcast(Component.text(critDamage));
        if (RcBuff.map.containsKey(uuid)) {
            if (RcBuff.map.get(uuid).containsKey("critper")) {
                critPer += RcBuff.map.get(uuid).get("critper");
            }
        }
        if (RcBuff.map.containsKey(uuid)) {
            if (RcBuff.map.get(uuid).containsKey("critdmg")) {
                critDamage += RcBuff.map.get(uuid).get("critdmg");
            }
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
        } else {
            e.setDamage(0.0);
            e.setDamage(EntityDamageEvent.DamageModifier.ARMOR, finalDamage);
        }

    }

}
