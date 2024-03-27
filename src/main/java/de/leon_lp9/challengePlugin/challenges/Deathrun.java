package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import org.bukkit.scoreboard.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@LoadChallenge
public class Deathrun extends Challenge{

    public Deathrun() {
        super(Material.LEATHER_BOOTS, ChallengeType.MINIGAME);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        setupScoreboard(event.getPlayer());
    }

    @Override
    public void register() {
        super.register();

        plugin.getBossBarInformation().addTile(new BossBarInformationTile("players", plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")), "0", Spacing.POSITIVE32PIXEl));
        plugin.getBossBarInformation().addTile(new BossBarInformationTile("allHearts", Spacing.ZEROPIXEl.getSpacing() + "\uDAC0\uDC40" + Spacing.ZEROPIXEl.getSpacing(), "0", Spacing.POSITIVE32PIXEl));

        Bukkit.getOnlinePlayers().forEach(player -> {
            setupScoreboard(player);
        });
    }

    @Override
    public void unregister() {
        super.unregister();

        plugin.getBossBarInformation().removeTile("players");
        plugin.getBossBarInformation().removeTile("allHearts");

        Bukkit.getOnlinePlayers().forEach(player -> {
            unloadScoreboard(player);
        });
    }

    @Override
    public void unload() {
        super.unload();

        plugin.getBossBarInformation().removeTile("players");
        plugin.getBossBarInformation().removeTile("allHearts");
        plugin.getBossBarInformation().update();
    }

    @Override
    public void tick() {
        super.tick();

        plugin.getBossBarInformation().getTile("players").setValue(Bukkit.getOnlinePlayers().size() + "");
        //Alle Herzen zusammen rechnen
        plugin.getBossBarInformation().getTile("allHearts").setValue(Bukkit.getOnlinePlayers().stream().mapToInt(player -> (int) player.getHealth()).sum() + "");

        ArrayList<Player> first = getFirst5Players();
        Bukkit.getOnlinePlayers().forEach(player -> {
            if (!first.isEmpty()){
                player.getScoreboard().getTeam("line-2").setPrefix("§f1. " + plugin.getPlayerHeadManager().getHeadComponent(first.get(0).getUniqueId()) + new ColorBuilder("§l" + first.get(0).getName()).addColorToString(new Color(0, 228, 49, 255)).getText() + new ColorBuilder(" " + first.get(0).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }else {
                player.getScoreboard().getTeam("line-2").setPrefix("§f1. " + plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")) + new ColorBuilder("§lLeer").addColorToString(new Color(0, 228, 49, 255)).getText() + new ColorBuilder(" 0").addColorToString(new Color(194, 85, 233, 255)).getText());
            }
            if (first.size() > 1){
                player.getScoreboard().getTeam("line-3").setPrefix("§f2. " + plugin.getPlayerHeadManager().getHeadComponent(first.get(1).getUniqueId()) + new ColorBuilder("§l" + first.get(1).getName()).addColorToString(new Color(40, 240, 87, 255)).getText() + new ColorBuilder(" " + first.get(1).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }else {
                player.getScoreboard().getTeam("line-3").setPrefix("§f2. " + plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")) + new ColorBuilder("§lLeer").addColorToString(new Color(40, 240, 87, 255)).getText() + new ColorBuilder(" 0").addColorToString(new Color(194, 85, 233, 255)).getText());
            }
            if (first.size() > 2){
                player.getScoreboard().getTeam("line-4").setPrefix("§f3. " + plugin.getPlayerHeadManager().getHeadComponent(first.get(2).getUniqueId()) + new ColorBuilder("§l" + first.get(2).getName()).addColorToString(new Color(87, 246, 128, 255)).getText() + new ColorBuilder(" " + first.get(2).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }else {
                player.getScoreboard().getTeam("line-4").setPrefix("§f3. " + plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")) + new ColorBuilder("§lLeer").addColorToString(new Color(87, 246, 128, 255)).getText() + new ColorBuilder(" 0").addColorToString(new Color(194, 85, 233, 255)).getText());
            }
            if (first.size() > 3){
                player.getScoreboard().getTeam("line-5").setPrefix("§f4. " + plugin.getPlayerHeadManager().getHeadComponent(first.get(3).getUniqueId()) + new ColorBuilder("§l" + first.get(3).getName()).addColorToString(new Color(203, 189, 186, 255)).getText() + new ColorBuilder(" " + first.get(3).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }else {
                player.getScoreboard().getTeam("line-5").setPrefix("§f4. " + plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")) + new ColorBuilder("Leer").addColorToString(new Color(203, 189, 186, 255)).getText() + new ColorBuilder(" 0").addColorToString(new Color(194, 85, 233, 255)).getText());
            }
            if (first.size() > 4){
                player.getScoreboard().getTeam("line-6").setPrefix("§f5. " + plugin.getPlayerHeadManager().getHeadComponent(first.get(4).getUniqueId()) + new ColorBuilder("§l" + first.get(4).getName()).addColorToString(new Color(210, 200, 198, 255)).getText() + new ColorBuilder(" " + first.get(4).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }else {
                player.getScoreboard().getTeam("line-6").setPrefix("§f5. " + plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")) + new ColorBuilder("Leer").addColorToString(new Color(210, 200, 198, 255)).getText() + new ColorBuilder(" 0").addColorToString(new Color(194, 85, 233, 255)).getText());
            }
        });

    }

    //Gibt die spieler zurück die am weitesten vorne sind auf der x coordiante
    public ArrayList<Player> getFirst5Players(){
        ArrayList<Player> players = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (players.size() < 5) {
                players.add(player);
            } else {
                for (int i = 0; i < players.size(); i++) {
                    if (player.getLocation().getX() > players.get(i).getLocation().getX()) {
                        players.set(i, player);
                        break;
                    }
                }
            }
        }
        return players;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().getX() < -50) {
            if (event.getFrom().getX() < -50) {
                Location location = event.getPlayer().getLocation().clone();
                location.setX(-49);
                event.getPlayer().teleport(location);
            }
        }
        if (event.getTo().getX() < -45) {
            for (int i = -8; i < 8; i++) {
                for (int j = -8; j < 8; j++) {
                    Location location = event.getTo().clone();
                    location.setX(-50);
                    location.add(0, j, i);
                    location.add(0.5, 0.5, 0.5);

                    if (location.distance(event.getTo()) > 5) {
                        continue;
                    }

                    if (location.getBlock().getType().isSolid()) {
                        continue;
                    }

                    event.getPlayer().sendBlockChange(location, Material.BARRIER.createBlockData());
                    event.getPlayer().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 3));

                }
            }
        }
        if (event.getTo().getZ() < -50) {
            if (event.getFrom().getZ() < -50) {
                Location location = event.getPlayer().getLocation().clone();
                location.setZ(-49);
                event.getPlayer().teleport(location);
            }
        }
        if (event.getTo().getZ() < -45) {
            for (int i = -8; i < 8; i++) {
                for (int j = -8; j < 8; j++) {
                    Location location = event.getTo().clone();
                    location.setZ(-50);
                    location.add(j, i, 0);
                    location.add(0.5, 0.5, 0.5);

                    if (location.distance(event.getTo()) > 5) {
                        continue;
                    }

                    if (location.getBlock().getType().isSolid()) {
                        continue;
                    }

                    event.getPlayer().sendBlockChange(location, Material.BARRIER.createBlockData());
                    event.getPlayer().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 3));

                }
            }
        }
        if (event.getTo().getZ() > 50) {
            if (event.getFrom().getZ() > 50) {
                Location location = event.getPlayer().getLocation().clone();
                location.setZ(49);
                event.getPlayer().teleport(location);
            }
        }
        if (event.getTo().getZ() > 45) {
            for (int i = -8; i < 8; i++) {
                for (int j = -8; j < 8; j++) {
                    Location location = event.getTo().clone();
                    location.setZ(50);
                    location.add(j, i, 0);
                    location.add(0.5, 0.5, 0.5);

                    if (location.distance(event.getTo()) > 5) {
                        continue;
                    }

                    if (location.getBlock().getType().isSolid()) {
                        continue;
                    }

                    event.getPlayer().sendBlockChange(location, Material.BARRIER.createBlockData());
                    event.getPlayer().spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, new Particle.DustOptions(org.bukkit.Color.fromRGB(255, 0, 0), 3));

                }
            }
        }
    }

    @EventHandler
    public void onVehicleMove(VehicleMoveEvent event){
        if (event.getTo().getX() < -50 || event.getTo().getZ() < -50 || event.getTo().getZ() > 50) {
            event.getVehicle().remove();
        }
    }

    @EventHandler
    public void onPlayerPortalEvent(PlayerPortalEvent event) {
        event.setCancelled(true);
        event.getPlayer().sendMessage("§cDu darfst während des Deathruns nicht Porten!");
    }

    public static void setupScoreboard(Player p) {
        Objective objective = null;

        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());

        objective = p.getScoreboard().registerNewObjective("sidebar", "bbb");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(" \uDAC0\uDC49 ");

        objective.getScore("§1").setScore(11);
        objective.getScore("§2").setScore(10);
        objective.getScore("§3").setScore(9);
        objective.getScore("§4").setScore(8);
        objective.getScore("§5").setScore(7);
        objective.getScore("§6").setScore(6);
        objective.getScore("§7").setScore(5);
        objective.getScore("§8").setScore(4);
        objective.getScore("§9").setScore(3);
        objective.getScore("§0").setScore(2);
        objective.getScore("§a").setScore(1);

        p.getScoreboard().registerNewTeam("line-1");
        p.getScoreboard().registerNewTeam("line-2");
        p.getScoreboard().registerNewTeam("line-3");
        p.getScoreboard().registerNewTeam("line-4");
        p.getScoreboard().registerNewTeam("line-5");
        p.getScoreboard().registerNewTeam("line-6");
        p.getScoreboard().registerNewTeam("line-7");
        p.getScoreboard().registerNewTeam("line-8");
        p.getScoreboard().registerNewTeam("line-9");
        p.getScoreboard().registerNewTeam("line-10");
        p.getScoreboard().registerNewTeam("line-11");

        p.getScoreboard().getTeam("line-1").addEntry("§1");
        p.getScoreboard().getTeam("line-2").addEntry("§2");
        p.getScoreboard().getTeam("line-3").addEntry("§3");
        p.getScoreboard().getTeam("line-4").addEntry("§4");
        p.getScoreboard().getTeam("line-5").addEntry("§5");
        p.getScoreboard().getTeam("line-6").addEntry("§6");
        p.getScoreboard().getTeam("line-7").addEntry("§7");
        p.getScoreboard().getTeam("line-8").addEntry("§8");
        p.getScoreboard().getTeam("line-9").addEntry("§9");
        p.getScoreboard().getTeam("line-10").addEntry("§0");
        p.getScoreboard().getTeam("line-11").addEntry("§a");

    }

    public void unloadScoreboard(Player p) {
        p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

}
