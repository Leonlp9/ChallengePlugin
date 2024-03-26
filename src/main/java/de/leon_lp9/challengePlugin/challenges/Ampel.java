package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

@LoadChallenge
public class Ampel extends Challenge {

    public enum AmpelStatus {
        ROT("\uDAC0\uDC36"),
        GELB("\uDAC0\uDC38"),
        GRUEN("\uDAC0\uDC37");

        private final String title;
        AmpelStatus(String title){
            this.title = title;
        }

        public String getTitle(){
            return title;
        }
    }
    private transient BossBar bossBar;

    @ConfigurationValue(title = "AmpelRotPhaseMillisName", icon = Material.REDSTONE_TORCH, min = 1, max = 10000, step = 100)
    private int ampelRotPhaseMillis = 5000;

    @ConfigurationValue(title = "AmpelGelbPhaseMillisName", icon = Material.REDSTONE_TORCH, min = 1, max = 10000, step = 100)
    private int ampelGelbPhaseMillis = 2000;

    //Es muss mindestens 60 Sekunden rum sein, bevor die Ampel wieder auf gelb springen kann nach wahrscheinlichkeit
    @ConfigurationValue(title = "GruenSchonPhaseSecsName", icon = Material.REDSTONE_TORCH, min = 10, max = 10000, step = 60)
    private int gruenSchonPhaseSecs = 60;

    @ConfigurationValue(title = "WahrscheinlichkeitFuerVonGruenNachGelbName", icon = Material.REDSTONE_TORCH, min = 1, max = 100)
    public int wahrscheinlichkeitFuerVonGruenNachGelb = 5;

    private transient long lastSwitch = System.currentTimeMillis();

    private AmpelStatus status;

    public Ampel() {
        super(Material.REDSTONE, ChallengeType.MOVEMENT);
        status = AmpelStatus.GRUEN;
    }

    @Override
    public void register(){
        super.register();
        bossBar = Bukkit.createBossBar("Ampel Init", BarColor.WHITE, BarStyle.SOLID);
        bossBar.setProgress(0);
        Bukkit.getOnlinePlayers().forEach(player -> {
            bossBar.addPlayer(player);
        });
        bossBar.setVisible(true);
        setTitle();
    }

    @Override
    public void unload(){
        super.unload();
        bossBar.removeAll();
    }

    @Override
    public void unregister(){
        super.unregister();
        bossBar.removeAll();
    }

    @Override
    public void tick(){
        super.tick();

        if (status == AmpelStatus.ROT && System.currentTimeMillis() - lastSwitch > ampelRotPhaseMillis){
            switchStatus();
            lastSwitch = System.currentTimeMillis();
        } else if (status == AmpelStatus.GELB && System.currentTimeMillis() - lastSwitch > ampelGelbPhaseMillis){
            switchStatus();
            lastSwitch = System.currentTimeMillis();
        } else if (status == AmpelStatus.GRUEN && System.currentTimeMillis() - lastSwitch > gruenSchonPhaseSecs * 1000 && Math.random() * 100 < (double) wahrscheinlichkeitFuerVonGruenNachGelb / 20){
            switchStatus();
            lastSwitch = System.currentTimeMillis();
        }
    }

    private void setTitle(){
        bossBar.setTitle(status.getTitle());
    }

    public void switchStatus(){
        switch (status){
            case ROT:
                status = AmpelStatus.GRUEN;
                break;
            case GELB:
                status = AmpelStatus.ROT;
                break;
            case GRUEN:
                status = AmpelStatus.GELB;
                break;
        }
        setTitle();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (!isRunning()){
            return;
        }
        if (status == AmpelStatus.ROT && (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ() || event.getFrom().getY() != event.getTo().getY())){
            event.getPlayer().setHealth(0);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        bossBar.addPlayer(event.getPlayer());
    }

}
