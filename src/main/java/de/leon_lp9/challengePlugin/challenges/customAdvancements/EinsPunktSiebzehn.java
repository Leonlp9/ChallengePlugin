package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class EinsPunktSiebzehn extends BaseAdvancement implements ParentGrantedVisibility {
    public EinsPunktSiebzehn(@NotNull Advancement parent) {
        super("eins_punkt_siebzehn", new AdvancementDisplay(Material.BEDROCK, "1.17", AdvancementFrameType.TASK, true, true, 3f, 10f, "Sei auf dem Bedrock in der Overword"), parent);

        registerEvent(org.bukkit.event.player.PlayerMoveEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                if (player.getWorld().getName().equals("ChallengeWorld") && player.getLocation().clone().add(0, -1, 0).getBlock().getType() == Material.BEDROCK){
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
