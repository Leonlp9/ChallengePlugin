package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.worldgeneration.populators.SpacingPopulator;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;

import java.util.Iterator;

@MinecraftCommand(name = "reset", description = "ResetDescription")
public class ResetCommand implements Listener {

    public ResetCommand() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Run
    public void command(String[] strings, CommandSender commandSender) {
        if (strings.length > 0){
            Main.getInstance().getWorldGenerationManager().regenerateWorlds(Long.parseLong(strings[0]));
        }else {
            Main.getInstance().getWorldGenerationManager().regenerateWorlds(null);
        }
    }


}
