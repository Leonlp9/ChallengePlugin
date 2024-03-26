package de.leon_lp9.challengePlugin.commands;

import de.leon_lp9.challengePlugin.command.MinecraftCommand;
import de.leon_lp9.challengePlugin.command.Run;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;

@MinecraftCommand(name = "test", description = "nur ein test command")
public class TestCommand {

    @Run
    public boolean command(String[] strings, CommandSender commandSender) {
        Location location = ((Player) commandSender).getLocation();

        for (int pitch = -22; pitch < 22; pitch++) {
            for (int yaw = -45; yaw < 45; yaw++) {
                Location clone = location.clone();
                clone.setPitch(pitch * 4);
                clone.setYaw(yaw * 4);
                Arrow arrow = clone.getWorld().spawnArrow(clone, clone.getDirection(), 1, 0);
                arrow.setVelocity(clone.getDirection().multiply(10));
                arrow.setGravity(false);
            }
        }
        return true;
    }
    
}
