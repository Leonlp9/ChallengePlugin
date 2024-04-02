package de.leon_lp9.challengePlugin.challenges.customAdvancements;

import com.fren_gor.ultimateAdvancementAPI.AdvancementTab;
import com.fren_gor.ultimateAdvancementAPI.UltimateAdvancementAPI;
import com.fren_gor.ultimateAdvancementAPI.advancement.RootAdvancement;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementDisplay;
import com.fren_gor.ultimateAdvancementAPI.advancement.display.AdvancementFrameType;
import com.fren_gor.ultimateAdvancementAPI.events.PlayerLoadingCompletedEvent;
import de.leon_lp9.challengePlugin.Main;
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

        //Kick all players from the server
        Bukkit.getOnlinePlayers().forEach(player -> player.kickPlayer("§cBei dieser Challenge musst du dich neu verbinden bei einem Reload!"));

        api = Main.getInstance().getUltimateAdvancementAPI();
        api.unregisterAdvancementTab("new_advancements");

        advancementTab = api.createAdvancementTab("new_advancements");

        AdvancementDisplay rootDisplay = new AdvancementDisplay(Material.PLAYER_HEAD, "§6§lLeon_lp9 Production", AdvancementFrameType.TASK, true, true, 0, 10f, "Der Anfang aller Dinge");
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
        OverTheRainbow overTheRainbow = new OverTheRainbow(itsMLGTime);
        EinsPunktSiebzehn einsPunktSiebzehn = new EinsPunktSiebzehn(itsMLGTime);
        LebenUntermLimit lebenUntermLimit = new LebenUntermLimit(einsPunktSiebzehn);
        Pyrotechniker pyrotechniker = new Pyrotechniker(wWolfie6);
        Schmelzmeister schmelzmeister = new Schmelzmeister(wWolfie6);

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
                privatsphaereBitte,
                overTheRainbow,
                einsPunktSiebzehn,
                lebenUntermLimit,
                pyrotechniker,
                schmelzmeister
        );

        advancementTab.automaticallyShowToPlayers();
        advancementTab.automaticallyGrantRootAdvancement();
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
        Player p = e.getPlayer();
        advancementTab.showTab(p);
    }
}
