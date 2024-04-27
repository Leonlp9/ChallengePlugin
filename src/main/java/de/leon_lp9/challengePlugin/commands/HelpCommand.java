package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.crafting.Crafting;
import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import de.leon_lp9.challengePlugin.command.TabComplete;
import de.leon_lp9.challengePlugin.management.BlockHologramAbovePlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@MinecraftCommand(name = "craft", description = "Crafting command")
public class HelpCommand implements Listener {

    ArrayList<String> recipes = new ArrayList<>();

    public HelpCommand() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        for (Material value : Material.values()) {
            if (Bukkit.getRecipesFor(new ItemBuilder(value).build()).size() > 0){
                recipes.add(value.name().toLowerCase());
            }
        }
    }

    @EventHandler
    public void onInvKlick(InventoryClickEvent event){
        if (event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getType() == Material.AIR) return;
        if(event.getView().getTitle().contains("Crafting-Rezept:")){
            event.setCancelled(true);

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {

                    if ((i * 9) + j + 1 == event.getSlot()){
                        new Crafting(event.getCurrentItem()).showRecipe((Player) event.getWhoClicked(), 0);
                    }

                }
            }

            //wenn es nicht das top inventory ist return
            if (event.getClickedInventory() != event.getView().getTopInventory()) return;

            Integer page = Integer.parseInt(event.getView().getTitle().split(" ")[2].replace("(", "").replace(")", "").split("/")[0]);
            Integer maxPage = Integer.parseInt(event.getView().getTitle().split(" ")[2].replace("(", "").replace(")", "").split("/")[1]);

            if (event.getSlot() == 0 || event.getSlot() == 9 || event.getSlot() == 18){
                if (page == 0) return;
                new Crafting(event.getInventory().getItem(15)).showRecipe((Player) event.getWhoClicked(), page- 2);
            }

            if (event.getSlot() == 8 || event.getSlot() == 17 || event.getSlot() == 26){
                if (page.equals(maxPage)) return;
                new Crafting(event.getInventory().getItem(15)).showRecipe((Player) event.getWhoClicked(), page);
            }

        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event){
        if (event.getView().getTitle().contains("Crafting-Rezept:")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (event.getItemInHand() != null && event.getItemInHand().getItemMeta() != null && event.getItemInHand().getItemMeta().getLocalizedName() != null && event.getItemInHand().getItemMeta().getLocalizedName().contains("crafting")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(InventoryClickEvent event){
        if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.getCurrentItem().getItemMeta().getLocalizedName() != null && event.getCurrentItem().getItemMeta().getLocalizedName().contains("crafting")){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(PlayerDropItemEvent event){
        if (event.getItemDrop() != null && event.getItemDrop().getItemStack() != null && event.getItemDrop().getItemStack().getItemMeta() != null && event.getItemDrop().getItemStack().getItemMeta().getLocalizedName() != null && event.getItemDrop().getItemStack().getItemMeta().getLocalizedName().contains("crafting")){
            event.setCancelled(true);
        }
    }
    @Run
    public void command(String[] strings, CommandSender commandSender) {

        if (!commandSender.hasPermission("herausforderung.crafting")) {
            commandSender.sendMessage("§cDu hast keine Rechte!");
        }

        if (strings.length == 0) {
            commandSender.sendMessage("§cBitte gebe /crafting <Material> ein!");
        }

        if (strings.length == 1) {
            String cmd = strings[0];

            Material material = Material.getMaterial(cmd.toUpperCase());

            if (material == null){
                commandSender.sendMessage("§cBitte gebe ein gültiges Material ein!");
            }

            new Crafting(material).showRecipe((Player) commandSender, 0);
        }
    }

    @TabComplete
    public @Nullable List<String> test(CommandSender commandSender, @NotNull String[] strings) {
        ArrayList<String> list = new ArrayList<>();

        if (strings.length == 1){
            list.addAll(recipes);

            list.removeIf(s1 -> !s1.toLowerCase().startsWith(strings[0].toLowerCase()));
        }

        return list;
    }
}
