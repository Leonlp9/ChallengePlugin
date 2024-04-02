package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Durchbruch extends BaseAdvancement {
    public Durchbruch(@NotNull Advancement parent) {
        super("durchbruch", new AdvancementDisplay(Material.SHIELD, "(Der) Durchbruch", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 4f, 3f, "Zerstöre ein Schild durch blocken"), parent);

        registerEvent(org.bukkit.event.player.PlayerItemBreakEvent.class, e -> {
            if (e.getBrokenItem().getType() == Material.SHIELD) {
                grant(e.getPlayer());
            }
        });

    }

    @Override
    public void giveReward(@NotNull org.bukkit.entity.Player player) {
        player.setLevel(player.getLevel() + 1);
    }
}
