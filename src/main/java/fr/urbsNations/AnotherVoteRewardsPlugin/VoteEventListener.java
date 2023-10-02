package fr.urbsNations.AnotherVoteRewardsPlugin;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class VoteEventListener implements Listener {

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVotifierEvent(VotifierEvent event) {
        Player player = Bukkit.getPlayer(event.getVote().getUsername());

        if(player != null && player.isOnline()) {
            List<Map<String, Object>> specialItems = Main.getInstance().getSpecialItems();

            if (specialItems != null && !specialItems.isEmpty()) {
                Map<String, Object> randomRewardData = specialItems.get(new Random().nextInt(specialItems.size()));

                ItemStack rewardItem = Main.getInstance().createUniqueItem(randomRewardData);

                if (rewardItem != null) {
                    player.getInventory().addItem(rewardItem);
                    player.sendMessage("Félicitations ! Vous avez reçu une récompense aléatoire pour votre vote !");
                } else {
                    player.sendMessage("Désolé, une erreur s'est produite lors de la récupération de votre récompense.");
                }
            }
        } else {
            System.out.println(event.getVote().getUsername() + " à voté, mais a pas perdu sa récompense car il n'était pas connecté !");
        }
    }
}
