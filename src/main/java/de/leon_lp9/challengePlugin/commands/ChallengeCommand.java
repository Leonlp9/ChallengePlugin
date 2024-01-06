package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import de.leon_lp9.challengePlugin.commands.gui.ChallengeMenu;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.List;

@MinecraftCommand(name = "challenge", description = "Challenge command")
public class ChallengeCommand implements Listener {

    public ChallengeCommand() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Run
    public void challenge(CommandSender sender, String[] args) {
        if(args.length == 0) {

            if (!(sender instanceof Player player)) {
                return;
            }

            new ChallengeMenu().openInventory(player);

        }
    }

    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        return null;
    }

    @SneakyThrows
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getView().getTitle().equals("§6Challenges")) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if(itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) return;

                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                    Class<? extends Challenge> challengeClass = (Class<? extends Challenge>) Class.forName(id);

                    if (event.getClick().isLeftClick()) {
                        if (!Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                            Main.getInstance().getChallengeManager().activateChallenge(challengeClass);
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§6" + Main.getInstance().getChallengeManager().getLoadedChallengeByClass(challengeClass).getName() + " §7wurde aktiviert!"));
                        } else {
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("§6" + Main.getInstance().getChallengeManager().getLoadedChallengeByClass(challengeClass).getName() + " §7wurde deaktiviert!"));
                            Main.getInstance().getChallengeManager().deactivateChallenge(challengeClass);
                        }
                        new ChallengeMenu().openInventory(((Player) event.getWhoClicked()));
                    } else if (event.getClick().isRightClick() && Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)) {
                        Inventory itemStacks = Main.getInstance().getConfigurationReader().openConfigurator(Main.getInstance().getChallengeManager().getActiveChallengeByClass(challengeClass));
                        event.getWhoClicked().openInventory(itemStacks);
                    }else if (event.getClick().isRightClick() && !Main.getInstance().getChallengeManager().isChallengeActive(challengeClass)){
                        event.getWhoClicked().sendMessage("§cDiese Challenge ist nicht aktiviert!");
                    }
                }
            }
        }
    }

}
