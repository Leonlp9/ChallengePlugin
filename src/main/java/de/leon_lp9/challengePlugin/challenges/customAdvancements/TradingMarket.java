package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.BaseAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TradingMarket extends BaseAdvancement {
    public TradingMarket(@NotNull Advancement parent) {
        super("trading_market", new AdvancementDisplay(Material.EMERALD_BLOCK, "Trading Market", com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType.TASK, true, true, 3f, 3f, "Villager auf max Stufe traden"), parent);

        registerEvent(org.bukkit.event.player.PlayerInteractEntityEvent.class, e -> {
            org.bukkit.entity.Player player = e.getPlayer();
            if (isVisible(player)) {
                if (e.getRightClicked() instanceof org.bukkit.entity.Villager villager) {
                    if (villager.getVillagerLevel() == 5) {
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
