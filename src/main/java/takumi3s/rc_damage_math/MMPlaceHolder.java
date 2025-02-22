package takumi3s.rc_damage_math;

import io.lumine.mythic.api.skills.placeholders.PlaceholderManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import io.r2dbc.spi.Parameter;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

public class MMPlaceHolder implements Listener {
      @EventHandler
      public static String MMPlaceHolder(MythicReloadedEvent e) {
          PlaceholderManager pm = MythicBukkit.inst().getPlaceholderManager();
          String buff = "0";
          buff = phBuffer("none",pm,buff);
          buff = phBuffer("fire",pm,buff);
          buff = phBuffer("ice",pm,buff);
          buff = phBuffer("wind",pm,buff);
          buff = phBuffer("lightning",pm,buff);
          buff = phBuffer("rekketu",pm,buff);
          buff = phBuffer("syukusei",pm,buff);
          buff = phBuffer("critper",pm,buff);
          buff = phBuffer("critdmg",pm,buff);
          return buff;
      }

      public static String phBuffer(String type, PlaceholderManager pm,String buff){

          pm.register("caster.buff."+type, Placeholder.meta((meta, s) -> {
              UUID uuid = meta.getTrigger().getUniqueId();
              String[] sp = s.split("\\.");
              if (RcBuff.map.containsKey(uuid) && RcBuff.map.get(uuid).containsKey(type)) {
                  return String.valueOf(RcBuff.map.get(uuid).get(type)+RcBuff.armorBuff(uuid,type));
              }else {
                  return buff;
              }
          }));
          return buff;
      }

}
