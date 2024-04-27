package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.HashMap;

@LoadChallenge
public class ForceItemBattle extends Challenge {

    private transient final HashMap<Player, Material> playerItems = new HashMap<>();

    public ForceItemBattle() {
        super(Material.CHEST, ChallengeType.MINIGAME);



    }

    @Override
    public void register() {
        super.register();

        getAllSurvivalPlayers().forEach(player -> {
            playerItems.put(player, randomItem());
            plugin.getBlockHologramAbovePlayer().createHologram(player, playerItems.get(player));
        });
    }

    @Override
    public void unregister() {
        super.unregister();

        playerItems.forEach((player, material) -> {
            plugin.getBlockHologramAbovePlayer().removeHologram(player);
        });
    }

    @Override
    public void skipIfIsPossible(Player player) {
        super.skipIfIsPossible(player);

        changeItem(player);
    }

    private void changeItem(Player player) {
        plugin.getBlockHologramAbovePlayer().removeHologram(player);
        player.playSound(player.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        player.sendMessage("§aDu hast das Item gefunden!");
        playerItems.put(player, randomItem());
        plugin.getBlockHologramAbovePlayer().createHologram(player, playerItems.get(player));
        player.sendMessage("§aDein neues Item ist: " + playerItems.get(player).name());
    }

    @EventHandler
    public void onGameModeChange(org.bukkit.event.player.PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == org.bukkit.GameMode.SURVIVAL) {
            playerItems.put(event.getPlayer(), randomItem());
            plugin.getBlockHologramAbovePlayer().createHologram(event.getPlayer(), playerItems.get(event.getPlayer()));
        } else {
            plugin.getBlockHologramAbovePlayer().removeHologram(event.getPlayer());
        }
    }

    @EventHandler
    public void playerPickupItem(org.bukkit.event.player.PlayerPickupItemEvent event) {
        if (playerItems.containsKey(event.getPlayer())) {
            if (event.getItem().getItemStack().getType() == playerItems.get(event.getPlayer())) {
                changeItem(event.getPlayer());
            }
        }
    }


    @EventHandler
    public void onInventoryInteract(org.bukkit.event.inventory.InventoryClickEvent event) {
        if (playerItems.containsKey(event.getWhoClicked())) {
            if (event.getCurrentItem().getType() == playerItems.get(event.getWhoClicked())) {
                changeItem((Player) event.getWhoClicked());
            }
        }
    }

}
