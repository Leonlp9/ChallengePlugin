package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.jetbrains.annotations.NotNull;

public class Schmelzmeister extends BaseAdvancement implements ParentGrantedVisibility {
    public Schmelzmeister(@NotNull Advancement parent) {
        super("schmelzmeister", new AdvancementDisplay(org.bukkit.Material.FURNACE, "Schmelzmeister", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 3f, 5f+10f, "Schmelze 1000 Eisenbarren"), parent, 1000);

        registerEvent(org.bukkit.event.inventory.FurnaceSmeltEvent.class, e -> {
            if (e.getResult().getType() == org.bukkit.Material.IRON_INGOT) {
                e.getBlock().getLocation().getNearbyEntitiesByType(org.bukkit.entity.Player.class, 10).forEach(player -> {
                    if (isVisible(player) && getParent().isGranted(player)) {
                        incrementProgression(player, e.getResult().getAmount());
                    }
                });
            }
        });

    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
