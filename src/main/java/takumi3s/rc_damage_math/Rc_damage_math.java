package takumi3s.rc_damage_math;
import com.comphenix.protocol.PacketType;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.FileNotFoundException;
import java.io.PrintWriter;


public final class Rc_damage_math extends JavaPlugin {

    private static Rc_damage_math main;
    public Rc_damage_math() {main=this;}
    public static Rc_damage_math getInstance() {return main;}

    @Override
    public void onEnable() {
        // Plugin startup logic
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new RcDamageListener(),this);
        //pm.registerEvents(new RcDamageViewer(),this);
        pm.registerEvents(new MythicListener(),this);
        pm.registerEvents(new MMPlaceHolder(),this);
        Bukkit.getLogger().info("mm reloadしたよ！！！！！！！");
        ServerCommandEvent e = new ServerCommandEvent(Bukkit.getConsoleSender(),"mythicmobs reload");
        if (e.callEvent()) {
            Bukkit.dispatchCommand(e.getSender(), e.getCommand());
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
