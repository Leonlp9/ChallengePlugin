package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PrivatsphaereBitte extends BaseAdvancement implements ParentGrantedVisibility {
    public PrivatsphaereBitte(@NotNull Advancement parent) {
        super("privatsphaere_bitte", new AdvancementDisplay(Material.ENDER_PEARL, "Ey, ein bisschen Privatsphäre bitte!", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 2f, 6f+10f, "Schaue einem Enderman in die Augen"), parent);

        registerEvent(org.bukkit.event.entity.EntityTargetLivingEntityEvent.class, e -> {
            if (e.getTarget() instanceof org.bukkit.entity.Player player && isVisible(player) && getParent().isGranted(player)) {
                if (e.getEntity().getType() == org.bukkit.entity.EntityType.ENDERMAN) {
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
