package de.leon_lp9.challengePlugin.management;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

public class BossBarInformation {

    private ArrayList<BossBarInformationTile> tiles;
    private Map<Player, ArrayList<BossBarInformationTile>> playerTiles;
    private Map<Player, BossBar> playerBossBars;

    public BossBarInformation() {
        this.tiles = new ArrayList<>();
        this.playerTiles = new java.util.HashMap<>();
        this.playerBossBars = new java.util.HashMap<>();
    }

    public boolean hasTile(String key) {
        return this.tiles.stream().anyMatch(tile -> tile.getKey().equals(key));
    }

    public void addTile(BossBarInformationTile tile) {
        this.tiles.add(tile);
        update();
    }

    public void addTile(Player player, BossBarInformationTile tile) {
        if (!this.playerTiles.containsKey(player)) {
            this.playerTiles.put(player, new ArrayList<>());
        }
        this.playerTiles.get(player).add(tile);
        updatePlayer(player);
    }

    public void removeTile(String key) {
        this.tiles.removeIf(tile -> tile.getKey().equals(key));
        update();
    }

    public void removeTile(Player player, String key) {
        if (this.playerTiles.containsKey(player)) {
            this.playerTiles.get(player).removeIf(tile -> tile.getKey().equals(key));
            updatePlayer(player);
        }
    }

    public BossBarInformationTile getTile(String key) {
        return this.tiles.stream().filter(tile -> tile.getKey().equals(key)).findFirst().orElse(null);
    }

    public BossBarInformationTile getTile(Player player, String key) {
        if (this.playerTiles.containsKey(player)) {
            return this.playerTiles.get(player).stream().filter(tile -> tile.getKey().equals(key)).findFirst().orElse(null);
        }
        return null;
    }

    public void update() {
        Bukkit.getOnlinePlayers().forEach(this::updatePlayer);
    }

    public void updatePlayer(Player player) {
        ArrayList<BossBarInformationTile> tiles = new ArrayList<>(this.tiles);
        tiles.addAll(this.playerTiles.getOrDefault(player, new ArrayList<>()));
        tiles.sort(Comparator.comparingInt(BossBarInformationTile::getSortIndex));

        StringBuilder title = new StringBuilder();
        tiles.forEach(tile -> {
            title.append(tile.getPadding().getSpacing());
            title.append(new ColorBuilder(tile.getTitle() + (tile.getValue() == null ? "" : " " + tile.getValue())).addBackground().getText());
            title.append(tile.getPadding().getSpacing());
        });

        if (!this.playerBossBars.containsKey(player)) {
            System.out.println("Create new BossBar for " + player.getName());
            this.playerBossBars.put(player, Bukkit.createBossBar(title.toString(), BarColor.WHITE, BarStyle.SOLID));
            this.playerBossBars.get(player).addPlayer(player);
            this.playerBossBars.get(player).setProgress(0);

        }
        this.playerBossBars.get(player).setVisible(!tiles.isEmpty());

        this.playerBossBars.get(player).setTitle(title.toString());
    }

    public void removeAll() {
        this.playerBossBars.forEach((player, bossBar) -> bossBar.removeAll());
        this.playerBossBars.clear();
    }

}
