package takumi3s.rcdamagecore;

import io.lumine.mythic.api.skills.placeholders.PlaceholderManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class MMPlaceHolder implements Listener {
    private static final String[] types = new String[]{
            "none",
            "fire",
            "ice",
            "wind",
            "lightning",
            "rekketu",
            "syukusei",
            "critper",
            "critdmg"
    };

    @EventHandler
    public static String setMMPlaceHolder(MythicReloadedEvent e) {
        PlaceholderManager pm = MythicBukkit.inst().getPlaceholderManager();

        String buff = "0";
        for(String type: types) {
            buff = phBuffer(type, pm, buff);
        }

        return buff;
    }

    public static String phBuffer(String type, PlaceholderManager pm, String buff) {
        pm.register("caster.buff." + type, Placeholder.meta((meta, s) -> {
            UUID uuid = meta.getTrigger().getUniqueId();
            if (RcBuff.map.containsKey(uuid) && RcBuff.map.get(uuid).containsKey(type)) {
                return String.valueOf(RcBuff.map.get(uuid).get(type) + RcBuff.armorBuff(uuid, type));
            } else {
                return buff;
            }
        }));

        return buff;
    }

}
