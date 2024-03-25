package de.leon_lp9.challengePlugin.builder;

import de.leon_lp9.challengePlugin.Main;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class ItemBuilder {

    ItemStack itemStack;
    ItemMeta itemMeta;

    public ItemBuilder(Material material){
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    ItemBuilder(Material material, int amount){
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setDisplayName(String displayName){
        itemMeta.setDisplayName(displayName);
        return this;
    }

    public ItemBuilder setLocalizedName(String translationKey){
        itemMeta.setLocalizedName(translationKey);
        return this;
    }

    public ItemBuilder setLore(String... lore){
        itemMeta.setLore(java.util.Arrays.asList(lore));
        return this;
    }
    public ItemBuilder setLore(ArrayList lore){
        itemMeta.setLore(lore);
        return this;
    }

    public ItemBuilder setAmount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public void addLineToLore(String line){
        ArrayList<String> lore = new ArrayList<>(itemMeta.getLore());
        lore.add(line);
        itemMeta.setLore(lore);
    }

    public ItemBuilder setUnbreakable(boolean unbreakable){
        itemMeta.setUnbreakable(unbreakable);
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData){
        itemMeta.setCustomModelData(customModelData);
        return this;
    }

    public ItemBuilder addEnchant(org.bukkit.enchantments.Enchantment enchantment, int level){
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemBuilder addAttributeModifier(org.bukkit.attribute.Attribute attribute, org.bukkit.attribute.AttributeModifier attributeModifier){
        itemMeta.addAttributeModifier(attribute, attributeModifier);
        return this;
    }

    public ItemBuilder addPersistentDataContainer(String namespacedKey, PersistentDataType persistentDataType, Object object){
        itemMeta.getPersistentDataContainer().set(new NamespacedKey(Main.getInstance(), namespacedKey), persistentDataType, object);
        return this;
    }

    public ItemBuilder addFlag(org.bukkit.inventory.ItemFlag itemFlag){
        itemMeta.addItemFlags(itemFlag);
        return this;
    }

    public ItemStack build(){
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
