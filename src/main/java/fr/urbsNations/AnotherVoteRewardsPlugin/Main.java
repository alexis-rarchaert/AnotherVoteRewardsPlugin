package fr.urbsNations.AnotherVoteRewardsPlugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin  {

    private Map<String, Object> rewards;
    private static Main instance;

    public void onEnable() {
        instance = this;

        loadRewards();

        saveResource("rewards.yml", false);

        getServer().getPluginManager().registerEvents(new VoteEventListener(), this);
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
            // Transformer les codes de couleur (&c, &b, etc.) en codes de format de texte de Bukkit
            String coloredDisplayName = ChatColor.translateAlternateColorCodes('&', displayName);
            meta.setDisplayName(coloredDisplayName);
        }

        List<Map<String, Integer>> enchantmentList = getEnchantments(itemData);
        if (enchantmentList != null) {
            enchantmentList.forEach(enchantMap -> {
                enchantMap.forEach((enchantName, level) -> {
                    Enchantment enchantment = Enchantment.getByName(enchantName);
                    if (enchantment != null) {
                        meta.addEnchant(enchantment, level, true);
                    }
                });
            });
        }


        if (lore != null) {
            // Transformer les codes de couleur dans chaque ligne du lore
            List<String> coloredLore = new ArrayList<>();
            for (String line : lore) {
                coloredLore.add(ChatColor.translateAlternateColorCodes('&', line));
            }
            meta.setLore(coloredLore);
        }

        item.setItemMeta(meta);
        return item;
    }

    public List<Map<String, Object>> getSpecialItems() {
        return (List<Map<String, Object>>) rewards.get("rewards");
    }

    public List<Map<String, Integer>> getEnchantments(Map<String, Object> itemData) {
        return (List<Map<String, Integer>>) itemData.get("enchants");
    }
}
