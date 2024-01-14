package de.leon_lp9.challengePlugin.crafting;

import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class Crafting {

    public Material material;
    public Crafting(ItemStack itemStack) {
        this.material = itemStack.getType();
    }

    public Crafting(Material material) {
        this.material = material;
    }

    public void showRecipe(Player player, Integer page) {
        // Rezepte für das Material abrufen
        List<Recipe> recipes = Bukkit.getServer().getRecipesFor(new ItemStack(material));

        // Überprüfen, ob Rezepte vorhanden sind
        if (recipes.isEmpty()) {
            player.sendMessage("Keine Rezepte für dieses Material gefunden.");
            return;
        }

        Integer maxPage = recipes.size();

        // Erstellen des Inventars
        Inventory inventory = Bukkit.createInventory(null, 27, "Crafting-Rezept: " + material.toString() + " (" + (page + 1) + "/" + maxPage + ")");

        for (int i = 0; i < 3; i++) {
            inventory.setItem(i * 9 + 4, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayName("§7").build());
            for (int j = 0; j < 3; j++) {
                inventory.setItem((i * 9) + j + 5, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setDisplayName("§7").build());
            }
        }

        // Erste Rezept auswählen
        Recipe recipe = recipes.get(page);

        if (page == 0){
            inventory.setItem(0, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cVorherige Seite").build());
            inventory.setItem(9, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cVorherige Seite").build());
            inventory.setItem(18, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cVorherige Seite").build());
        }else {
            inventory.setItem(0, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aVorherige Seite").build());
            inventory.setItem(9, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aVorherige Seite").build());
            inventory.setItem(18, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aVorherige Seite").build());
        }

        if ((page + 1) ==  maxPage){
            inventory.setItem(8, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cNächste Seite").build());
            inventory.setItem(17, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cNächste Seite").build());
            inventory.setItem(26, new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§cNächste Seite").build());
        }else {
            inventory.setItem(8, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aNächste Seite").build());
            inventory.setItem(17, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aNächste Seite").build());
            inventory.setItem(26, new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayName("§aNächste Seite").build());
        }

        // Überprüfen, ob es sich um ein geformtes Rezept handelt
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe) recipe;

            // Rezept-Matrix abrufen
            Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
            String[] shape = shapedRecipe.getShape();

            // Rezept-Zutaten zum Inventar hinzufügen
            for (int i = 0; i < shape.length; i++) {
                String row = shape[i];
                for (int j = 0; j < row.length(); j++) {
                    char c = row.charAt(j);
                    if (ingredientMap.containsKey(c)) {
                        ItemStack ingredient = ingredientMap.get(c);
                        inventory.setItem((i * 9) + j + 1, ingredient);
                    }
                }
            }

            inventory.setItem(15, shapedRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.CRAFTING_TABLE));
        }else if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            int i1 = 0;
            for (int i = 0; i < shapelessRecipe.getIngredientList().size(); i++) {
                ItemStack ingredient = shapelessRecipe.getIngredientList().get(i);
                inventory.setItem((i + 9 * i1 + 1) - i1 * 3, ingredient);

                if (i == 2 || i == 5){
                    i1++;
                }
            }

            inventory.setItem(15, shapelessRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.CRAFTING_TABLE));
        }else if (recipe instanceof FurnaceRecipe) {
            FurnaceRecipe furnaceRecipe = (FurnaceRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = furnaceRecipe.getInput();
            inventory.setItem(1, ingredient);

            inventory.setItem(15, furnaceRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.FURNACE));
        }else if (recipe instanceof BlastingRecipe) {
            BlastingRecipe blastingRecipe = (BlastingRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = blastingRecipe.getInput();
            inventory.setItem(1, ingredient);

            inventory.setItem(15, blastingRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.BLAST_FURNACE));
        }else if (recipe instanceof SmokingRecipe) {
            SmokingRecipe smokingRecipe = (SmokingRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = smokingRecipe.getInput();
            inventory.setItem(1, ingredient);

            inventory.setItem(15, smokingRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.SMOKER));
        }else if (recipe instanceof StonecuttingRecipe) {
            StonecuttingRecipe stonecuttingRecipe = (StonecuttingRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = stonecuttingRecipe.getInput();
            inventory.setItem(1, ingredient);

            inventory.setItem(15, stonecuttingRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.STONECUTTER));
        }else if (recipe instanceof CampfireRecipe) {
            CampfireRecipe campfireRecipe = (CampfireRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = campfireRecipe.getInput();
            inventory.setItem(1, ingredient);

            inventory.setItem(15, campfireRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.CAMPFIRE));
        }else if (recipe instanceof SmithingRecipe) {
            SmithingRecipe smithingRecipe = (SmithingRecipe) recipe;

            // Rezept-Zutaten zum Inventar hinzufügen
            ItemStack ingredient = smithingRecipe.getBase().getItemStack();
            ItemStack ingredient2 = smithingRecipe.getAddition().getItemStack();

            inventory.setItem(1, ingredient);
            inventory.setItem(2, ingredient2);

            inventory.setItem(15, smithingRecipe.getResult());
            inventory.setItem(13, new ItemStack(Material.SMITHING_TABLE));
        }

        //füge allen items im inventar den localizedName hinzu "crafting"
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);
            if (itemStack != null){
                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setLocalizedName("crafting");
                itemStack.setItemMeta(itemMeta);
            }
        }

        // Spieler das Inventar anzeigen
        player.openInventory(inventory);
    }

}
