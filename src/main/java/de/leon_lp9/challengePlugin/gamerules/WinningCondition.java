package de.leon_lp9.challengePlugin.gamerules;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.gamerules.config.LoadGamerule;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.List;

@LoadGamerule
public class WinningCondition extends GameRule{

    public enum WinningConditions{
        EnderDragon,
        Wither,
        ElderGuardian,
        AllBossMobs;
    }

    @ConfigurationValue(title = "winningConditionMobType", icon = Material.WITHER_SKELETON_SKULL)
    private WinningConditions winningCondition;

    private List<EntityType> killedEntityTypes;

    public WinningCondition() {
        super(Material.DRAGON_HEAD, true);
        winningCondition = WinningConditions.EnderDragon;
        killedEntityTypes = new ArrayList<>();
    }

    @EventHandler
    public void onEntityDeathEvent(EntityDeathEvent e){
        EntityType entityType = e.getEntity().getType();
        if (winningCondition==WinningConditions.EnderDragon && entityType.equals(EntityType.ENDER_DRAGON)){
            Main.getInstance().getChallengeManager().getTimer().setResumed(false);
            Main.getInstance().getChallengeManager().sendChallengeDoneMessage();
        }else if (winningCondition==WinningConditions.Wither && entityType.equals(EntityType.WITHER)){
            Main.getInstance().getChallengeManager().getTimer().setResumed(false);
            Main.getInstance().getChallengeManager().sendChallengeDoneMessage();
        }else if (winningCondition==WinningConditions.ElderGuardian && entityType.equals(EntityType.ELDER_GUARDIAN)){
            Main.getInstance().getChallengeManager().getTimer().setResumed(false);
            Main.getInstance().getChallengeManager().sendChallengeDoneMessage();
        }else if (winningCondition==WinningConditions.AllBossMobs){
            if (    (entityType.equals(EntityType.ENDER_DRAGON)
                        || entityType.equals(EntityType.WITHER)
                        || entityType.equals(EntityType.ELDER_GUARDIAN)
                    ) && !killedEntityTypes.contains(entityType)){
                killedEntityTypes.add(entityType);
            }
            if (killedEntityTypes.size() == 3){
                Main.getInstance().getChallengeManager().getTimer().setResumed(false);
                Main.getInstance().getChallengeManager().sendChallengeDoneMessage();
            }
        }
    }

}
