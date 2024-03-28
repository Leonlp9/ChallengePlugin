package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.Timer;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurableField;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.*;

@Getter
public class Challenge implements Listener {

    public enum ChallengeType {
        MOVEMENT(Material.FEATHER),
        BLOCKS(Material.STONE),
        DAMAGE(Material.REDSTONE),
        RANDOMIZER(Material.CHAIN_COMMAND_BLOCK),
        ZWISCHENZIELE(Material.WHITE_BANNER),
        ENTITYS(Material.PIG_SPAWN_EGG),
        INVENTORY(Material.CHEST),
        EXTRA_WORLD(Material.GRASS_BLOCK),
        MISC(Material.HAY_BLOCK),
        MINIGAME(Material.SLIME_BLOCK);

        private Material icon;
        private ChallengeType(Material icon) {
            this.icon = icon;
        }

        public String getTranslationName(Player player) {
            return Main.getInstance().getTranslationManager().getTranslation(player, this.name()+"_ChallengeTypeName");
        }

        public String getTranslationDescription(Player player) {
            return Main.getInstance().getTranslationManager().getTranslation(player, this.name()+"_ChallengeTypeDescription");
        }

        public Material getIcon() {
            return icon;
        }
    }

    private transient final String name;
    private transient final String description;
    private transient final Material icon;
    @Getter
    private final transient ChallengeType type;
    protected transient final Main plugin;

    private transient List<BossBarInformationTile> globalBossBarInformationTiles = new ArrayList<>();
    private transient Map<Player, ArrayList<BossBarInformationTile>> playerBossBarInformationTiles = new HashMap<>();

    @Setter
    @Getter
    private transient Collection<ConfigurableField> configurableFields;

    public Challenge(Material icon, ChallengeType type) {
        this.name = this.getClass().getSimpleName() + "Name";
        this.description = this.getClass().getSimpleName() + "Description";
        this.icon = icon;
        this.type = type;
        this.plugin = Main.getInstance();

        Main.getInstance().getConfigurationReader().readConfigurableFields(this);
    }

    public void register() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void unregister() {
        HandlerList.unregisterAll(this);

        globalBossBarInformationTiles.forEach(tile -> plugin.getBossBarInformation().removeTile(tile.getKey()));
        playerBossBarInformationTiles.forEach((player, tiles) -> tiles.forEach(tile -> plugin.getBossBarInformation().removeTile(player, tile.getKey())));

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
    public void unload(){}
    public void timerFirstTimeResume(){}
    public void skipIfIsPossible(){}

    public String getTranslationName(Player player) {
        return Main.getInstance().getTranslationManager().getTranslation(player, name);
    }

    public void addGlobalBossBarInformationTile(BossBarInformationTile tile) {
        globalBossBarInformationTiles.add(tile);
        plugin.getBossBarInformation().addTile(tile);
        plugin.getBossBarInformation().update();
    }

    public void addPlayerBossBarInformationTile(Player player, BossBarInformationTile tile) {
        if (!playerBossBarInformationTiles.containsKey(player)) {
            playerBossBarInformationTiles.put(player, new ArrayList<>());
        }
        playerBossBarInformationTiles.get(player).add(tile);
        plugin.getBossBarInformation().addTile(player, tile);
    }

    public void removeGlobalBossBarInformationTile(String key) {
        globalBossBarInformationTiles.removeIf(tile -> tile.getKey().equals(key));
        plugin.getBossBarInformation().removeTile(key);
    }

    public void removePlayerBossBarInformationTile(Player player, String key) {
        if (playerBossBarInformationTiles.containsKey(player)) {
            playerBossBarInformationTiles.get(player).removeIf(tile -> tile.getKey().equals(key));
            plugin.getBossBarInformation().removeTile(player, key);
        }
    }

    public BossBarInformationTile getGlobalBossBarInformationTile(String key) {
        return globalBossBarInformationTiles.stream().filter(tile -> tile.getKey().equals(key)).findFirst().orElse(null);
    }

    public BossBarInformationTile getPlayerBossBarInformationTile(Player player, String key) {
        if (playerBossBarInformationTiles.containsKey(player)) {
            return playerBossBarInformationTiles.get(player).stream().filter(tile -> tile.getKey().equals(key)).findFirst().orElse(null);
        }
        return null;
    }

}
