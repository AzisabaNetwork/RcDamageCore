package takumi3s.rcdamagecore;

import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import takumi3s.rcdamagecore.command.TestCommand;
import takumi3s.rcdamagecore.listener.MythicListener;
import takumi3s.rcdamagecore.listener.RcDamageListener;


public final class RcDamageCore extends JavaPlugin {
    private static RcDamageCore instance;

    public RcDamageCore() {
        instance = this;
    }

    public static RcDamageCore getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new RcDamageListener(getLogger()), this);
        //pm.registerEvents(new RcDamageViewer(),this);
        pm.registerEvents(new MythicListener(), this);
        pm.registerEvents(new MMPlaceHolder(), this);

        ServerCommandEvent e = new ServerCommandEvent(Bukkit.getConsoleSender(), "mythicmobs reload");
        if (e.callEvent()) {
            Bukkit.dispatchCommand(e.getSender(), e.getCommand());
        }

        getCommand("testcmd").setExecutor(new TestCommand());

        getLogger().info("mm reloadしたよ！！！！！！！");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
