package takumi3s.rcdamagecore.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import takumi3s.rcdamagecore.util.LevelUtil;

import java.util.ArrayList;
import java.util.List;

public class LoreApplyListener extends PacketAdapter {
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
                    PersistentDataContainerView pdc = item.getPersistentDataContainer();
                    if (pdc.has(LevelUtil.LEVEL)) {
                        int level = pdc.getOrDefault(LevelUtil.LEVEL, PersistentDataType.INTEGER, -1);
                        if (level <= 0) {
                            continue;
                        }

                        List<Component> loreList = item.lore();
                        if (loreList == null) loreList = new ArrayList<>();
                        loreList.add(Component.text("武器レベル: " + level));
                        item.lore(loreList);
                    }
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
                    if(item == null) continue;

                    PersistentDataContainerView pdc = item.getPersistentDataContainer();
                    if (pdc.has(LevelUtil.LEVEL)) {
                        int level = pdc.getOrDefault(LevelUtil.LEVEL, PersistentDataType.INTEGER, -1);
                        if (level <= 0) {
                            continue;
                        }

                        List<Component> loreList = item.lore();
                        if (loreList == null) loreList = new ArrayList<>();
                        loreList.add(Component.text("武器レベル: " + level));
                        item.lore(loreList);
                    }
                }
            }
            event.setPacket(packet);
        }
    }
}
