package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.jetbrains.annotations.NotNull;

public class Pyrotechniker extends BaseAdvancement implements ParentGrantedVisibility {
    public Pyrotechniker(@NotNull Advancement parent) {
        super("pyrotechniker", new AdvancementDisplay(org.bukkit.Material.FIREWORK_ROCKET, "Pyrotechniker", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 3f, 4f+10f, "Zünde eine Creeper mit einem Feuerzeug an"), parent);

        registerEvent(PlayerInteractEntityEvent.class, e -> {
            if (e.getRightClicked().getType() == org.bukkit.entity.EntityType.CREEPER && e.getPlayer().getInventory().getItemInMainHand().getType() == org.bukkit.Material.FLINT_AND_STEEL && isVisible(e.getPlayer()) && getParent().isGranted(e.getPlayer())){
                grant(e.getPlayer());
            }
        });

    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
