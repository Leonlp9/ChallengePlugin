package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@LoadChallenge
public class Wolfi extends Challenge{

    private final transient Map<UUID, UUID> playerWolfiMap;

    //@ConfigurationValue(title = "nameTagVisible", icon = Material.NAME_TAG)
    private boolean nameTagVisible = true;

    public Wolfi() {
        super(Material.BONE, ChallengeType.ENTITYS);
        playerWolfiMap = new HashMap<>();
    }

    public void spawnWolfi(Player player){
        Wolf wolf = player.getWorld().spawn(player.getLocation(), Wolf.class);
        wolf.setOwner(player);
        wolf.setTamed(true);
        wolf.setCustomNameVisible(nameTagVisible);
        wolf.setCustomName(player.getName() + "s Wolfi");
        playerWolfiMap.put(player.getUniqueId(), wolf.getUniqueId());
    }

    public void removeWolfi(Player player){
        if (playerWolfiMap.containsKey(player.getUniqueId())){
            if (Bukkit.getEntity(playerWolfiMap.get(player.getUniqueId())) != null){
                Objects.requireNonNull(Bukkit.getEntity(playerWolfiMap.get(player.getUniqueId()))).remove();
            }
            playerWolfiMap.remove(player.getUniqueId());
        }
    }

    public void removeAllWolfis(){
        playerWolfiMap.forEach((playerUUID, wolfUUID) -> {
            if (Bukkit.getEntity(wolfUUID) != null){
                Objects.requireNonNull(Bukkit.getEntity(wolfUUID)).remove();
            }
        });
        playerWolfiMap.clear();
    }


    @Override
    public void register() {
        super.register();

        Bukkit.getOnlinePlayers().forEach(this::spawnWolfi);
    }

    @Override
    public void unregister() {
        super.unregister();

        removeAllWolfis();
    }

    @Override
    public void unload() {
        super.unload();

        removeAllWolfis();
    }

    @Override
    public void update() {
        super.update();

        playerWolfiMap.forEach((playerUUID, wolfUUID) -> {
            if (Bukkit.getEntity(wolfUUID) != null) {
                Wolf wolf = (Wolf) Bukkit.getEntity(wolfUUID);
                wolf.setCustomName(null);
            }
        });
    }

    @EventHandler
    public void onWolfDeath(org.bukkit.event.entity.EntityDeathEvent event) {
        if (event.getEntity() instanceof Wolf wolf) {
            if (wolf.getOwner() != null) {
                if (wolf.getOwner() instanceof org.bukkit.entity.Player player) {
                    if (playerWolfiMap.containsKey(player.getUniqueId())) {
                        playerWolfiMap.remove(player.getUniqueId());
                        player.sendMessage("§cDein Wolfi ist gestorben!");
                        player.setHealth(0);
                    }
                }
            }
        }
    }

}
