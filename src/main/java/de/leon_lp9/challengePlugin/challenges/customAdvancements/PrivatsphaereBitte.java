package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.destroystokyo.paper.event.entity.EndermanAttackPlayerEvent;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class PrivatsphaereBitte extends BaseAdvancement {
    public PrivatsphaereBitte(@NotNull Advancement parent) {
        super("privatsphaere_bitte", new AdvancementDisplay(Material.ENDER_PEARL, "Ey, ein bisschen Privatsphäre bitte!", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 2f, 6f, "Schaue einem Enderman in die Augen"), parent);



    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
