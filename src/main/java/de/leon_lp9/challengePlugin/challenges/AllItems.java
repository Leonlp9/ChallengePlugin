package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import net.kyori.adventure.text.ComponentBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@LoadChallenge
public class AllItems extends Challenge {

    private ArrayList<Material> allMaterialsRandomized;
    private ArrayList<Material> allCollectedItems;
    private transient BossBar bossBar;

    public AllItems() {
        super(Material.ENDER_CHEST, ChallengeType.MINIGAME);
        allCollectedItems = new ArrayList<>();

        allMaterialsRandomized = new ArrayList<>();
        allMaterialsRandomized.addAll(Arrays.asList(Material.values()));
        Collections.shuffle(allMaterialsRandomized);

    }

    @Override
    public void register(){
        super.register();

        bossBar = Bukkit.createBossBar("AllItems", BarColor.BLUE, BarStyle.SOLID);

        Bukkit.getOnlinePlayers().forEach(player -> {
            bossBar.addPlayer(player);
        });
        bossBar.setVisible(true);
        setTitle();
    }

    @Override
    public void unload(){
        super.unload();

        bossBar.removeAll();
        bossBar.setVisible(false);
        bossBar = null;
    }
    @Override
    public void unregister(){
        super.unregister();

        bossBar.removeAll();
        bossBar.setVisible(false);
        bossBar = null;

    }

    public void setTitle(){
        TextComponent textComponent = new TextComponent("Next Item: ");
        textComponent.addExtra(plugin.getTranslationManager().getTranslation(allMaterialsRandomized.get(0)));
        bossBar.setTitle(textComponent.toLegacyText());
    }

    public void nextItem(){

        TextComponent hacken = new TextComponent("✔ ");
        hacken.setColor(ChatColor.GREEN);

        TextComponent gefunden = new TextComponent(plugin.getTranslationManager().getTranslation(allMaterialsRandomized.get(0)));
        gefunden.setColor(ChatColor.YELLOW);
        gefunden.addExtra(" gefunden!");

        TextComponent textComponent = new TextComponent("\nNext Item: ");
        textComponent.setColor(ChatColor.WHITE);

        TextComponent nextItem = new TextComponent(plugin.getTranslationManager().getTranslation(allMaterialsRandomized.get(1)));
        nextItem.setColor(ChatColor.GRAY);
        nextItem.setUnderlined(true);

        hacken.addExtra(gefunden);
        hacken.addExtra(textComponent);
        hacken.addExtra(nextItem);

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.spigot().sendMessage(hacken);
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        }

        allCollectedItems.add(allMaterialsRandomized.get(0));
        allMaterialsRandomized.remove(0);
        setTitle();
    }

    @EventHandler
    public void onPickUpItemEvent(PlayerPickupItemEvent event){
        if (!isRunning()) return;
        if (event.getItem().getItemStack().getType().equals(allMaterialsRandomized.get(0))) {
            nextItem();
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        bossBar.addPlayer(player);
    }

}
