package de.leon_lp9.challengePlugin.management;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.ArrayList;

public class BossBarInformation {

    private ArrayList<BossBarInformationTile> tiles;
    private BossBar bossBar;

    public BossBarInformation() {
        this.bossBar = Bukkit.createBossBar(" ", BarColor.WHITE, BarStyle.SOLID);
        this.bossBar.setProgress(0);
        this.tiles = new ArrayList<>();
    }

    public void addTile(BossBarInformationTile tile) {
        this.tiles.add(tile);
    }

    public void removeTile(String key) {
        this.tiles.removeIf(tile -> tile.getKey().equals(key));
    }

    public BossBarInformationTile getTile(String key) {
        return this.tiles.stream().filter(tile -> tile.getKey().equals(key)).findFirst().orElse(null);
    }

    public void update() {
        StringBuilder title = new StringBuilder();

        for (BossBarInformationTile tile : this.tiles) {
            title.append(tile.getPadding().getSpacing());
            title.append(new ColorBuilder(tile.getTitle() + " " + tile.getValue()).addBackground().getText());
            title.append(tile.getPadding().getSpacing());
        }

        this.bossBar.setTitle(title.toString());
        this.bossBar.setColor(BarColor.WHITE);
        this.bossBar.setStyle(BarStyle.SOLID);
        this.bossBar.setProgress(0);

        this.bossBar.setVisible(!this.tiles.isEmpty());

        Bukkit.getOnlinePlayers().forEach(player -> {
            this.bossBar.addPlayer(player);
        });
    }

}
