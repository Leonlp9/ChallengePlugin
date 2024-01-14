package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.Objects;

@LoadGamerule
public class Skips extends GameRule {

    @ConfigurationValue(title = "AmountOfSkips", min = 0, max = 100, icon = Material.ACACIA_BUTTON)
    private int amountOfSkips = 1;

    private transient ItemStack skipItem = new ItemBuilder(Material.BARRIER).setDisplayName("§cSkip").addPersistentDataContainer("skip", PersistentDataType.STRING, "skip").build();
    private transient List<Player> cooldown = new java.util.ArrayList<>();

    public Skips() {
        super(Material.BARRIER);
    }

    @Override
    public void timerFirstTimeResume() {
        super.timerFirstTimeResume();

        if (isEnabled()) {
            skipItem.setAmount(amountOfSkips);
            Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(skipItem));
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerInteractEvent event) {
        if (event.getItem() == null) return;
        if (event.getItem().getItemMeta() == null) return;
        if (event.getItem().getItemMeta().getPersistentDataContainer() == null) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "skip"), PersistentDataType.STRING)) return;
        if (!event.getItem().getItemMeta().getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "skip"), PersistentDataType.STRING).equals("skip")) return;

        if (cooldown.contains(event.getPlayer())) return;
        event.setCancelled(true);
        cooldown.add(event.getPlayer());
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> cooldown.remove(event.getPlayer()), 10);

        Main.getInstance().getChallengeManager().getActiveChallenges().forEach(Challenge::skipIfIsPossible);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "SkipUsed").replace("%player%", event.getPlayer().getName()));
        });

        //entferne nur 1 skip aus dem Inventar des Spielers
        ItemStack itemStack = event.getItem();
        itemStack.setAmount(itemStack.getAmount() - 1);

    }
}
