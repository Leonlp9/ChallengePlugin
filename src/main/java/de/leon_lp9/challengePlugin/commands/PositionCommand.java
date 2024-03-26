package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@MinecraftCommand(name = "position", description = "PositionDescription")
public class PositionCommand {


    @Run
    public void command(String[] strings, CommandSender commandSender) {
        if (strings.length == 0) {
            commandSender.sendMessage("Soon");
        }
    }

}
