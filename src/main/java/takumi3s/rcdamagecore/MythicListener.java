package takumi3s.rcdamagecore;

import io.lumine.mythic.bukkit.events.MythicMechanicLoadEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MythicListener implements Listener {

    //ばふのMM実行したときの処理。
    @EventHandler
    public void onLoad(MythicMechanicLoadEvent e) {
        String s = e.getMechanicName();
        if (s.equalsIgnoreCase("rcbuff")) {
            e.register(new RcBuff(e.getConfig()));
        }
    }

    //はいったときりせっと。
    @EventHandler
    public void onJoind(PlayerJoinEvent e) {
        RcBuff.map.remove(e.getPlayer().getUniqueId());
    }
}
