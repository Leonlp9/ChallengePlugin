package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurableField;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Collection;

@Getter
public class Challenge implements Listener {
    private final String name;
    private final String description;
    private final Material icon;

    @Setter
    @Getter
    private transient Collection<ConfigurableField> configurableFields;

    public Challenge(String name, String description, Material icon) {
        this.name = name;
        this.description = description;
        this.icon = icon;

        Main.getInstance().getConfigurationReader().readConfigurableFields(this);
    }

    public void register() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
