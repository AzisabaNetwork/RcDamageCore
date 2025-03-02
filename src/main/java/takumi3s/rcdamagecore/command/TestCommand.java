package takumi3s.rcdamagecore.command;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import takumi3s.rcdamagecore.util.LevelUtil;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
//        var gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
//        commandSender.sendMessage(Component.text(gson.toJson(MythicBukkit.inst().getItemManager().getItem(strings[0]).get())));
        if(commandSender instanceof Player player) {
            player.getEquipment().getItemInMainHand().editMeta(meta -> {
//                LevelUtil.IS_LVL_ITEM.set(meta.getPersistentDataContainer(), true);
                LevelUtil.LVL.set(meta.getPersistentDataContainer(), 10);
                LevelUtil.MAX_LVL.set(meta.getPersistentDataContainer(), 20);
//                meta.getPersistentDataContainer().set(LevelUtil.IS_LEVELING_ITEM, PersistentDataType.BOOLEAN, true);
//                meta.getPersistentDataContainer().set(LevelUtil.LEVEL, PersistentDataType.INTEGER, 10);
//                meta.getPersistentDataContainer().set(LevelUtil.MAX_LEVEL, PersistentDataType.INTEGER, 20);
            });
            commandSender.sendMessage(Component.text("Success.").color(NamedTextColor.GREEN));
        } else {
            commandSender.sendMessage("Failure.");
        }
        return true;
    }
}
