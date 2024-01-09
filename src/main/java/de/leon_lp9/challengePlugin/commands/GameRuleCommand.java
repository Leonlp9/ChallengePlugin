package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import de.leon_lp9.challengePlugin.commands.gui.ChallengeMenu;
import de.leon_lp9.challengePlugin.commands.gui.GameRuleMenu;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
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

import java.util.List;

@MinecraftCommand(name = "gamerulechallenge", description = "Manage the gamerules of the challengea")
public class GameRuleCommand implements Listener {

    public GameRuleCommand() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Run
    public void challenge(CommandSender sender, String[] args) {
        if(args.length == 0) {

            if (!(sender instanceof Player player)) {
                return;
            }

            new GameRuleMenu().openInventory(player);

        }
    }

    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        return null;
    }

    @SneakyThrows
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player ePlayer)) {
            return;
        }
        if(event.getView().getTitle().equals("§6" + Main.getInstance().getTranslationManager().getTranslation(ePlayer, "gamerules"))) {
            event.setCancelled(true);
            ItemStack currentItem = event.getCurrentItem();
            if(currentItem != null) {
                ItemMeta itemMeta = currentItem.getItemMeta();
                if(itemMeta != null) {
                    if (!itemMeta.getPersistentDataContainer().has(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING)) return;

                    String id = itemMeta.getPersistentDataContainer().get(new NamespacedKey(Main.getInstance(), "id"), PersistentDataType.STRING);
                    Class<? extends GameRule> challengeClass = (Class<? extends GameRule>) Class.forName(id);

                    if (event.getClick().isLeftClick()) {
                        if (!Main.getInstance().getGameruleManager().isEnable(challengeClass)) {
                            Main.getInstance().getGameruleManager().setEnable(challengeClass, true);
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "hasBeenActivated").replace("%challenge%", Main.getInstance().getGameruleManager().getGameRuleByClass(challengeClass).getTranslationName(player))));
                        } else {
                            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(Main.getInstance().getTranslationManager().getTranslation(player, "hasBeenDeactivated").replace("%challenge%", Main.getInstance().getGameruleManager().getGameRuleByClass(challengeClass).getTranslationName(player))));
                            Main.getInstance().getGameruleManager().setEnable(challengeClass, false);
                        }
                        new GameRuleMenu().openInventory(((Player) event.getWhoClicked()));
                    } else {
                        Inventory itemStacks = Main.getInstance().getConfigurationReader().openConfigurator(Main.getInstance().getGameruleManager().getGameRuleByClass(challengeClass), Main.getInstance().getTranslationManager().getLanguageOfPlayer(((Player) event.getWhoClicked())));
                        event.getWhoClicked().openInventory(itemStacks);
                    }
                }
            }
        }
    }

}
