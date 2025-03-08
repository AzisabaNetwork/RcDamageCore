package takumi3s.rcdamagecore;

import io.lumine.mythic.api.skills.placeholders.PlaceholderManager;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicReloadedEvent;
import io.lumine.mythic.core.skills.placeholders.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import takumi3s.rcdamagecore.skill.RcBuff;

import java.util.UUID;

public class MMPlaceHolder implements Listener {
    private static final String[] types = new String[]{
            "none",
            "fire",
            "water",
            "wind",
            "lightning",
            "dark",
            "photon",
            "critper",
            "critdmg",
            "nocritdmg"
    };

    @EventHandler
    public static void setMMPlaceHolder(MythicReloadedEvent e) {
        PlaceholderManager pm = MythicBukkit.inst().getPlaceholderManager();

        String buff = "0";
        for(String type: types) {
            buff = phBuffer(type, pm, buff);
        }

//        pm.register("caster.rc.level", Placeholder.meta((meta, s) -> {
//            meta.getTrigger().getItemMainHand();
//        }));

//        return buff;
    }

    public static String phBuffer(String type, PlaceholderManager pm, String buff) {
        // Register placeholder's callback function
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
