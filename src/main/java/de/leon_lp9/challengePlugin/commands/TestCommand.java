package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@MinecraftCommand(name = "test", description = "Test command")
public class TestCommand {

    @Run
    public void test(String[] args, CommandSender sender) {
        System.out.println(Arrays.toString(args));
        sender.sendMessage("Hi");
    }

    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Du");
        list.add("Nudel");
        list.removeIf(string -> !string.toLowerCase().startsWith(strings[0].toLowerCase()));
        return list;
    }

}
