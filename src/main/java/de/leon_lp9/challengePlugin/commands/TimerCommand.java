package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import java.awt.Color;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@MinecraftCommand(name = "timer", description = "Timer command")
public class TimerCommand {

    private final Timer timer;

    public TimerCommand() {
        timer = Main.getInstance().getChallengeManager().getTimer();
    }

    @Run
    public boolean command(String[] strings, CommandSender commandSender) {

        String prefix = "§c§lTimer §8| §7";
        if (strings.length == 0) {
            commandSender.sendMessage(prefix + "Der Timer ist aktuell auf §a§l" + timer.getFormattedTime() + "§7.");
            return true;
        }

        if (strings[0].equalsIgnoreCase("pause")) {
            if (timer.isResumed()) {
                timer.setResumed(false);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(prefix + "Der Timer wurde §c§lpausiert§7 von §a§l" + commandSender.getName() + "§7.");
                });
                timer.sendActionBar();
            } else {
                commandSender.sendMessage(prefix + "Der Timer ist bereits §a§lpausiert§7.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("resume")) {
            if (!timer.isResumed()) {
                timer.setResumed(true);
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage(prefix + "Der Timer wurde §a§lfortgesetzt§7 von §a§l" + commandSender.getName() + "§7.");
                });
                timer.sendActionBar();
            } else {
                commandSender.sendMessage(prefix + "Der Timer läuft bereits.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("reset")) {
            timer.setSeconds(0);
            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendMessage(prefix + "Der Timer wurde §a§lzurückgesetzt§7 von §a§l" + commandSender.getName() + "§7.");
            });
            timer.sendActionBar();
            return true;
        }else

        if (strings[0].equalsIgnoreCase("set")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(seconds);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Der Timer wurde auf §a§l" + timer.getFormattedTime() + "§7 gesetzt von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
                }
            } else {
                commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("add")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(timer.getSeconds() + seconds);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Der Timer wurde um §a§l" + seconds + " Sekunden §7erhöht von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
                }
            } else {
                commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("remove")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(timer.getSeconds() - seconds);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Der Timer wurde um §a§l" + seconds + " Sekunden §7verringert von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
                }
            } else {
                commandSender.sendMessage(prefix + "Bitte gib eine §a§lZahl §7an.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("get")) {
            commandSender.sendMessage(prefix + "Der Timer ist aktuell auf Sekunde §a§l" + timer.getSeconds() + "§7.");
            return true;
        }else

        if (strings[0].equalsIgnoreCase("mode")) {
            if (strings.length == 2) {
                try {
                    Timer.TimerState state = Timer.TimerState.valueOf(strings[1]);
                    timer.setState(state);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Der Timer wurde auf §a§l" + state.toString() + "§7 gesetzt von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(prefix + "Bitte gib einen §a§lModus §7an.");
                }
            } else {
                commandSender.sendMessage(prefix + "Bitte gib einen §a§lModus §7an.");
            }
            return true;
        }else

        if(strings[0].equalsIgnoreCase("setFirstColor")){
            if(strings.length == 2){
                try{
                    Color color = Color.decode(strings[1]);
                    timer.setFirstColor(color);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Die erste Farbe wurde auf " + ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue())) + "§l■■■§7 gesetzt von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                }catch (IllegalArgumentException e){
                    commandSender.sendMessage(prefix + "Bitte gib eine §a§lFarbe §7an.");
                }
            }else{
                commandSender.sendMessage(prefix + "Bitte gib eine §a§lFarbe §7an.");
            }
            return true;
        }else

        if(strings[0].equalsIgnoreCase("setSecondColor")){
            if(strings.length == 2){
                try{
                    Color color = Color.decode(strings[1]);
                    timer.setSecondColor(color);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Die zweite Farbe wurde auf " + ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue())) + "§l■■■§7 gesetzt von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                }catch (IllegalArgumentException e){
                    commandSender.sendMessage(prefix + "Bitte gib eine §a§lFarbe §7an.");
                }
            }else{
                commandSender.sendMessage(prefix + "Bitte gib eine §a§lFarbe §7an.");
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("bold")) {
            if (strings.length == 2) {
                try {
                    boolean bold = Boolean.parseBoolean(strings[1]);
                    timer.setBold(bold);
                    Bukkit.getOnlinePlayers().forEach(player -> {
                        player.sendMessage(prefix + "Bold wurde auf §a§l" + bold + "§7 gesetzt von §a§l" + commandSender.getName() + "§7.");
                    });
                    timer.sendActionBar();
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(prefix + "Bitte gib einen §a§lWahrheitswert §7an.");
                }
            } else {
                commandSender.sendMessage(prefix + "Bitte gib einen §a§lWahrheitswert §7an.");
            }
            return true;
        }

        return false;
    }


    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.add("pause");
            list.add("resume");
            list.add("reset");
            list.add("set");
            list.add("add");
            list.add("remove");
            list.add("get");
            list.add("mode");
            list.add("setFirstColor");
            list.add("setSecondColor");
            list.add("bold");
            list.removeIf(string -> !string.startsWith(strings[0]));
        }
        if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("mode")) {

                for (Timer.TimerState value : Timer.TimerState.values()) {
                    list.add(value.toString());
                }

                list.removeIf(string -> !string.startsWith(strings[1]));
            }else if(strings[0].equalsIgnoreCase("setFirstColor") || strings[0].equalsIgnoreCase("setSecondColor")){
                list.add("#000000");
                list.add("#0000FF");
                list.add("#00FF00");
                list.add("#00FFFF");
                list.add("#FF0000");
                list.add("#FF00FF");
                list.add("#FFFF00");
                list.add("#FFFFFF");
                list.removeIf(string -> !string.startsWith(strings[1]));
            }else if(strings[0].equalsIgnoreCase("bold")){
                list.add("true");
                list.add("false");
                list.removeIf(string -> !string.startsWith(strings[1]));
            }
        }
        return list;
    }
}
