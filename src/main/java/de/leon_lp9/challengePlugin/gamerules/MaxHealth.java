package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityRegainHealthEvent;

@LoadGamerule
public class MaxHealth extends GameRule{

    @ConfigurationValue(title = "MaxHealth", min = 1, max = 100, icon = Material.HEART_OF_THE_SEA)
    private int maxHealt = 20;

    @ConfigurationValue(title = "Regeneration", icon = Material.ALLIUM)
    private boolean regeneration = true;

    public MaxHealth() {
        super(Material.HEART_OF_THE_SEA);
    }

    @Override
    public void update() {
        super.update();

        updateHealth();
    }

    @Override
    public void register() {
        super.register();
        updateHealth();
    }

    @Override
    public void unregister() {
        super.unregister();
        updateHealth();
    }

    @EventHandler
    public void onPlayerJoinEvent(org.bukkit.event.player.PlayerJoinEvent event) {
        if (isEnabled()) {
            event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealt);
            event.getPlayer().setHealth(maxHealt);
        }else {
            event.getPlayer().getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            event.getPlayer().setHealth(20);
        }
    }

    @EventHandler
    public void onPlayerRegeneration(EntityRegainHealthEvent event) {
        if (event.getEntity() instanceof Player) {
            if (isEnabled() && !regeneration) {
                event.setCancelled(true);
            }
        }
    }

    public void updateHealth() {
        if (isEnabled()) {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                    player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealt);
                    player.setHealth(maxHealt);
            });
        }else {
            plugin.getServer().getOnlinePlayers().forEach(player -> {
                player.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
                player.setHealth(20);
            });
        }

        //natural regeneration
        Bukkit.getWorlds().forEach(world -> world.setGameRuleValue("naturalRegeneration", String.valueOf(regeneration)));

    }
}
