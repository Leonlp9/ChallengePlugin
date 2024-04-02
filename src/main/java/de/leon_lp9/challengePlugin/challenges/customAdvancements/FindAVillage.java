package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.visibilities.ParentGrantedVisibility;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FindAVillage extends BaseAdvancement implements ParentGrantedVisibility {
    public FindAVillage(RootAdvancement root) {
        super("find_a_village", new AdvancementDisplay(Material.VILLAGER_SPAWN_EGG, "Bau dir ein eigenes!", AdvancementFrameType.TASK, true, true, 1f, 10f, "Finde ein Village. Der Anfang jedes Projektes"), root);

        // Register the event via the tab's EventManager
        registerEvent(PlayerMoveEvent.class, e -> {
            Player player = e.getPlayer();
            if (isVisible(player) && getParent().isGranted(player)) {
                //detect if player is in a village

                Location loc = player.getWorld().locateNearestStructure(player.getLocation(), org.bukkit.StructureType.VILLAGE, 10, false);
                if (loc == null) return;
                loc.setY(player.getLocation().getY());
                if (loc.distance(player.getLocation()) < 50) {
                    grant(player);
                }
            }
        });

    }
    @Override
    public void giveReward(@NotNull Player player) {
       player.setLevel(player.getLevel() + 1);
    }
}
