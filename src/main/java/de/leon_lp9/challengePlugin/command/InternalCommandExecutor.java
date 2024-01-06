package de.leon_lp9.challengePlugin.command;

import lombok.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Data
public class InternalCommandExecutor implements CommandExecutor, TabCompleter {

    private final Object instance;
    private final CommandInfo info;

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!commandSender.hasPermission(info.getPermission()) && !info.getPermission().isEmpty()) {
            commandSender.sendMessage("§8| §aServer §8» §6Du§7 hast keine §6Rechte§7 für diesen §6Command§7!");
            return true;
        }

        info.getRun().run(instance, strings, commandSender);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (info.getTabCompleter() != null) {
            return info.getTabCompleter().onTabComplete(instance, commandSender, strings);
        }
        return null;
    }
}
