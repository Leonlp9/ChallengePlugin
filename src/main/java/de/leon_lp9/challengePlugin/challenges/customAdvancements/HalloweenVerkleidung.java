package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.entity.Villager;
import org.jetbrains.annotations.NotNull;

public class HalloweenVerkleidung extends BaseAdvancement implements ParentGrantedVisibility {
    public HalloweenVerkleidung(@NotNull Advancement parent) {
        super("halloween_verkleidung", new AdvancementDisplay(org.bukkit.Material.JACK_O_LANTERN, "Halloween Verkleidung", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 2f, 2f+10f, "Setze einem Villager einen geschnitzten Kürbis auf"), parent);

        registerEvent(org.bukkit.event.player.PlayerInteractEntityEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                if (e.getRightClicked() instanceof Villager villager) {
                    if (villager.getEquipment().getHelmet() != null && villager.getEquipment().getHelmet().getType() == org.bukkit.Material.CARVED_PUMPKIN) {
                        grant(player);
                    }
                }
            }
        });
    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }

}
