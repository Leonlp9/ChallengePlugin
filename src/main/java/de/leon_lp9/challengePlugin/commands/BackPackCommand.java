package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.gamerules.BackPack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@MinecraftCommand(name = "backpack", description = "BackPackDescription")
public class BackPackCommand {

    @Run
    public void command(String[] strings, CommandSender commandSender) {

        if (!Main.getInstance().getGameruleManager().isEnable(BackPack.class)){
            commandSender.sendMessage("§cBackPack ist nicht aktiviert");
            return;
        }

        if (commandSender instanceof Player player){
            ((BackPack) Main.getInstance().getGameruleManager().getGameRuleByClass(BackPack.class)).openInventory(player);
        }
    }

}
