package de.leon_lp9.challengePlugin;

import de.leon_lp9.challengePlugin.builder.ColorBuilder;
import lombok.*;
import org.bukkit.Bukkit;
import java.awt.Color;

@Data
public class Timer {

    public enum TimerState {
        Countdown,
        Countup
    }

    private int seconds;
    private transient int fadeStep = 0;
    private boolean resumed;
    private TimerState state;

    private transient int task;
    private transient int fadeTask;

    private Color firstColor = new Color(207, 62, 229);
    private Color secondColor = new Color(128, 11, 146);
    private boolean bold = true;

    public Timer(int seconds) {
        this.seconds = seconds;
        resumed = false;
        state = TimerState.Countup;
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
            }

        }, 0, 20).getTaskId();

        if (fadeTask != 0) {
            Bukkit.getScheduler().cancelTask(fadeTask);
        }
        fadeTask = Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), () -> {
            fadeStep = (fadeStep + 1) % 20;
            if ((seconds >= 0 && state == TimerState.Countdown) || state == TimerState.Countup) {
                sendActionBar();
            }
        }, 0, 2).getTaskId();
    }

    public void sendActionBar() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            ColorBuilder colorBuilder = new ColorBuilder(getFormattedTime() + (!resumed ? " | PAUSED" : "")).
            addColorGradientToString(firstColor, secondColor, fadeStep, 10, true);
            player.sendActionBar(colorBuilder.getText());
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

}
