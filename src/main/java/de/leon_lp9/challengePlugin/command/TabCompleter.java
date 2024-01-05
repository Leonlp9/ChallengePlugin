package de.leon_lp9.challengePlugin.command;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FunctionalInterface
public interface TabCompleter {

    @Nullable List<String> onTabComplete(Object instance ,CommandSender commandSender, @NotNull String[] strings);

}
