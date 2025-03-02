package takumi3s.rcdamagecore.util;

import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import takumi3s.rcdamagecore.RDCVariables;

public class LevelUtil {
    public static final UsefulKey<Integer> EXP = new UsefulKey<>(RDCVariables.ID, "experience", PersistentDataType.INTEGER, 0);
    public static final UsefulKey<Integer> LVL = new UsefulKey<>(RDCVariables.ID, "level", PersistentDataType.INTEGER, 0);
    public static final UsefulKey<Integer> MAX_LVL = new UsefulKey<>(RDCVariables.ID, "max_level", PersistentDataType.INTEGER, 0);

    /**
     * @param itemPdc {@link PersistentDataContainer} for target item stack
     * @param playerLevel level of player
     * @return level difference amount
     */
    public static int doItemLeveling(PersistentDataContainer itemPdc, int playerLevel) {
        // check item max level and is able to level up
        int itemMaxLevel = MAX_LVL.getOrDefault(itemPdc);
        if(itemMaxLevel == 0) {
            return 0;
        }

        // get some data for leveling
        int nowExp = EXP.getOrDefault(itemPdc);
        int nowLevel = LVL.getOrDefault(itemPdc);

        // try leveling
        LevelData result = tryLeveling(nowExp, nowLevel, itemMaxLevel, playerLevel);

        // store exp and level
        EXP.set(itemPdc, result.exp());
        LVL.set(itemPdc, result.itemLevel());

        // returns level difference
        return result.levelChangeAmount();
    }

    public static LevelData tryLeveling(int nowExp, int nowLevel, int itemMaxLevel, int playerLevel) {
        // get lowest maximum value
        int maxLevel = Math.min(itemMaxLevel, playerLevel);

        if(maxLevel < nowLevel) {
            // if over max level
            return tryItemLevelDown(nowExp, nowLevel, maxLevel);
        } else {
            return tryItemLevelUp(nowExp, nowLevel, maxLevel);
        }
    }

    private static LevelData tryItemLevelUp(
            int nowExp,
            int nowLevel,
            int maxLevel
    ) {
        // increment counter
        int count = 0;

        // level up loop
        while (true) {
            // calc require exp for next level
            int requireExp = calcRequireExp(nowLevel + 1);

            // if nowExp is not enough to level up, exit while loop
            if(nowExp < requireExp) break;

            // if reaches max level, skip incrementation.
            if(nowLevel == maxLevel) break;

            // if it can level up
            nowExp -= requireExp;
            nowLevel++;

            // counter increment
            count++;
        }

        // returns counter value
//        return count;
        return new LevelData(
                nowExp,
                nowLevel,
                count
        );
    }

    private static LevelData tryItemLevelDown(
            int nowExp,
            int nowLevel,
            int maxLevel
    ) {
        // decrement counter
        int count = 0;

        // if now level is overing max level
        while (nowLevel > maxLevel) {
            nowExp += calcRequireExp(nowLevel);
            nowLevel--;
            count--;
        }

        return new LevelData(
                nowExp,
                nowLevel,
                count
        );
    }

    public static int calcRequireExp(int targetLevel) {
        return targetLevel * 2;
    }

//    public static int getExp(PersistentDataContainerView dataContainer) {
//        return dataContainer.getOrDefault(EXPERIENCE, PersistentDataType.INTEGER, 0);
//    }
//
//    public static void addPlayerExp(Player player, int amount) {
//        PersistentDataContainer dataContainer = player.getPersistentDataContainer();
//
//        int nowExp = dataContainer.getOrDefault(EXPERIENCE, PersistentDataType.INTEGER, 0);
//        int newExp = nowExp + amount;
//        dataContainer.set(EXPERIENCE, PersistentDataType.INTEGER, newExp);
//    }
//
//    public static void addItemExp(ItemStack stack, int amount) {
//        stack.editMeta(meta -> {
//            modifyExp(meta.getPersistentDataContainer(), amount, i -> 1000);
//        });
//    }
//
//    private static void modifyExp(PersistentDataContainer dataContainer, int amount, Function<Integer, Integer> nextLevelExpCalc) {
//        int nowExp = dataContainer.getOrDefault(EXPERIENCE, PersistentDataType.INTEGER, 0);
//        int newExp = nowExp + amount;
//
//        int nowLevel = dataContainer.getOrDefault(LEVEL, PersistentDataType.INTEGER, 0);
//        int newLevel = nowLevel;
//
//        int nextLevelRequireExp = nextLevelExpCalc.apply(nowLevel);
//        while(newExp >= nextLevelRequireExp) {
//            newExp -= nextLevelRequireExp;
//            newLevel++;
//            nextLevelRequireExp = nextLevelExpCalc.apply(newLevel);
//        }
//
//        dataContainer.set(EXPERIENCE, PersistentDataType.INTEGER, newExp);
//        dataContainer.set(LEVEL, PersistentDataType.INTEGER, newLevel);
//    }
//
//    private static int calcNextExpForItem(int nowLevel) {
//        return 1000;
//    }
}
