package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.util.UUID;

@LoadChallenge
public class Deathrun extends Challenge{
    public Deathrun() {
        super(Material.LEATHER_BOOTS, ChallengeType.MINIGAME);
    }

    @Override
    public void register() {
        super.register();

        Main.getInstance().getBossBarInformation().addTile(new BossBarInformationTile("players", Main.getInstance().getPlayerHeadManager().getHeadComponent(UUID.fromString("9cb6a52c-55bc-456b-9513-f4cf19cdf9e3")), "0", Spacing.POSITIVE32PIXEl));
        Main.getInstance().getBossBarInformation().addTile(new BossBarInformationTile("allHearts", Spacing.ZEROPIXEl.getSpacing() + "\uDAC0\uDC40" + Spacing.ZEROPIXEl.getSpacing(), "0", Spacing.POSITIVE32PIXEl));
    }

    @Override
    public void unregister() {
        super.unregister();

        Main.getInstance().getBossBarInformation().removeTile("players");
        Main.getInstance().getBossBarInformation().removeTile("allHearts");
    }

    @Override
    public void unload() {
        super.unload();

        Main.getInstance().getBossBarInformation().removeTile("players");
        Main.getInstance().getBossBarInformation().removeTile("allHearts");
        Main.getInstance().getBossBarInformation().update();
    }

    @Override
    public void tick() {
        super.tick();

        Main.getInstance().getBossBarInformation().getTile("players").setValue(Bukkit.getOnlinePlayers().size() + "");
        //Alle Herzen zusammen rechnen
        Main.getInstance().getBossBarInformation().getTile("allHearts").setValue(Bukkit.getOnlinePlayers().stream().mapToInt(player -> (int) player.getHealth()).sum() + "");

    }
}
