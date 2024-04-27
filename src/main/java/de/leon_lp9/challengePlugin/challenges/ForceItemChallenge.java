package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.BlockHologramAbovePlayer;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.HashMap;

@LoadChallenge
public class ForceItemChallenge extends Challenge{

    public enum ForceItemState {
        FORCE_ITEM,
        WAITING
    }

    @ConfigurationValue(title = "Items", icon = Material.CHEST)
    private boolean syncItems = false;

    @ConfigurationValue(title = "Minuten", icon = Material.CLOCK, min = 1, max = 60)
    private int minMinutes = 5;

    @ConfigurationValue(title = "Max Minuten", icon = Material.CLOCK, min = 1, max = 60)
    private int randomMinutes = 10;

    private transient HashMap<org.bukkit.entity.Player, Material> playerItems = new HashMap<>();

    private int nextForceInSecond = 0;
    private int lastForceAtSecond = 60;
    private int timeToCollect = 0;

    private ForceItemState state = ForceItemState.WAITING;

    public ForceItemChallenge() {
        super(Material.CHEST, ChallengeType.ZWISCHENZIELE);
    }

    @Override
    public void update() {
        super.update();

        nextForceInSecond = minMinutes * 60 + getRandom().nextInt(randomMinutes * 60);
    }

    @Override
    public void unregister() {
        super.unregister();

        playerItems.forEach((player, material) -> {
            plugin.getBlockHologramAbovePlayer().removeHologram(player);
        });
    }

    @Override
    public void timerTick(int second) {
        super.timerTick(second);

        if (playerItems == null) {
            playerItems = new HashMap<>();
        }

        if (state == ForceItemState.WAITING) {
            System.out.println(second - lastForceAtSecond + " " + nextForceInSecond);
            if (second - lastForceAtSecond > nextForceInSecond) {
                lastForceAtSecond = second;
                nextForceInSecond = minMinutes * 60 + getRandom().nextInt(randomMinutes * 60);
                state = ForceItemState.FORCE_ITEM;

                if (syncItems){
                    Material item = randomItem();
                    getAllSurvivalPlayers().forEach(player -> {
                        playerItems.put(player, item);
                    });
                }else {
                    getAllSurvivalPlayers().forEach(player -> {
                        playerItems.put(player, randomItem());
                    });
                }

                playerItems.forEach((player, material) -> {
                    addPlayerBossBarInformationTile(player, new BossBarInformationTile("item", "§5I§5t§5e§5m§0: ", material.name(), Spacing.POSITIVE4PIXEl, 0));
                    plugin.getBlockHologramAbovePlayer().createHologram(player, material);
                });

                timeToCollect = 120 + getRandom().nextInt(240);

                addGlobalBossBarInformationTile(new BossBarInformationTile("time", "§5Z§5e§5i§5t§0: ", timeToCollect + " s", Spacing.POSITIVE4PIXEl, 1));
            }
        }else if (state == ForceItemState.FORCE_ITEM) {
            if (getGlobalBossBarInformationTile("time") == null) {
                state = ForceItemState.WAITING;
            }
            getGlobalBossBarInformationTile("time").setValue(timeToCollect - (second - lastForceAtSecond) + " s");
            if (second - lastForceAtSecond > timeToCollect) {
                state = ForceItemState.WAITING;

                playerItems.forEach((player, material) -> {
                    removePlayerBossBarInformationTile(player, "item");
                    plugin.getBlockHologramAbovePlayer().removeHologram(player);
                    if (!player.getInventory().contains(material)){
                        getAllSurvivalPlayers().forEach(player1 -> {
                            player1.sendMessage("§c" + player.getName() + " hat das Item nicht gesammelt!");
                        });
                    }
                });

                removeGlobalBossBarInformationTile("time");

                playerItems.forEach((player, material) -> {
                    if (!player.getInventory().contains(material)){
                        player.setHealth(0);
                    }
                });

                playerItems.clear();

                lastForceAtSecond = second;
            }
        }
    }
}
