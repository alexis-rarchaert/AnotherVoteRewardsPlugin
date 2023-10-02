package fr.urbsNations.AnotherVoteRewardsPlugin;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin  {

    private Map<String, Object> rewards;
    private RewardDataManager rewardDataManager;
    private static Main instance;

    public void onEnable() {
        instance = this;
        rewardDataManager = new RewardDataManager(this);

        loadRewards();

        saveResource("rewards.yml", false);

        getServer().getPluginManager().registerEvents(new VoteEventListener(rewardDataManager), this);
        System.out.println("[AnotherVoteRewardsPlugin] Plugin started");
    }

    public static Main getInstance() {
        return instance;
    }

    public Map<String, Object> getRewards() {
        return rewards;
    }

    private void loadRewards() {
        Yaml yaml = new Yaml();

        InputStream rewardsYml = getResource("rewards.yml");
        rewards = yaml.load(rewardsYml);
        System.out.println(rewards);
    }

    public ItemStack createUniqueItem(Map<String, Object> itemData){
        Material material = Material.valueOf((String) itemData.get("material"));
        int amount = (int) itemData.get("amount");
        String displayName = (String) itemData.get("name");
        List<String> lore = (List<String>) itemData.get("lore");

        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (displayName != null) {
            meta.setDisplayName(displayName);
        }

        Map<String, Integer> enchantments = (Map<String, Integer>) itemData.get("enchants");
        if (enchantments != null) {
            enchantments.forEach((enchantName, level) -> {
                Enchantment enchantment = Enchantment.getByName(enchantName);
                if (enchantment != null) {
                    meta.addEnchant(enchantment, level, true);
                }
            });
        }

        if (lore != null) {
            meta.setLore(lore);
        }

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSpecialItem(String rewardKey) {
        Map<String, Object> rewardData = (Map<String, Object>) rewards.get("rewards");
        Map<String, Object> itemData = (Map<String, Object>) rewardData.get(rewardKey);
        if (itemData != null) {
            return createUniqueItem(itemData);
        }
        return null;
    }
}
