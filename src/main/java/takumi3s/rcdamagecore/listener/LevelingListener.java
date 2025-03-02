package takumi3s.rcdamagecore.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import takumi3s.rcdamagecore.util.LevelUtil;

import java.util.logging.Logger;

public class LevelingListener implements Listener {

    private final Logger logger;

    public LevelingListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR || event.getAction() == InventoryAction.HOTBAR_SWAP) {
            ItemStack oldStack = event.getCurrentItem();
            if(oldStack == null) return;

            ItemStack newStack = event.getCursor();

            // get maximum level and validate
//            int maxLevel = oldStack.getPersistentDataContainer().getOrDefault(LevelUtil.MAX_LEVEL, PersistentDataType.INTEGER, -1);
            int maxLevel = LevelUtil.MAX_LVL.getOrDefault(oldStack.getPersistentDataContainer());
            if (maxLevel <= 0) {
                // ignore item
                return;
            }

//            boolean isLevelingItem = newStack.getPersistentDataContainer().getOrDefault(LevelUtil.IS_LEVELING_ITEM, PersistentDataType.BOOLEAN, false);
//            boolean isLevelingItem = LevelUtil.IS_LVL_ITEM.getOrDefault(newStack.getPersistentDataContainer());
//            if (!isLevelingItem) {
//                // ignore item
//                return;
//            }

            // get now level
//            int nowLevel = oldStack.getPersistentDataContainer().getOrDefault(LevelUtil.LEVEL, PersistentDataType.INTEGER, 0);

            // edit metadata to increment level
            oldStack.editMeta(meta -> {
                LevelUtil.MAX_LVL.computeWithDefault(meta.getPersistentDataContainer(), i -> Math.min(i + 1, maxLevel));
//                meta.getPersistentDataContainer().set(LevelUtil.LEVEL, PersistentDataType.INTEGER, Math.min(nowLevel + 1, maxLevel));
            });

            event.setCursor(ItemStack.empty());

            event.setCancelled(true);
        }
    }
}
