package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurableField;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Collection;

@Getter
public class GameRule implements Listener {
    private transient final String name;
    private transient final String description;
    private transient final Material icon;
    @Getter
    @Setter
    private boolean enabled;

    @Setter
    @Getter
    private transient Collection<ConfigurableField> configurableFields;

    public GameRule(Material icon) {
        this.name = this.getClass().getSimpleName() + "GameRuleName";
        this.description = this.getClass().getSimpleName() + "GameRuleDescription";
        this.icon = icon;
        this.enabled = false;
    }

    public GameRule(Material icon, boolean enabled) {
        this.name = this.getClass().getSimpleName() + "GameRuleName";
        this.description = this.getClass().getSimpleName() + "GameRuleDescription";
        this.icon = icon;
        this.enabled = enabled;
    }

    public void register() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public void update(){}
    public void timerFirstTimeResume(){}

    public String getTranslationName(Player player) {
        return Main.getInstance().getTranslationManager().getTranslation(player, name);
    }
}
