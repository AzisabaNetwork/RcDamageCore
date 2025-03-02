package takumi3s.rcdamagecore.config;

import org.bukkit.configuration.ConfigurationSection;

public class RDCConfig {
    public static int playerMaxLevel = 0;

    public static void loadConfig(ConfigurationSection section) {
        playerMaxLevel = section.getInt("playerMaxLevel", 10);
    }
}
