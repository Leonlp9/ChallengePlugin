package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.ChallengeManager;
import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.gamerules.GameRule;
import de.leon_lp9.challengePlugin.management.FileUtils;
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

        Main.getInstance().getChallengeManager().deactivateAllChallenges();
        Main.getInstance().getGameruleManager().resetGameRules();
        Main.getInstance().getChallengeManager().getTimer().setResumed(false);
        Main.getInstance().getChallengeManager().getTimer().setSeconds(0);
        Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countup);

        for (Player all : Bukkit.getOnlinePlayers()) {

            //wenn tot
            all.spigot().respawn();

            all.teleport(Bukkit.getWorld("world").getSpawnLocation());
            all.getInventory().clear();
            all.getInventory().setArmorContents(null);
            all.setMaxHealth(20);
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setLevel(0);
            all.setFireTicks(0);
            all.setSaturation(20);
            all.setFallDistance(0);
            all.setTotalExperience(0);
            all.setExhaustion(0);
            all.getEnderChest().clear();

            Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
            while (iterator.hasNext())
            {
                AdvancementProgress progress = all.getAdvancementProgress(iterator.next());
                for (String criteria : progress.getAwardedCriteria())
                    progress.revokeCriteria(criteria);
            }
        }

        //Welten löschen
        World world = Bukkit.getWorld("ChallengeWorld");
        world.setKeepSpawnInMemory(false);
        Bukkit.unloadWorld(world, false);
        Main.getInstance().getFileUtils().deleteDirectory(world.getWorldFolder());

        World nether = Bukkit.getWorld("ChallengeWorld_nether");
        nether.setKeepSpawnInMemory(false);
        Bukkit.unloadWorld(nether, false);
        Main.getInstance().getFileUtils().deleteDirectory(nether.getWorldFolder());

        World end = Bukkit.getWorld("ChallengeWorld_the_end");
        end.setKeepSpawnInMemory(false);
        Bukkit.unloadWorld(end, false);
        Main.getInstance().getFileUtils().deleteDirectory(end.getWorldFolder());

        //Welten erstellen
        WorldCreator overworldCreator = new WorldCreator("ChallengeWorld");
        Bukkit.createWorld(overworldCreator);

        WorldCreator netherCreator = new WorldCreator("ChallengeWorld_nether");
        netherCreator.environment(World.Environment.NETHER);
        netherCreator.seed(overworldCreator.seed());
        netherCreator.createWorld();

        WorldCreator theEndCreator = new WorldCreator("ChallengeWorld_the_end");
        theEndCreator.environment(World.Environment.THE_END);
        theEndCreator.seed(overworldCreator.seed());
        theEndCreator.createWorld();

        //Spieler teleportieren
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.teleport(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

    //Link worlds for the portals

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Location to = event.getTo();
        if(to.getWorld().getName().equals("world")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld"));
        }
        if(to.getWorld().getName().equals("world_nether")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld_nether"));
        }
        if(to.getWorld().getName().equals("world_the_end")) {
            to.setWorld(Bukkit.getWorld("ChallengeWorld_the_end"));
        }
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerPortal(EntityPortalEvent event) {
        Location to = event.getTo();
        if(to.getWorld().getName().equals("world")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld"));
        }
        if(to.getWorld().getName().equals("world_nether")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld_nether"));
        }
        if(to.getWorld().getName().equals("world_the_end")) {
            to.setWorld(Bukkit.getWorld("ChallengeWorld_the_end"));
        }
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals("world")) {
            event.getPlayer().teleport(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

    @EventHandler
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        if (event.getRespawnLocation().getWorld().getName().equals("world")) {
            event.setRespawnLocation(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

}
