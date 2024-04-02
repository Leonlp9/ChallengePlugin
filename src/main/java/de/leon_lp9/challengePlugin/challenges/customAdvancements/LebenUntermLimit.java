package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class LebenUntermLimit extends BaseAdvancement implements ParentGrantedVisibility {
    public LebenUntermLimit(@NotNull Advancement parent) {
        super("leben_unterm_limit", new AdvancementDisplay(Material.BEDROCK, "Leben unterm Limit", AdvancementFrameType.TASK, true, true, 4f, 10f, "Unter höhe 0 mit einer Elytra fliegen"), parent);

        registerEvent(org.bukkit.event.player.PlayerMoveEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                if (player.getLocation().getY() < -64 && player.isGliding()) {
                    grant(player);
                }
            }
        });

    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
