package takumi3s.rcdamagecore.listener;

import io.papermc.paper.persistence.PersistentDataContainerView;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import takumi3s.rcdamagecore.util.LevelUtil;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

public class LevelingListener implements Listener {

    private final Logger logger;

    public LevelingListener(Logger logger) {
        this.logger = logger;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getAction() == InventoryAction.SWAP_WITH_CURSOR || event.getAction() == InventoryAction.HOTBAR_SWAP) {
            // if action performer is not player, ignore this.
            if(!(event.getWhoClicked() instanceof Player player)) return;

            // get slot item
            ItemStack slotStack = event.getCurrentItem();
            if(slotStack == null) return;

            // get cursor item
            ItemStack cursorStack = event.getCursor();
            if(cursorStack.getType() == Material.AIR) return;

            // get pdc view for these stacks
            PersistentDataContainerView slotStackPdcView = slotStack.getPersistentDataContainer();
            PersistentDataContainerView cursorStackPdcView = cursorStack.getPersistentDataContainer();

            // check item max level and is able to level up
            int itemMaxLevel = LevelUtil.MAX_LVL.getOrDefault(slotStackPdcView);
            if(itemMaxLevel == 0) {
                return;
            }

            // get leveling amount and check is it leveling item
            int expAmount = LevelUtil.LVL.getOrDefault(cursorStackPdcView);
            if(expAmount == 0) {
                return;
            }

            // get performer's level to get level limit
            int playerLevel = LevelUtil.LVL.getOrDefault(player.getPersistentDataContainer());
            int maxLevel = Math.min(itemMaxLevel, playerLevel);

            // edit metadata to update level
            slotStack.editMeta(meta -> {
                int nowExp = LevelUtil.EXP.getOrDefault(meta.getPersistentDataContainer());
                int nowLevel = LevelUtil.LVL.getOrDefault(meta.getPersistentDataContainer());
                int count = 0;

                while(slotStack.getAmount() > 0 && nowLevel != maxLevel) {
                    if(nowLevel < maxLevel){
                        // add exp
                        nowExp += expAmount;
                        cursorStack.subtract();

                        // increment loop
                        while (true) {
                            // calc require exp for next level
                            int requireExp = LevelUtil.calcRequireExp(nowLevel + 1);

                            // if nowExp is not enough to level up, exit while loop
                            if (nowExp < requireExp) break;

                            // if reaches max level, skip incrementation.
                            if (nowLevel == maxLevel) break;

                            // if it can level up
                            nowExp -= requireExp;
                            nowLevel++;

                            // counter increment
                            count++;
                        }
                    } else {
                        // decrement loop
                        while (nowLevel > maxLevel) {
                            nowExp += LevelUtil.calcRequireExp(nowLevel);
                            nowLevel--;
                            count--;
                        }

                        break;
                    }
                }

                LevelUtil.EXP.set(meta.getPersistentDataContainer(), nowExp);
                LevelUtil.LVL.set(meta.getPersistentDataContainer(), nowLevel);

//                player.sendMessage(Component.text("Updated! difference: " + count));

                if(cursorStack.getAmount() == 0) {
                    event.setCursor(ItemStack.empty());
                }

                event.setCancelled(true);
            });
        }
    }
}
