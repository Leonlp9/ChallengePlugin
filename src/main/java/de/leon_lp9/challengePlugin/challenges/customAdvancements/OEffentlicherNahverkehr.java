package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.jetbrains.annotations.NotNull;

public class OEffentlicherNahverkehr extends BaseAdvancement {
    public OEffentlicherNahverkehr(@NotNull Advancement parent) {
        super("oeffentlicher_nahverkehr", new AdvancementDisplay(org.bukkit.Material.MINECART, "Öffentlicher Nahverkehr", AdvancementFrameType.TASK, true, true, 4f, 2f, "Fahre 1000 Blöcke mit einem Minecart"), parent, 1000);

        registerEvent(org.bukkit.event.vehicle.VehicleMoveEvent.class, e -> {
            if (e.getVehicle() instanceof org.bukkit.entity.Minecart minecart) {
                if (!minecart.getPassengers().isEmpty()) {
                    if (minecart.getPassengers().get(0) instanceof org.bukkit.entity.Player player) {
                        if (isVisible(player) && getParent().isGranted(player)) {
                            if (e.getFrom().getBlock().getLocation().distance(e.getTo().getBlock().getLocation()) >= 1) {
                                incrementProgression(player);
                            }
                        }
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
