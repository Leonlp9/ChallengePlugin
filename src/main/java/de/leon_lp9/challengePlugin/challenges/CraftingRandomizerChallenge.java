package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.*;

//@LoadChallenge
public class CraftingRandomizerChallenge extends Challenge{

    protected final Map<Material, Material> randomization = new HashMap();

    public CraftingRandomizerChallenge() {
        super(Material.CRAFTING_TABLE);
    }

    protected void reloadRandomization() {
        List<Material> from = new ArrayList(Arrays.asList(Material.values()));
        from.removeIf((material) -> {
            return !material.isItem();
        });
        Collections.shuffle(from);
        List<Material> to = new ArrayList(Arrays.asList(Material.values()));
        to.removeIf((material) -> {
            return !material.isItem();
        });
        Collections.shuffle(to);

        while(!from.isEmpty()) {
            Material item = (Material)from.remove(0);
            Material result = (Material)to.remove(0);
            this.randomization.put(item, result);
        }

    }

    @Override
    public void register() {
        this.reloadRandomization();
    }

    @Override
    public void unregister() {
        this.randomization.clear();
    }


    @EventHandler(
            priority = EventPriority.NORMAL,
            ignoreCancelled = true
    )
    public void onCraftItem(@Nonnull CraftItemEvent event) {
        ItemStack item = event.getCurrentItem();
        if (item != null) {
            Material result = (Material)this.randomization.get(item.getType());
            if (result != null) {
                event.setCurrentItem((new ItemBuilder(result)).setAmount(item.getAmount()).build());
            }
        }
    }

}
