package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class WasEineAussicht extends BaseAdvancement implements ParentGrantedVisibility {
    public WasEineAussicht(@NotNull Advancement parent) {
        super("was_eine_aussicht", new AdvancementDisplay(Material.NETHERRACK, "Was eine Aussicht", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 3f, 8f, "Komme auf die Netherdecke"), parent);

        registerEvent(org.bukkit.event.player.PlayerMoveEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                if (player.getWorld().getName().equals("ChallengeWorld_nether") && player.getLocation().getY() >= 128) {
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
