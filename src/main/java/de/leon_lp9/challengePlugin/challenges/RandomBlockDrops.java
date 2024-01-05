package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@LoadChallenge
public class RandomBlockDrops extends Challenge{

    /**
     * Wenn false, dann wird ein zufälliges Item gedroppt
     * Wenn true, dann soll gespeichert werden, welches Item gedroppt wurde und wenn der block erneut abgebaut wird, soll das gleiche Item gedroppt werden
     */
    @Getter
    @ConfigurationValue(title = "Save Drops", description = "Sollen die Items gespeichert werden?", icon = Material.PAPER)
    @SuppressWarnings("FieldMayBeFinal")
    private boolean saveDrops = true;

    private final Map<Integer, Integer> savedDropsOrdinal = new HashMap<>();

    public RandomBlockDrops() {
        super("RandomBlockDrops", "Jeder Block dropt ein zufälliges Item", Material.CHEST);
        System.out.println("RandomBlockDrops");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (!isRunning()){
            return;
        }
        if (!saveDrops){
            ItemStack itemStack = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
            while (itemStack.getType().isAir() || itemStack.getType().isLegacy() || itemStack.getType().name().contains("_WALL_")){
                itemStack = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
            }
            itemStack.setAmount(event.getBlock().getDrops().size());
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
            event.setDropItems(false);
        }else{
            if (savedDropsOrdinal.containsKey(event.getBlock().getType().ordinal())){
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.values()[savedDropsOrdinal.get(event.getBlock().getType().ordinal())]));
            }else{
                ItemStack itemStack = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
                while (itemStack.getType().isAir() || itemStack.getType().isLegacy()){
                    itemStack = new ItemStack(Material.values()[new Random().nextInt(Material.values().length)]);
                }
                savedDropsOrdinal.put(event.getBlock().getType().ordinal(), itemStack.getType().ordinal());
                itemStack.setAmount(event.getBlock().getDrops().size());
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), itemStack);
            }
            event.setDropItems(false);
        }
    }

}
