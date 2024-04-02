package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class ItsMLGTime extends BaseAdvancement {
    public ItsMLGTime(@NotNull Advancement parent) {
        super("its_mlg_time", new AdvancementDisplay(Material.WATER_BUCKET, "Its MLG time", AdvancementFrameType.TASK, true, true, 2f, 1f, "Begebe dich auf maximale Bauhöhe"), parent);

        registerEvent(PlayerMoveEvent.class, e -> {
            Player player = e.getPlayer();
            if (isVisible(player)) {

                if (player.getLocation().getY() >= 320) {
                    grant(player);
                }

            }
        });

    }

    @Override
    public void giveReward(@NotNull Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
