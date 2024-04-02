package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class SchuetzeDieNachberschaft extends BaseAdvancement {
    public SchuetzeDieNachberschaft(@NotNull Advancement parent) {
        super("schuetze_die_nachbarschaft", new AdvancementDisplay(Material.NETHERITE_CHESTPLATE, "Schütze die Nachbarschaft", AdvancementFrameType.CHALLENGE, true, true, 4f, 1f, "Villager mit Full Netherite ausrüsten"), parent);

        registerEvent(org.bukkit.event.player.PlayerInteractEntityEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player)) {
                if (e.getRightClicked() instanceof org.bukkit.entity.Villager villager) {
                    if (villager.getEquipment().getChestplate() != null && villager.getEquipment().getChestplate().getType() == org.bukkit.Material.NETHERITE_CHESTPLATE
                            && villager.getEquipment().getLeggings() != null && villager.getEquipment().getLeggings().getType() == org.bukkit.Material.NETHERITE_LEGGINGS
                            && villager.getEquipment().getBoots() != null && villager.getEquipment().getBoots().getType() == org.bukkit.Material.NETHERITE_BOOTS
                            && villager.getEquipment().getHelmet() != null && villager.getEquipment().getHelmet().getType() == org.bukkit.Material.NETHERITE_HELMET){
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
