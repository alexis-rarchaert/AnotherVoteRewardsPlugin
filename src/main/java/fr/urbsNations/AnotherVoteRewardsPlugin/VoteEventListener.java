package fr.urbsNations.AnotherVoteRewardsPlugin;

import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VoteEventListener implements Listener {
    private final RewardDataManager rewardDataManager;

    public VoteEventListener(RewardDataManager rewardDataManager) {
        this.rewardDataManager = rewardDataManager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Player player = Bukkit.getPlayer(event.getEventName());

        if(player != null && player.isOnline()) {
            Map<String, Object> rewardsData = (Map<String, Object>) Main.getInstance().getRewards().get("rewards");

            // Obtenir un palier de récompense aléatoire
            List<String> rewardKeys = new ArrayList<>(rewardsData.keySet());
            String randomRewardKey = rewardKeys.get(new Random().nextInt(rewardKeys.size()));

            // Obtenir l'item spécial associé au palier de récompense aléatoire
            ItemStack rewardItem = Main.getInstance().getSpecialItem(randomRewardKey);

            // Donner l'item spécial au joueur
            if (rewardItem != null) {
                player.getInventory().addItem(rewardItem);
                player.sendMessage("Félicitations ! Vous avez reçu une récompense aléatoire pour votre vote !");
            } else {
                player.sendMessage("Désolé, une erreur s'est produite lors de la récupération de votre récompense.");
            }
        } else {
            UUID playerId = UUID.fromString(event.getEventName());
            Map<String, Object> rewardsData = Main.getInstance().getRewards();
            List<String> rewardKeys = new ArrayList<>(rewardsData.keySet());
            String randomRewardKey = rewardKeys.get(new Random().nextInt(rewardKeys.size()));
            rewardDataManager.addPendingReward(playerId, randomRewardKey);
        }
    }
}
