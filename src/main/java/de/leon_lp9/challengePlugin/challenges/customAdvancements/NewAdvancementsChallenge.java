package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.Advancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

@LoadChallenge
public class NewAdvancementsChallenge extends Challenge {

    private transient AdvancementTab advancementTab;
    private transient UltimateAdvancementAPI api;

    public NewAdvancementsChallenge() {
        super(Material.KNOWLEDGE_BOOK, ChallengeType.MISC);
    }

    @Override
    public void register() {
        super.register();

        //check ob die api schon geladen ist
        if (!Bukkit.getPluginManager().isPluginEnabled("UltimateAdvancementAPI")) {
            plugin.getLogger().warning("UltimateAdvancementAPI is not enabled!");
            plugin.getLogger().warning("You can download it here: https://www.spigotmc.org/resources/ultimateadvancementapi-1-15-1-20-4.95585/");
            plugin.getLogger().warning("Deactivating challenge: " + this.getClass().getSimpleName());

            plugin.getChallengeManager().deactivateChallenge(this.getClass());
        }else {

            api = UltimateAdvancementAPI.getInstance(plugin);
            api.unregisterPluginAdvancementTabs();

            advancementTab = api.createAdvancementTab("new_advancements");

            AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.PLAYER_HEAD, "§6§lLeon_lp9 Production", AdvancementFrameType.TASK, true, true, 0, 0, "Der Anfang aller Dinge");
            RootAdvancement root = new RootAdvancement(advancementTab, "root", rootDisplay, "textures/block/pink_wool.png");

            FindAVillage findAVillage = new FindAVillage(root);
            ItsMLGTime itsMLGTime = new ItsMLGTime(findAVillage);
            WasEineAussicht wasEineAussicht = new WasEineAussicht(itsMLGTime);
            NewWorldRecord newWorldRecord = new NewWorldRecord(findAVillage);
            HalloweenVerkleidung halloweenVerkleidung = new HalloweenVerkleidung(findAVillage);
            Waschtag waschtag = new Waschtag(halloweenVerkleidung);
            TradingMarket tradingMarket = new TradingMarket(halloweenVerkleidung);
            OEffentlicherNahverkehr oEffentlicherNahverkehr = new OEffentlicherNahverkehr(tradingMarket);
            Durchbruch durchbruch = new Durchbruch(tradingMarket);
            SchuetzeDieNachberschaft schuetzeDieNachberschaft = new SchuetzeDieNachberschaft(waschtag);
            Bedwars bedwars = new Bedwars(tradingMarket);
            WWolfie6 wWolfie6 = new WWolfie6(findAVillage);
            PrivatsphaereBitte privatsphaereBitte = new PrivatsphaereBitte(findAVillage);

            advancementTab.registerAdvancements(root,
                    findAVillage,
                    itsMLGTime,
                    newWorldRecord,
                    halloweenVerkleidung,
                    waschtag,
                    tradingMarket,
                    schuetzeDieNachberschaft,
                    oEffentlicherNahverkehr,
                    durchbruch,
                    bedwars,
                    wWolfie6,
                    wasEineAussicht,
                    privatsphaereBitte
            );

            Bukkit.getOnlinePlayers().forEach(player -> {
                api.loadOfflinePlayer(player.getUniqueId());
                api.getAdvancementTab("new_advancements").showTab(player);
                advancementTab.grantRootAdvancement(player);
            });

        }

    }

    @Override
    public void unregister() {
        super.unregister();

        Bukkit.getOnlinePlayers().forEach(player -> {
            advancementTab.hideTab(player);

            //revokes all advancements
            advancementTab.getAdvancements().forEach(advancement -> {
                advancement.revoke(player);
            });

        });

    }

    @EventHandler
    public void onJoin(PlayerLoadingCompletedEvent e) {
        // Called after a player has successfully been loaded by the API
        Player p = e.getPlayer();
        // Here you can show tabs to players
        advancementTab.showTab(p);
    }
}
