package de.leon_lp9.challengePlugin.challenges;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;


public class LevelEqualsBorder extends Challenge{

    public LevelEqualsBorder() {
        super(Material.STRUCTURE_BLOCK);
    }

    @Override
    public void register() {
        super.register();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getWorldBorder() == null) {
                player.setWorldBorder(Bukkit.getWorlds().get(0).getWorldBorder());
            }
            Location spawnLocation = player.getWorld().getHighestBlockAt(player.getLocation()).getLocation();
            player.getWorldBorder().setSize(1);
            player.getWorldBorder().setCenter(spawnLocation);
            if (player.getWorldBorder().isInside(player.getLocation())){
                player.teleport(spawnLocation);
            }
        });

    }

    @Override
    public void unregister() {
        super.unregister();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.getWorldBorder() == null) {
                player.setWorldBorder(Bukkit.getWorlds().get(0).getWorldBorder());
            }
            player.getWorldBorder().reset();
        });
    }

}
