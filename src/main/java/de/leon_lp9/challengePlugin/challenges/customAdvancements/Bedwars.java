package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class Bedwars extends BaseAdvancement {
    public Bedwars(@NotNull Advancement parent) {
        super("bedwars", new AdvancementDisplay(Material.BLUE_BED, "Bedwars?", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 4f, 4f, "Baue 10 Betten in Dörfern ab"), parent, 10);

        registerEvent(BlockBreakEvent.class, e -> {
            if (isVisible(e.getPlayer())) {
                Player player = e.getPlayer();

                Location loc = player.getWorld().locateNearestStructure(player.getLocation(), org.bukkit.StructureType.VILLAGE, 10, false);
                if (loc == null) return;
                loc.setY(player.getLocation().getY());
                if (loc.distance(player.getLocation()) < 150) {
                    if (e.getBlock().getType().name().endsWith("_BED")) {
                        incrementProgression(player);
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
