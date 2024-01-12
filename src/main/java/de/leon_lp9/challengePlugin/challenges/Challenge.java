package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurableField;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Collection;

@Getter
public class Challenge implements Listener {
    private transient final String name;
    private transient final String description;
    private transient final Material icon;

    @Setter
    @Getter
    private transient Collection<ConfigurableField> configurableFields;

    public Challenge(Material icon) {
        this.name = this.getClass().getSimpleName() + "Name";
        this.description = this.getClass().getSimpleName() + "Description";
        this.icon = icon;

        Main.getInstance().getConfigurationReader().readConfigurableFields(this);
    }

    public void register() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    public boolean isRunning() {
        return Main.getInstance().getChallengeManager().getTimer().isResumed();
    }

    public Timer getTimer() {
        return Main.getInstance().getChallengeManager().getTimer();
    }

    public void update(){}
    public void timerTick(int second){}
    public void tick(){}

    public String getTranslationName(Player player) {
        return Main.getInstance().getTranslationManager().getTranslation(player, name);
    }
}
