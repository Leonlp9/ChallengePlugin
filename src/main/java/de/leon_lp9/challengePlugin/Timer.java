package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import de.leon_lp9.challengePlugin.challenges.Challenge;
import de.leon_lp9.challengePlugin.management.BossBarInformationTile;
import de.leon_lp9.challengePlugin.management.Spacing;
import lombok.*;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;

import java.awt.Color;
import java.lang.management.ManagementFactory;

@Data
public class Timer {

    public enum TimerState {
        Countdown,
        Countup
    }

    public enum DisplayType {
        ActionBar,
        BossBar,
        None
    }

    private int seconds;
    private transient int fadeStep = 0;
    private boolean resumed;
    private TimerState state;
    @Getter
    private DisplayType displayType;

    private transient int task;
    private transient int fadeTask;

    private Color firstColor = new Color(207, 62, 229);
    private Color secondColor = new Color(128, 11, 146);
    private boolean bold = true;
    private boolean background = true;

    public Timer(int seconds) {
        this.seconds = seconds;
        resumed = false;
        state = TimerState.Countup;
        displayType = DisplayType.ActionBar;
    }

    public void startTask() {
        if (task != 0) {
            Bukkit.getScheduler().cancelTask(task);
        }
        task = Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            if (resumed) {
                if (state == TimerState.Countdown) {
                    if (seconds > 0) {
                        seconds--;
                    }
                } else {
                    seconds++;
                }

                Main.getInstance().getChallengeManager().getActiveChallenges().forEach(challenge -> challenge.timerTick(seconds));
            }

        }, 0, 20).getTaskId();

        if (fadeTask != 0) {
            Bukkit.getScheduler().cancelTask(fadeTask);
        }
        fadeTask = Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            fadeStep = (fadeStep + 1) % 40;
            if ((seconds >= 0 && state == TimerState.Countdown) || state == TimerState.Countup) {
                sendActionBar();
            }
            Main.getInstance().getBossBarInformation().update();
            Main.getInstance().getChallengeManager().getActiveChallenges().forEach(Challenge::tick);
        }, 0, 1).getTaskId();
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
        if (displayType == DisplayType.BossBar){
            if (!Main.getInstance().getBossBarInformation().hasTile("timer")) {
                Main.getInstance().getBossBarInformation().addTile(new BossBarInformationTile("timer", getFormattedTime(), null, Spacing.POSITIVE32PIXEl, 1));
            }
        } else {
            Main.getInstance().getBossBarInformation().removeTile("timer");
        }
    }

    public void sendActionBar() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ColorBuilder colorBuilder = new ColorBuilder(getFormattedTime() + (!resumed ? " | " + Main.getInstance().getTranslationManager().getTranslation(player, "paused") : "")).
            addColorGradientToString(firstColor, secondColor, fadeStep, 40, Main.getInstance().getChallengeManager().getTimer().bold);
            if (background && displayType == DisplayType.ActionBar) {
                colorBuilder.addBackground();
            }
            if (displayType == DisplayType.ActionBar) {
                player.sendActionBar(colorBuilder.getText());
            }else if (displayType == DisplayType.BossBar){
                if (!Main.getInstance().getBossBarInformation().hasTile("timer")) {
                    setDisplayType(DisplayType.BossBar);
                }
                Main.getInstance().getBossBarInformation().getTile("timer").setTitle(colorBuilder.getText());
            }

            //Get Server TPS, Cpu Usage, Memory Usage, and Player Count
            double tps = Bukkit.getServer().getTPS()[0];
            double cpu = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getCpuLoad() * 100;
            double mem = ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalMemorySize() - ((com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreeMemorySize();

            //round
            tps = Math.round(tps * 100.0) / 100.0;
            cpu = Math.round(cpu * 100.0) / 100.0;

            //convert bytes to gb and round
            mem = Math.round((mem / 1024 / 1024 / 1024) * 100.0) / 100.0;

            int playerCount = Bukkit.getServer().getOnlinePlayers().size();

            if (!Main.getInstance().getConfig().contains("useTablist")) {
                Main.getInstance().getConfig().set("useTablist", true);
                Main.getInstance().saveConfig();
            }
            if (Main.getInstance().getConfig().getBoolean("useTablist")) {
                player.setPlayerListHeaderFooter("\uDAC0\uDC31\n\n\n\n\n\n", "\n§7TPS: §b" + tps + "§7 | CPU: §b" + cpu + "%§7 | RAM: §b" + mem + "GB§7 | Players: §b" + playerCount + "\n");
            }

        });
    }

    public String getFormattedTime() {
        int days = seconds / 86400;
        int hours = seconds / 3600 % 24;
        int minutes = seconds / 60 % 60;
        int seconds = this.seconds % 60;

        if (days > 0) {
            return String.format("%02dd %02dh %02dm %02ds", days, hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format("%02dh %02dm %02ds", hours, minutes, seconds);
        } else if (minutes > 0) {
            return String.format("%02dm %02ds", minutes, seconds);
        } else {
            return String.format("%02ds", seconds);
        }
    }

    public void stopTask() {
        Bukkit.getScheduler().cancelTask(task);
        Bukkit.getScheduler().cancelTask(fadeTask);
    }

}
