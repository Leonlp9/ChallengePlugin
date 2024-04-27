package de.leon_lp9.challengePlugin.management;

import de.leon_lp9.challengePlugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.joml.Matrix4f;

import java.util.HashMap;

public class BlockHologramAbovePlayer implements Listener {

    public HashMap<Player, Material> playerItems = new HashMap<>();

    public void createHologram(Player player, Material material) {
        playerItems.put(player, material);

        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Location location = player.getLocation().add(0, 2, 0);
            location.setYaw(0);
            location.setPitch(0);

            ItemDisplay itemDisplay = player.getWorld().spawn(location, ItemDisplay.class);
            itemDisplay.setItemStack(new ItemStack(material));

            //scale the item to 0.5 and move it up by 0.5
            itemDisplay.setTransformationMatrix(new Matrix4f().scale(0.4f).translate(0, 2f, 0));

            player.addPassenger(itemDisplay);
        });

    }

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (playerItems.containsKey(event.getPlayer())) {
            if (event.getFrom().getBlock().getType() != event.getTo().getBlock().getType()) {
                if (event.getTo().getBlock().getType() == Material.NETHER_PORTAL || event.getTo().getBlock().getType() == Material.END_PORTAL || event.getTo().getBlock().getType() == Material.END_PORTAL_FRAME || event.getTo().getBlock().getType() == Material.NETHER_PORTAL || event.getTo().getBlock().getType() == Material.END_PORTAL_FRAME || event.getTo().getBlock().getType() == Material.END_PORTAL) {
                    Material material = playerItems.get(event.getPlayer());
                    removeHologram(event.getPlayer());
                    playerItems.put(event.getPlayer(), material);
                }else if (event.getFrom().getBlock().getType() == Material.NETHER_PORTAL || event.getFrom().getBlock().getType() == Material.END_PORTAL || event.getFrom().getBlock().getType() == Material.END_PORTAL_FRAME || event.getFrom().getBlock().getType() == Material.NETHER_PORTAL || event.getFrom().getBlock().getType() == Material.END_PORTAL_FRAME || event.getFrom().getBlock().getType() == Material.END_PORTAL) {
                    createHologram(event.getPlayer(), playerItems.get(event.getPlayer()));
                }
            }
        }
    }

    public void removeHologram(Player player) {
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            player.getPassengers().forEach(passenger -> {
                if (passenger instanceof ItemDisplay) {
                    passenger.remove();
                }
            });
        });
        playerItems.remove(player);
    }

}
