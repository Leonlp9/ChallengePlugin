package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import org.bukkit.Material;

public class RandomBlockDrops extends Challenge{

    @ConfigurationValue(title = "Save Drops", description = "Sollen die Items gespeichert werden?", icon = Material.CHEST)
    private boolean saveDrops;

    @ConfigurationValue(title = "Test", description = "Test", icon = Material.CHEST)
    private String test = "Test";

    public RandomBlockDrops(boolean saveDrops) {
        super("RandomBlockDrops", "Jeder Block dropt ein zufälliges Item", Material.CHEST);
        this.saveDrops = saveDrops;
    }

    public boolean isSaveDrops() {
        return saveDrops;
    }
}
