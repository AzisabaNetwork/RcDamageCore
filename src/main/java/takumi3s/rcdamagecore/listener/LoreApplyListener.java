package takumi3s.rcdamagecore.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import takumi3s.rcdamagecore.util.LevelUtil;

import java.util.ArrayList;
import java.util.List;

public class LoreApplyListener extends PacketAdapter {
    public static final String LEVEL_PREFIX = "武器レベル: ";

    public LoreApplyListener(Plugin plugin) {
        super(
                plugin,
                ListenerPriority.LOWEST,
                PacketType.Play.Server.SET_SLOT,
                PacketType.Play.Server.WINDOW_ITEMS
        );
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
            PacketContainer packet = event.getPacket().deepClone();
            StructureModifier<ItemStack> sm = packet.getItemModifier();
            for (int i = 0; i < sm.size(); i++) {
                if (sm.getValues().get(i) != null && sm.getValues().get(i).getType() != Material.AIR) {
                    ItemStack item = sm.getValues().get(i);
                    addLevelLore(item);
                }
            }
            event.setPacket(packet);
        }
        if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
            PacketContainer packet = event.getPacket().deepClone();
            StructureModifier<ItemStack[]> sm = packet.getItemArrayModifier();

            for (ItemStack[] items: sm.getValues()) {
                if (items == null) continue;

                for (ItemStack item : items) {
                    addLevelLore(item);
                }
            }
            event.setPacket(packet);
        }
    }

    private ItemStack addLevelLore(@NotNull ItemStack originalStack) {
        ItemStack targetStack = originalStack.clone();

        PersistentDataContainerView pdc = targetStack.getPersistentDataContainer();
        if (pdc.has(LevelUtil.LEVEL)) {
            int level = pdc.getOrDefault(LevelUtil.LEVEL, PersistentDataType.INTEGER, -1);
            if (level <= 0) {
                return originalStack;
            }

            List<Component> loreList = targetStack.lore();
            Component newComponent = Component.text(LEVEL_PREFIX + level);

            // flag to check level lore appended.
            boolean appended = false;

            // if lore is not available
            if (loreList == null) {
                loreList = new ArrayList<>();
            }

            // try to find level lore
            for (int i = 0; i < loreList.size(); i++) {
                var comp = loreList.get(i);
                if (PlainTextComponentSerializer.plainText().serialize(comp).startsWith(LEVEL_PREFIX)) {
                    // set new level lore
                    loreList.set(i, newComponent);
                    appended = true;
                    break;
                }
            }

            if(!appended){
                // if no level lore appended, add new level lore at last.
                loreList.add(newComponent);
            }

            targetStack.lore(loreList);
        }
        return targetStack;
    }
}
