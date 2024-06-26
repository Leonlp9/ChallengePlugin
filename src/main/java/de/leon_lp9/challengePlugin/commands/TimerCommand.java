package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import java.awt.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

        if (!(commandSender instanceof Player cPlayer)) {
            commandSender.sendMessage("You must be a player to execute this command.");
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "timerIsAt").replace("%time%", timer.getFormattedTime()));
            Main.getInstance().getMenus().getTimerMenu().openInventory(cPlayer);
            return true;
        }

        if (strings[0].equalsIgnoreCase("pause")) {
            if (timer.isResumed()) {
                timer.stopTimer(cPlayer);
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "timerAlreadyPaused"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("resume")) {
            if (!timer.isResumed()) {
                timer.startTimer(cPlayer);
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "timerAlreadyResumed"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("reset")) {
            timer.resetTimer(cPlayer);
            return true;
        }else

        if (strings[0].equalsIgnoreCase("set")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(seconds, cPlayer);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("add")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(timer.getSeconds() + seconds, cPlayer);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("remove")) {
            if (strings.length == 2) {
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    timer.setSeconds(timer.getSeconds() - seconds, cPlayer);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidNumber"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("get")) {
            commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "timerIsAt").replace("%time%", timer.getFormattedTime()));
            return true;
        }else

        if (strings[0].equalsIgnoreCase("mode")) {
            if (strings.length == 2) {
                try {
                    Timer.TimerState state = Timer.TimerState.valueOf(strings[1]);
                    timer.setState(state, cPlayer);
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidMode"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidMode"));
            }
            return true;
        }else

        if(strings[0].equalsIgnoreCase("setFirstColor")){
            if(strings.length == 2){
                try{
                    Color color = Color.decode(strings[1]);
                    timer.setFirstColor(color, cPlayer);
                }catch (IllegalArgumentException e){
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidColor"));
                }
            }else{
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidColor"));
            }
            return true;
        }else

        if(strings[0].equalsIgnoreCase("setSecondColor")){
            if(strings.length == 2){
                try{
                    Color color = Color.decode(strings[1]);
                    timer.setSecondColor(color, cPlayer);
                }catch (IllegalArgumentException e){
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidColor"));
                }
            }else{
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidColor"));
            }
            return true;
        }else

        if (strings[0].equalsIgnoreCase("bold")) {
            if (strings.length == 2) {
                try {
                    boolean bold = Boolean.parseBoolean(strings[1]);
                    timer.setBold(bold, cPlayer);
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidBoolean"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidBoolean"));
            }
            return true;
        }else if(strings[0].equalsIgnoreCase("background")){
            if (strings.length == 2) {
                try {
                    boolean background = Boolean.parseBoolean(strings[1]);
                    timer.setBackground(background, cPlayer);
                } catch (IllegalArgumentException e) {
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidBoolean"));
                }
            } else {
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidBoolean"));
            }
            return true;
        }

        else if (strings[0].equalsIgnoreCase("help")) {

            commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation((Player) commandSender, "timerHelp"));

            return true;
        }else if(strings[0].equalsIgnoreCase("displaytype")){
            if(strings.length == 2){
                try{
                    Timer.DisplayType displayType = Timer.DisplayType.valueOf(strings[1]);
                    timer.setDisplayType(displayType, cPlayer);
                }catch (IllegalArgumentException e){
                    commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidDisplayType"));
                }
            }else{
                commandSender.sendMessage(Main.getInstance().getTranslationManager().getTranslation(cPlayer, "pleaseEnterAValidDisplayType"));
            }
            return true;
        }

        return false;
    }


    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();
        if (strings.length == 1) {
            list.add("help");
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
            list.add("displaytype");
            list.add("background");
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
            }else if(strings[0].equalsIgnoreCase("bold") || strings[0].equalsIgnoreCase("background")){
                list.add("true");
                list.add("false");
                list.removeIf(string -> !string.startsWith(strings[1]));
            }else if(strings[0].equalsIgnoreCase("displaytype")){
                for (Timer.DisplayType value : Timer.DisplayType.values()) {
                    list.add(value.toString());
                }
                list.removeIf(string -> !string.startsWith(strings[1]));
            }
        }
        return list;
    }
}
