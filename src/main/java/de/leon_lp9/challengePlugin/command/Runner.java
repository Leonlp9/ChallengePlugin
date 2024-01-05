package de.leon_lp9.challengePlugin.command;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface Runner {

    void run(Object instance, String[] args, CommandSender executor);

}
