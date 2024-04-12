package de.leon_lp9.challengePlugin.worldgeneration;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.worldgeneration.biomeProvider.SingleBiomeProvider;
import de.leon_lp9.challengePlugin.worldgeneration.populators.*;
import de.leon_lp9.challengePlugin.worldgeneration.worldGenerators.CustomChunkGenerator;
import de.leon_lp9.challengePlugin.worldgeneration.worldGenerators.TestGenerator;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.*;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class WorldGenerationManager implements Listener {

    @Getter
    public enum WorldGenerators{
        DEFAULT(null, Material.GRASS_BLOCK),
        FLAT(null, Material.BEDROCK),
        DEBUG(CustomChunkGenerator.class, Material.COMMAND_BLOCK),
        TEST(TestGenerator.class, Material.COMMAND_BLOCK);

        private final Class<? extends ChunkGenerator> generatorClass;
        private final Material icon;
        WorldGenerators(Class<? extends ChunkGenerator> generatorClass, Material icon) {
            this.generatorClass = generatorClass;
            this.icon = icon;
        }
    }

    @Getter
    public enum WorldPopulators{
        NONE(null, Material.BARRIER),
        SPACING_POPULATOR(SpacingPopulator.class, Material.STICK),
        CIRCLE_POPULATOR(CirclePopulator.class, Material.SNOWBALL),
        DIAGONAL_POPULATOR(DiagonalPopulator.class, Material.BLAZE_ROD),
        PLATTE_POPULATOR(PlattePopulator.class, Material.OAK_PRESSURE_PLATE),
        SPHERE_POPULATOR(SpherePopulator.class, Material.HEART_OF_THE_SEA),
        STAB_POPULATOR(StabPopulator.class, Material.END_ROD);
        //RANDOM_BLOCK_TYPE_POPULATOR(RandomBlockTypeChunkPopulator.class, Material.DIAMOND_BLOCK);

        private final Class<? extends BlockPopulator> populatorClass;
        private final Material icon;
        WorldPopulators(Class<? extends BlockPopulator> populatorClass, Material icon) {
            this.populatorClass = populatorClass;
            this.icon = icon;
        }
    }

    @Getter
    @Setter
    private WorldGenerators activeWorldGenerator;
    @Getter
    @Setter
    private WorldPopulators activeWorldPopulator;
    @Getter
    @Setter
    private Biome singleBiome;

    public WorldGenerationManager(){
        this.activeWorldGenerator = WorldGenerators.DEFAULT;
        this.activeWorldPopulator = WorldPopulators.NONE;
        this.singleBiome = null;
    }

    @SneakyThrows
    public void regenerateWorlds(Long seed){
        Main.getInstance().getChallengeManager().deactivateAllChallenges();
        Main.getInstance().getGameruleManager().resetGameRules();
        Main.getInstance().getChallengeManager().getTimer().setResumed(false);
        Main.getInstance().getChallengeManager().getTimer().setSeconds(0);
        Main.getInstance().getChallengeManager().getTimer().setState(Timer.TimerState.Countup);

        for (Player all : Bukkit.getOnlinePlayers()) {

            if (seed != null) {
                all.sendMessage(Main.getInstance().getTranslationManager().getTranslation(all, "resetMessageSeed").replace("%seed%", seed.toString()));
            }else {
                all.sendMessage(Main.getInstance().getTranslationManager().getTranslation(all, "resetMessage"));
            }

            //wenn tot
            all.spigot().respawn();

            all.teleport(Bukkit.getWorld("world").getSpawnLocation());
            all.getInventory().clear();
            all.getInventory().setArmorContents(null);
            all.setMaxHealth(20);
            all.setHealth(20);
            all.setFoodLevel(20);
            all.setExp(0);
            all.setLevel(0);
            all.setFireTicks(0);
            all.setSaturation(20);
            all.setFallDistance(0);
            all.setTotalExperience(0);
            all.setExhaustion(0);
            all.getEnderChest().clear();
            all.getActivePotionEffects().forEach(potionEffect -> all.removePotionEffect(potionEffect.getType()));
            all.setGameMode(GameMode.SURVIVAL);
            all.setFlying(false);
            all.setAllowFlight(false);

            Iterator<Advancement> iterator = Bukkit.getServer().advancementIterator();
            while (iterator.hasNext())
            {
                AdvancementProgress progress = all.getAdvancementProgress(iterator.next());
                for (String criteria : progress.getAwardedCriteria())
                    progress.revokeCriteria(criteria);
            }
        }

        //Welten löschen
        deleteWorld("ChallengeWorld");

        deleteWorld("ChallengeWorld_nether");

        deleteWorld("ChallengeWorld_the_end");

        Long finalSeed = seed;
        if (seed == null) {
            finalSeed = System.currentTimeMillis();
        }

        generateWorld(finalSeed, "ChallengeWorld", World.Environment.NORMAL);

        generateWorld(finalSeed, "ChallengeWorld_nether", World.Environment.NETHER);

        generateWorld(finalSeed, "ChallengeWorld_the_end", World.Environment.THE_END);

        //Spieler teleportieren
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.teleport(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

    private static void deleteWorld(String ChallengeWorld) {
        World world = Bukkit.getWorld(ChallengeWorld);
        world.setKeepSpawnInMemory(false);
        Bukkit.unloadWorld(world, false);
        Main.getInstance().getFileUtils().deleteDirectory(world.getWorldFolder());
    }

    @SneakyThrows
    public void generateWorld(Long seed, String worldName, World.Environment environment) {
        //Welten erstellen
        WorldCreator overworldCreator = new WorldCreator(worldName);
        overworldCreator.environment(environment);

        if (seed != null) {
            overworldCreator.seed(seed);
        } else {
            overworldCreator.seed(System.currentTimeMillis());
        }

        if (singleBiome != null) {
            SingleBiomeProvider singleBiomeProvider = new SingleBiomeProvider(singleBiome);
            overworldCreator.biomeProvider(singleBiomeProvider);
        }

        if (getActiveWorldGenerator().getGeneratorClass() != null) {
            overworldCreator.generator(activeWorldGenerator.getGeneratorClass().newInstance());
        }else if (getActiveWorldGenerator() == WorldGenerators.FLAT){
            overworldCreator.type(WorldType.FLAT);
        }

        Bukkit.createWorld(overworldCreator);
    }


    //Link worlds for the portals

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent event) {
        Location to = event.getTo();
        if(to.getWorld().getName().equals("world")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld"));
        }
        if(to.getWorld().getName().equals("world_nether")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld_nether"));
        }
        if(to.getWorld().getName().equals("world_the_end")) {
            to.setWorld(Bukkit.getWorld("ChallengeWorld_the_end"));
        }
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerPortal(EntityPortalEvent event) {
        Location to = event.getTo();
        if(to.getWorld().getName().equals("world")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld"));
        }
        if(to.getWorld().getName().equals("world_nether")){
            to.setWorld(Bukkit.getWorld("ChallengeWorld_nether"));
        }
        if(to.getWorld().getName().equals("world_the_end")) {
            to.setWorld(Bukkit.getWorld("ChallengeWorld_the_end"));
        }
        event.setTo(to);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals("world")) {
            event.getPlayer().teleport(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

    @EventHandler
    public void onRespawn(org.bukkit.event.player.PlayerRespawnEvent event) {
        if (event.getRespawnLocation().getWorld().getName().equals("world")) {
            event.setRespawnLocation(Bukkit.getWorld("ChallengeWorld").getSpawnLocation());
        }
    }

    @SneakyThrows
    @EventHandler
    public void onWorldInit(org.bukkit.event.world.WorldInitEvent event) {
        if (getActiveWorldPopulator().getPopulatorClass() != null){
            if (event.getWorld().getName().equals("ChallengeWorld")) {
                event.getWorld().getPopulators().add(getActiveWorldPopulator().getPopulatorClass().newInstance());
            }else if (event.getWorld().getName().equals("ChallengeWorld_nether")) {
                event.getWorld().getPopulators().add(getActiveWorldPopulator().getPopulatorClass().newInstance());
            }else if (event.getWorld().getName().equals("ChallengeWorld_the_end")) {
                event.getWorld().getPopulators().add(getActiveWorldPopulator().getPopulatorClass().newInstance());
            }
        }
    }
}
