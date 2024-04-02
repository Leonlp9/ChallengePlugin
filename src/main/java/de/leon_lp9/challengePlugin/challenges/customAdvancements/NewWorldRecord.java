package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class NewWorldRecord extends BaseAdvancement {
    public NewWorldRecord(@NotNull Advancement parent) {
        super("new_world_record", new AdvancementDisplay(Material.CLOCK, "NEUER WELTREKORD?", AdvancementFrameType.TASK, true, true, 2f, 0, "Betrete das End mit mindestens 9 Betten und ohne Armor"), parent);

        registerEvent(org.bukkit.event.player.PlayerChangedWorldEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player)) {
                if (player.getLocation().getWorld().getName().equals("ChallengeWorld_the_end") && player.getInventory().getArmorContents().length == 0) {
                    int bedCount = 0;
                    for (org.bukkit.inventory.ItemStack item : player.getInventory().getContents()) {
                        if (item != null && item.getType().name().endsWith("_BED")) {
                            bedCount++;
                        }
                    }
                    if (bedCount >= 9) {
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
