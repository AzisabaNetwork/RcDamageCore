package takumi3s.rcdamagecore.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import takumi3s.rcdamagecore.RcDamageCore;
import takumi3s.rcdamagecore.util.LevelUtil;

@CommandAlias("rcexp")
public class ExpCommand extends BaseCommand {
    private static final int MAX_LEVEL = 10000;

    private final RcDamageCore plugin;

    public ExpCommand(RcDamageCore plugin) {
        this.plugin = plugin;
    }

    @Default
    public void onDefault(CommandSender sender) {
        sender.sendMessage("fmm... there is nothing...");
    }

    @Subcommand("give")
    @CommandCompletion("@players @range:0-100")
    @CommandPermission("rcdamagecore.cmd.rcexp.give")
    public void giveExp(CommandSender sender, String playerName, @Default("0") int amount) {
        Player player = plugin.getServer().getPlayer(playerName);
        if (player == null) {
            sender.sendMessage(Component.text("Player not found. mcid: " + playerName));
            return;
        }

        if (amount <= 0) {
            sender.sendMessage(Component.text("経験値量は1以上の整数である必要があります。"));
            return;
        }

        PersistentDataContainer dataContainer = player.getPersistentDataContainer();

        LevelUtil.EXP.computeWithDefault(dataContainer, v -> v + amount);

        int nowExp = LevelUtil.EXP.getOrDefault(dataContainer);
        int nowLevel = LevelUtil.LVL.getOrDefault(dataContainer);
        int count = 0;

        if (nowLevel < MAX_LEVEL) {
            // increment loop
            while (true) {
                // calc require exp for next level
                int requireExp = LevelUtil.calcRequireExp(nowLevel + 1);

                // if nowExp is not enough to level up, exit while loop
                if (nowExp < requireExp) break;

                // if reaches max level, skip incrementation.
                if (nowLevel == MAX_LEVEL) break;

                // if it can level up
                nowExp -= requireExp;
                nowLevel++;

                // counter increment
                count++;
            }
        }

        LevelUtil.EXP.set(dataContainer, nowExp);
        LevelUtil.LVL.set(dataContainer, nowLevel);

        player.sendMessage(Component.text("Updated! difference: " + count));

        sender.sendMessage(Component.text(String.format("%sの経験値を%d増やしました。", playerName, amount)));
    }
}
