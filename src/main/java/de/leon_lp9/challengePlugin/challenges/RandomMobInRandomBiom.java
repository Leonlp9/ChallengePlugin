package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.material.SpawnEgg;

import java.util.ArrayList;
import java.util.Random;

@LoadChallenge
public class RandomMobInRandomBiom extends Challenge {

    @ConfigurationValue(title = "RandomMobChillTime", description = "RandomMobChillTimeDescription", icon = Material.CLOCK)
    private int chillTimeInMinutes = 4;

    @ConfigurationValue(title = "RandomMobCatchTime", description = "RandomMobCatchTimeDescription", icon = Material.CLOCK)
    private int catchTimeInMinutes = 12;

    @ConfigurationValue(title = "RandomMobCatchEggs", description = "RandomMobCatchEggsDescription", icon = Material.EGG)
    private int catchEggs = 5;

    private Biome biome;
    private EntityType entityType;
    private boolean finished = true;

    private transient ItemStack elytra = new ItemBuilder(Material.ELYTRA).addEnchant(Enchantment.DURABILITY, 3).setUnbreakable(true).addFlag(ItemFlag.HIDE_ENCHANTS).build();
    private transient ItemStack eggs = new ItemBuilder(Material.EGG).setDisplayName("§cPoké§fball").build();
    private transient BossBar bossBar;

    public RandomMobInRandomBiom() {
        super(Material.ALLAY_SPAWN_EGG);
        biome = null;
        entityType = null;
    }

    @Override
    public void register(){
        super.register();

        bossBar = Bukkit.createBossBar("RandomMobInRandomBiom", BarColor.BLUE, BarStyle.SOLID);

        Bukkit.getOnlinePlayers().forEach(player -> {
            bossBar.addPlayer(player);
        });
        bossBar.setVisible(true);
        setTitle();
    }

    @Override
    public void unload(){
        super.unload();

        if (bossBar == null) return;
        bossBar.removeAll();
        bossBar.setVisible(false);
        bossBar = null;
    }
    @Override
    public void unregister(){
        super.unregister();

        if (bossBar == null) return;
        bossBar.removeAll();
        bossBar.setVisible(false);
        bossBar = null;

    }

    @Override
    public void timerFirstTimeResume() {
        super.timerFirstTimeResume();

        eggs.setAmount(catchEggs);
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getInventory().addItem(elytra);
            player.getInventory().addItem(eggs);
        });
    }

    //Wenn Entitys mit dem Pokéball abgeworfen werden, dann soll derren Spawn Eggs an der stelle erscheinen und das Entity soll verschwinden
    @EventHandler
    public void onPlayerDropItem(PlayerEggThrowEvent event) {
        if (event.getEgg().getItem().getItemMeta() == null || !event.getEgg().getItem().getItemMeta().hasDisplayName()) return;
        if (event.getEgg().getItem().getItemMeta().getDisplayName().equals("§cPoké§fball")) {
            event.setHatching(false);

            //get Nearby Entities and remove them
            event.getEgg().getWorld().getNearbyEntities(event.getEgg().getLocation(), 1, 1, 1).forEach(this::dropSpawnEgg);
        }
    }

    @Override
    public void timerTick(int second){
        super.timerTick(second);

        setTitle();
    }

    public void setTitle(){
        int second = Main.getInstance().getChallengeManager().getTimer().getSeconds() - chillTimeInMinutes * 60 + catchTimeInMinutes * 60;
        int time = (second % (catchTimeInMinutes * 60));

        if (time == 0){
            if (!finished){
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setHealth(0);
                });
            }

            finished = false;
            newMob();
        }

        if (finished) {
            bossBar.setColor(BarColor.GREEN);
            bossBar.setProgress((double) time / (double) (catchTimeInMinutes * 60));
            bossBar.setTitle("§a✔");
        } else {
            if (biome == null || entityType == null){
                newMob();
            }

            bossBar.setColor(BarColor.RED);
            bossBar.setProgress((double) time / (double) (catchTimeInMinutes * 60));
            bossBar.setTitle("§f§lTöte §d§l" + entityType.name().replace("_", " ") + "§f§l in §d§l" + biome.name().replace("_", " "));
        }
    }

    public void newMob(){
        Random random = new Random();
        biome = Biome.values()[random.nextInt(Biome.values().length)];
        entityType = EntityType.values()[random.nextInt(EntityType.values().length)];

        while (!entityType.isAlive() || entityType.equals(EntityType.PLAYER) || entityType.equals(EntityType.ENDER_DRAGON) || entityType.equals(EntityType.DROPPED_ITEM) || entityType.equals(EntityType.EGG) || entityType.equals(EntityType.ARROW) || entityType.equals(EntityType.ILLUSIONER)){
            entityType = EntityType.values()[random.nextInt(EntityType.values().length)];
        }

        Bukkit.getOnlinePlayers().forEach(player -> {
            TextComponent textComponent = new TextComponent("Töte ");
            textComponent.setColor(net.md_5.bungee.api.ChatColor.WHITE);

            TextComponent entity = new TextComponent(Main.getInstance().getTranslationManager().getTranslation(entityType));
            entity.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
            entity.setBold(true);

            TextComponent in = new TextComponent(" in ");
            in.setColor(net.md_5.bungee.api.ChatColor.WHITE);

            TextComponent cBiome = new TextComponent(Main.getInstance().getTranslationManager().getTranslation(biome));
            cBiome.setColor(net.md_5.bungee.api.ChatColor.DARK_PURPLE);
            cBiome.setBold(true);

            textComponent.addExtra(entity);
            textComponent.addExtra(in);
            textComponent.addExtra(cBiome);

            player.spigot().sendMessage(textComponent);
        });
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent event){
        if (!isRunning()) return;
        if (event.getEntity().getType().equals(entityType) && event.getEntity().getKiller() != null){
            Biome biome = event.getEntity().getLocation().getBlock().getBiome();
            if (biome.equals(this.biome) && !finished){
                finished = true;

                setTitle();
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                });
            }
        }
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        bossBar.addPlayer(player);
    }

    @EventHandler
    public void onEntityDammageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!isRunning()) return;
        if (event.getDamager() instanceof Egg egg){
            if (egg.getItem().getItemMeta() == null || !egg.getItem().getItemMeta().hasDisplayName()) return;
            if (egg.getItem().getItemMeta().getDisplayName().equals("§cPoké§fball")) {

                dropSpawnEgg(event.getEntity());
            }
        }
    }

    public void dropSpawnEgg(Entity entity){
        if (entity.getType().equals(EntityType.ENDER_DRAGON)) return;
        if (entity.getType().equals(EntityType.PLAYER)) return;
        if (entity.getType().equals(EntityType.DROPPED_ITEM)) return;
        if (entity.getType().equals(EntityType.EGG)) return;
        if (entity.getType().equals(EntityType.ARROW)) return;

        ItemStack spawnEgg = new ItemStack(Material.WOLF_SPAWN_EGG);
        SpawnEggMeta spawnEggMeta = (SpawnEggMeta) spawnEgg.getItemMeta();
        spawnEggMeta.setSpawnedEntity(entity.createSnapshot());

        TextComponent tc = new TextComponent("");
        tc.setItalic(false);

        spawnEggMeta.setDisplayNameComponent(new TextComponent[]{tc, Main.getInstance().getTranslationManager().getTranslation(entity.getType())});
        spawnEgg.setItemMeta(spawnEggMeta);

        Entity entity1 = entity.getWorld().dropItemNaturally(entity.getLocation(), spawnEgg);
        entity1.setGlowing(true);

        entity.remove();
    }

}
