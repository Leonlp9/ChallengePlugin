package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

@LoadChallenge
public class RandomEffectInEveryChunk extends Challenge{

    private transient HashMap<Chunk, PotionEffectType> chunkEffect = new HashMap<>();
    private transient HashMap<Chunk, Integer> chunkEffectStrength = new HashMap<>();

    public RandomEffectInEveryChunk() {
        super(Material.POTION, ChallengeType.RANDOMIZER);
    }

    @Override
    public void unload() {
        super.unload();

        getAllSurvivalPlayers().forEach(player -> {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (chunkEffect.containsKey(player.getLocation().getChunk())) {
                    player.removePotionEffect(chunkEffect.get(player.getLocation().getChunk()));
                }
            });
        });

    }

    @Override
    public void unregister() {
        super.unregister();

        getAllSurvivalPlayers().forEach(player -> {
            player.getActivePotionEffects().forEach(potionEffect -> {
                if (chunkEffect.containsKey(player.getLocation().getChunk())) {
                    player.removePotionEffect(chunkEffect.get(player.getLocation().getChunk()));
                }
            });
        });

    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (isPlayerInChallenge(event.getPlayer())) {
            if (!isRunning()) {
                return;
            }
            Chunk oldChunk = event.getFrom().getChunk();
            Chunk newChunk = event.getTo().getChunk();

            Random random = new Random(event.getPlayer().getLocation().getWorld().getSeed() + (long) newChunk.getX() * newChunk.getZ());

            if (!oldChunk.equals(newChunk) || chunkEffect.isEmpty()) {
                if (!chunkEffect.containsKey(newChunk)) {
                    ArrayList<PotionEffectType> potionEffectTypes = new ArrayList<>(PotionEffectType.values().length);
                    potionEffectTypes.addAll(Arrays.asList(PotionEffectType.values()));
                    potionEffectTypes.remove(PotionEffectType.SATURATION);
                    potionEffectTypes.remove(PotionEffectType.HEAL);
                    potionEffectTypes.remove(PotionEffectType.HARM);
                    PotionEffectType randomEffect = potionEffectTypes.get(random.nextInt(potionEffectTypes.size()));
                    chunkEffect.put(newChunk, randomEffect);
                    chunkEffectStrength.put(newChunk, random.nextInt(3));

                    if (chunkEffect.containsKey(oldChunk)) event.getPlayer().removePotionEffect(chunkEffect.get(oldChunk));
                    event.getPlayer().addPotionEffect(randomEffect.createEffect(1000000, chunkEffectStrength.get(newChunk)));
                }else {
                    if (chunkEffect.containsKey(oldChunk)) event.getPlayer().removePotionEffect(chunkEffect.get(oldChunk));
                    event.getPlayer().addPotionEffect(chunkEffect.get(newChunk).createEffect(1000000, chunkEffectStrength.get(newChunk)));
                }
            }
        }
    }


}
