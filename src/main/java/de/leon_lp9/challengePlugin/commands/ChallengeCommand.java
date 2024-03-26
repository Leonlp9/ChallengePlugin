package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import de.leon_lp9.challengePlugin.commands.gui.ChallengeMenu;
import de.leon_lp9.challengePlugin.commands.gui.HubMenu;
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

            Main.getInstance().getMenus().getHubMenu().openInventory(player);

        }
    }

    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        return null;
    }



}
