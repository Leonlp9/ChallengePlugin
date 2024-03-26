package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

@MinecraftCommand(name = "showplayerheaddebug")
public class ShowPlayerHeadDebugCommand {


    @Run
    public void command(String[] strings, CommandSender commandSender) {
        String text;
        if (strings.length == 0) {
            text = Main.getInstance().getPlayerHeadManager().getHeadComponent((Player) commandSender);
        } else {
            text = Main.getInstance().getPlayerHeadManager().getHeadComponent(UUID.fromString(strings[0]));
        }
        commandSender.sendMessage(text);
        ((Player) commandSender).sendTitle(text, "", 10, 60, 20);

    }

}
