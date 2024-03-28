package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;

import org.bukkit.scoreboard.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@LoadChallenge
public class Deathrun extends Challenge{

    public enum ChallengeWorlds {
        CHALLENGEWORLD("ChallengeWorld"),
        CHALLENGEWORLD_NETHER("ChallengeWorld_Nether"),
        CHALLENGEWORLD_END("ChallengeWorld_the_end");

        private final String name;

        ChallengeWorlds(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @ConfigurationValue(title = "ChallengeWorld", icon = Material.GRASS_BLOCK)
    private ChallengeWorlds challengeWorld = ChallengeWorlds.CHALLENGEWORLD;

    private double totaldamage = 0;
    private List<Player> deadPlayers = new ArrayList<>();

    public Deathrun() {
        super(Material.LEATHER_BOOTS, ChallengeType.MINIGAME);
    }

    @Override
    public void update() {
        super.update();

        Location location = new Location(Bukkit.getWorld(challengeWorld.getName()), 0, 100, 0);
        location.setY(location.getWorld().getHighestBlockYAt(location));
        Bukkit.getOnlinePlayers().forEach(player -> {
            setupScoreboard(player);
            player.teleport(location);
            plugin.getBossBarInformation().updatePlayer(player);
        });

        location.getWorld().getWorldBorder().setCenter(0, 0);
        location.getWorld().getWorldBorder().setSize(50);
        location.getWorld().getWorldBorder().setWarningDistance(0);
        location.getWorld().getWorldBorder().setWarningTime(0);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Location location = new Location(Bukkit.getWorld(challengeWorld.getName()), 0, 100, 0);
        location.setY(location.getWorld().getHighestBlockYAt(location));
        Bukkit.getOnlinePlayers().forEach(player -> {
            setupScoreboard(player);
            player.teleport(location);
        });
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            totaldamage += event.getFinalDamage();
        }
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {

        Bukkit.getOnlinePlayers().forEach(player -> {
            plugin.getBossBarInformation().removeTile(player, "distance");
            unloadScoreboard(player);
        });
    }

    @Override
    public void register() {
        super.register();

        addGlobalBossBarInformationTile(new BossBarInformationTile("players", plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")), "0", Spacing.POSITIVE8PIXEl, 2));
        addGlobalBossBarInformationTile(new BossBarInformationTile("allHearts", Spacing.ZEROPIXEl.getSpacing() + "\uDAC0\uDC40" + Spacing.ZEROPIXEl.getSpacing(), "0", Spacing.POSITIVE8PIXEl, 0));
        addGlobalBossBarInformationTile(new BossBarInformationTile("deadPlayerAmount", plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("2fa10394-1f4b-45ec-8748-52920751062d")), "0", Spacing.POSITIVE8PIXEl, 3));

        //Wenn world existiert
        if (Bukkit.getWorld(challengeWorld.getName()) != null) {
            Location location = new Location(Bukkit.getWorld(challengeWorld.getName()), 0, 100, 0);
            location.setY(location.getWorld().getHighestBlockYAt(location));
            Bukkit.getOnlinePlayers().forEach(player -> {
                setupScoreboard(player);
                player.teleport(location);
            });

            location.getWorld().getWorldBorder().setCenter(0, 0);
            location.getWorld().getWorldBorder().setSize(50);
            location.getWorld().getWorldBorder().setWarningDistance(0);
            location.getWorld().getWorldBorder().setWarningTime(0);
        }
    }

    @Override
    public void unregister() {
        super.unregister();

        Bukkit.getOnlinePlayers().forEach(player -> {
            unloadScoreboard(player);
        });

        Location location = new Location(Bukkit.getWorld(challengeWorld.getName()), 0, 100, 0);
        location.getWorld().getWorldBorder().reset();
    }

    @Override
    public void tick() {
        super.tick();

        getGlobalBossBarInformationTile("players").setValue(Bukkit.getOnlinePlayers().size() + "");
        getGlobalBossBarInformationTile("allHearts").setValue(String.valueOf((int) totaldamage));
        getGlobalBossBarInformationTile("deadPlayerAmount").setValue(deadPlayers.size() + "");

        ArrayList<Player> first = getSortedPlayers();
        Bukkit.getOnlinePlayers().forEach(player -> {

            getPlayerBossBarInformationTile(player, "distance").setValue("§f" + player.getLocation().getBlockX());

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

            //get own place
            int ownPlace = 0;
            for (int i = 0; i < first.size(); i++) {
                if (first.get(i).equals(player)) {
                    ownPlace = i + 1;
                    break;
                }
            }

            if (ownPlace == 1){
                player.getScoreboard().getTeam("line-8").setPrefix("§8-----------------");
            }else {
                player.getScoreboard().getTeam("line-8").setPrefix("§f" + (ownPlace - 1) + ". " + plugin.getPlayerHeadManager().getHeadComponent(first.get(ownPlace - 2).getUniqueId()) + new ColorBuilder( first.get(ownPlace - 2).getName()).addColorToString(new Color(203, 189, 186, 255)).getText() + new ColorBuilder(" " + first.get(ownPlace - 2).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }
            player.getScoreboard().getTeam("line-9").setPrefix("§f" + ownPlace + ". " + plugin.getPlayerHeadManager().getHeadComponent(player.getUniqueId()) + new ColorBuilder("§l" + player.getName()).addColorToString(new Color(217, 207, 205, 255)).getText() + new ColorBuilder(" " + player.getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            if (ownPlace == first.size()){
                player.getScoreboard().getTeam("line-10").setPrefix("§8-----------------");
            }else {
                player.getScoreboard().getTeam("line-10").setPrefix("§f" + (ownPlace + 1) + ". " + plugin.getPlayerHeadManager().getHeadComponent(first.get(ownPlace).getUniqueId()) + new ColorBuilder(first.get(ownPlace).getName()).addColorToString(new Color(210, 200, 198, 255)).getText() + new ColorBuilder(" " + first.get(ownPlace).getLocation().getBlockX()).addColorToString(new Color(194, 85, 233, 255)).getText());
            }

        });

    }

    public ArrayList<Player> getSortedPlayers(){
        ArrayList<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());

        //order by x
        players.sort((o1, o2) -> {
            if (o1.getLocation().getX() > o2.getLocation().getX()) {
                return -1;
            } else if (o1.getLocation().getX() < o2.getLocation().getX()) {
                return 1;
            }
            return 0;
        });

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

    public void setupScoreboard(Player p) {
        Objective objective = null;

        addPlayerBossBarInformationTile(p, new BossBarInformationTile("distance", plugin.getPlayerHeadManager().getHeadComponent(UUID.fromString("fef039ef-e6cd-4987-9c84-26a3e6134277")), "0", Spacing.POSITIVE8PIXEl, -1));

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
