package takumi3s.rcdamagecore.command;

import com.google.gson.GsonBuilder;
import io.lumine.mythic.bukkit.MythicBukkit;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        var gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().create();
        commandSender.sendMessage(Component.text(gson.toJson(MythicBukkit.inst().getItemManager().getItem(strings[0]).get())));
        return true;
    }
}
