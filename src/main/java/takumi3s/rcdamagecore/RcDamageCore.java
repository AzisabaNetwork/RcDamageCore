package takumi3s.rcdamagecore;

import net.azisaba.loreeditor.api.event.EventBus;
import net.azisaba.loreeditor.api.event.ItemEvent;
import net.azisaba.loreeditor.libs.net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import takumi3s.rcdamagecore.command.TestCommand;
import takumi3s.rcdamagecore.listener.LevelingListener;
import takumi3s.rcdamagecore.listener.MythicListener;
import takumi3s.rcdamagecore.listener.RcDamageListener;
import takumi3s.rcdamagecore.util.LevelUtil;


public final class RcDamageCore extends JavaPlugin {
    public static final String ID = "rcdamagecore";

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
        pm.registerEvents(new LevelingListener(getLogger()), this);

        ServerCommandEvent e = new ServerCommandEvent(Bukkit.getConsoleSender(), "mythicmobs reload");
        if (e.callEvent()) {
            Bukkit.dispatchCommand(e.getSender(), e.getCommand());
        }

        getCommand("testcmd").setExecutor(new TestCommand());

        getLogger().info("mm reloadしたよ！！！！！！！");

        EventBus.INSTANCE.register(this, ItemEvent.class, 0, event -> {
            ItemStack stack = event.getBukkitItem();
            int level = stack.getPersistentDataContainer().getOrDefault(LevelUtil.LEVEL, PersistentDataType.INTEGER, -1);
            if(level > 0) {
                event.addLore(Component.text("武器レベル: " + level));
            }
        });
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
