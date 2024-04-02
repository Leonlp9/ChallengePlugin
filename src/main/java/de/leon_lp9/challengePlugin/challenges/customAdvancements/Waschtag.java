package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Waschtag extends BaseAdvancement implements ParentGrantedVisibility {
    public Waschtag(@NotNull Advancement parent) {
        super("waschtag", new AdvancementDisplay(Material.LEATHER_CHESTPLATE, "Waschtag", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 3f, 1.5f+10f, "Wasche eine komplette Lederrüstung"), parent);


    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }

}
