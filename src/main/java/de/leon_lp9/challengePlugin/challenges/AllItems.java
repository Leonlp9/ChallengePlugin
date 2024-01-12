package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@LoadChallenge
public class AllItems extends Challenge {

    private ArrayList<Material> allMaterialsRandomized;
    private ArrayList<Material> allCollectedItems;

    public AllItems() {
        super(Material.ENDER_CHEST);
        allCollectedItems = new ArrayList<>();

        //allMaterialsRandomized = (ArrayList<Material>) Arrays.stream(Material.values()).toList();
        //allMaterialsRandomized.removeIf(material -> material.isAir() || material.isLegacy());
        //shuffel the list
        //Collections.shuffle(allMaterialsRandomized);
    }

    public void nextItem(){
        allCollectedItems.add(allMaterialsRandomized.get(0));
        allMaterialsRandomized.remove(0);
    }

    @EventHandler
    public void onPickUpItemEvent(PlayerPickupItemEvent event){
        if (event.getItem().getType().equals(allMaterialsRandomized.get(0))) {
            nextItem();
        }
    }



}
