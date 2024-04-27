package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

@LoadChallenge
public class ForceHeightChallenge extends Challenge{

    public enum ForceHeightState {
        FORCE_HEIGHT,
        WAITING
    }

    @ConfigurationValue(title = "Minuten", icon = Material.CLOCK, min = 1, max = 60)
    private int minMinutes = 5;

    @ConfigurationValue(title = "Max Minuten", icon = Material.CLOCK, min = 1, max = 60)
    private int randomMinutes = 10;

    private long lastForceHeightTime = 60;
    private int nextForceInMinutes = 0;

    private ForceHeightState state = ForceHeightState.WAITING;

    private int forceHeight = 0;
    private int secondsToGetToHeight = 0;

    public ForceHeightChallenge() {
        super(Material.LADDER, ChallengeType.ZWISCHENZIELE);

        this.update();
    }

    @Override
    public void update() {
        super.update();

        nextForceInMinutes = minMinutes + getRandom().nextInt(randomMinutes);
    }

    @Override
    public void timerTick(int second) {
        super.timerTick(second);

        if (state == ForceHeightState.WAITING) {
            if (second - lastForceHeightTime > (long) nextForceInMinutes * 60) {
                state = ForceHeightState.FORCE_HEIGHT;
                lastForceHeightTime = second;
                nextForceInMinutes = minMinutes + getRandom().nextInt(randomMinutes);
                forceHeight = 50 + getRandom().nextInt(150);
                secondsToGetToHeight = 45 + getRandom().nextInt(100);
                addGlobalBossBarInformationTile(new BossBarInformationTile("height", "§5Y§0: ", forceHeight + "", Spacing.POSITIVE4PIXEl, 0));
                addGlobalBossBarInformationTile(new BossBarInformationTile("time", "§5Z§5e§5i§5t§0: ", secondsToGetToHeight + " s", Spacing.POSITIVE4PIXEl, 1));
                getAllSurvivalPlayers().forEach(player -> {
                    addPlayerBossBarInformationTile(player, new BossBarInformationTile("yourHeight", "§5D§5e§5i§5n §5Y§0: ",  player.getLocation().getBlockY() + "", Spacing.POSITIVE4PIXEl, 2));
                });
            }
        }else if (state == ForceHeightState.FORCE_HEIGHT) {
            getGlobalBossBarInformationTile("time").setValue(secondsToGetToHeight - (second - lastForceHeightTime) + " s");

            if (second - lastForceHeightTime > secondsToGetToHeight) {
                state = ForceHeightState.WAITING;
                getAllSurvivalPlayers().forEach(player -> {
                    if (player.getLocation().getBlockY() != forceHeight) {
                        getAllSurvivalPlayers().forEach(all -> {
                            all.sendMessage("§c" + player.getName() + " §7hat die Höhe nicht erreicht und ist gestorben.");
                        });
                    }
                    removePlayerBossBarInformationTile(player, "yourHeight");
                });
                removeGlobalBossBarInformationTile("height");
                removeGlobalBossBarInformationTile("time");
                getAllSurvivalPlayers().forEach(player -> {
                    if (player.getLocation().getBlockY() != forceHeight) {
                        player.setHealth(0);
                    }
                });
            }
        }

    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (state == ForceHeightState.FORCE_HEIGHT) {
            getPlayerBossBarInformationTile(event.getPlayer(), "yourHeight").setValue(event.getPlayer().getLocation().getBlockY() + "");
        }
    }

}
