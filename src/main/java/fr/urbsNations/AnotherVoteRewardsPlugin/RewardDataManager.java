package fr.urbsNations.AnotherVoteRewardsPlugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RewardDataManager {
    private final Main plugin;
    private final FileConfiguration dataConfig;
    private final Map<UUID, String> pendingRewards;

    public RewardDataManager(Main plugin) {
        this.plugin = plugin;
        this.dataConfig = plugin.getConfig();
        this.pendingRewards = new HashMap<>();
    }

    public void addPendingReward(UUID playerId, String rewardKey) {
        pendingRewards.put(playerId, rewardKey);
    }

    public void givePendingReward(Player player) {
        UUID playerId = player.getUniqueId();
        if(pendingRewards.containsKey(playerId)) {
            String rewardKey = pendingRewards.get(playerId);
            ItemStack rewardItem = Main.getInstance().getSpecialItem(rewardKey);

            if(rewardKey != null) {
                player.getInventory().addItem(rewardItem);
                player.sendMessage("You're lucky today !");
                pendingRewards.remove(playerId);
            } else {
                player.sendMessage("Error. Please try again.");
            }
        }
    }
}
