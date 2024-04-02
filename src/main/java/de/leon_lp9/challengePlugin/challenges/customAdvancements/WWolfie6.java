package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.bukkit.event.entity.EntityTameEvent;
import org.jetbrains.annotations.NotNull;

public class WWolfie6 extends BaseAdvancement {
    public WWolfie6(@NotNull Advancement parent) {
        super("wwolfie6", new AdvancementDisplay(Material.WOLF_SPAWN_EGG, "W-wolfie 6?", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 2f, 5f, "Zähme einen Hund"), parent);

        registerEvent(EntityTameEvent.class, e -> {
            if (e.getEntity().getType() == org.bukkit.entity.EntityType.WOLF) {
                grant((org.bukkit.entity.Player) e.getOwner());
            }
        });

    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }

}
