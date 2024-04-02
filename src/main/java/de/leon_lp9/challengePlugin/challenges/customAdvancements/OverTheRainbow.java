package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class OverTheRainbow extends BaseAdvancement implements ParentGrantedVisibility {
    public OverTheRainbow(@NotNull Advancement parent) {
        super("over_the_rainbow", new AdvancementDisplay(Material.ELYTRA, "Over the Rainbow", AdvancementFrameType.TASK, true, true, 3f, 9f, "Fliege 1.000 Blöcke hoch"), parent);

        registerEvent(org.bukkit.event.player.PlayerMoveEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                if (player.getLocation().getY() >= 1000 && player.isGliding()) {
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
