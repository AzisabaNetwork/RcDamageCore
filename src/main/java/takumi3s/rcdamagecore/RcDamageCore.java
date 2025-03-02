package takumi3s.rcdamagecore;

import co.aikar.commands.PaperCommandManager;
import net.azisaba.loreeditor.api.event.EventBus;
import net.azisaba.loreeditor.api.event.ItemEvent;
import net.azisaba.loreeditor.libs.net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import takumi3s.rcdamagecore.command.ExpCommand;
import takumi3s.rcdamagecore.command.TestCommand;
import takumi3s.rcdamagecore.config.RDCConfig;
import takumi3s.rcdamagecore.listener.LevelingListener;
import takumi3s.rcdamagecore.listener.MythicListener;
import takumi3s.rcdamagecore.listener.RcDamageListener;
import takumi3s.rcdamagecore.util.LevelUtil;

import java.util.Objects;


public final class RcDamageCore extends JavaPlugin {
    private static RcDamageCore instance;

    public static RcDamageCore getInstance() {
        return instance;
    }

    private PaperCommandManager commandManager;

    public RcDamageCore() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new ExpCommand(this));

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
            int level = LevelUtil.LVL.getOrDefault(stack.getPersistentDataContainer());
            if (level > 0) {
                event.addLore(Component.text("武器レベル: " + level));
            }
        });
        saveDefaultConfig();
        RDCConfig.loadConfig(Objects.requireNonNull(getConfig().getConfigurationSection("leveling")));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        commandManager.unregisterCommands();
    }
}
