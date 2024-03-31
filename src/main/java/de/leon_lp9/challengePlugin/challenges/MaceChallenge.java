package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.builder.ItemBuilder;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

@LoadChallenge
public class MaceChallenge extends Challenge{

    public transient final ItemStack MACE = new ItemBuilder(Material.IRON_AXE)
            .setDisplayName("§cMace")
            .setLore("§7This is a Mace")
            .setCustomModelData(1)
            .addPersistentDataContainer("mace", PersistentDataType.STRING, "mace")
            .build();

    public MaceChallenge() {
        super(Material.IRON_AXE, ChallengeType.MISC);
    }

    @Override
    public void register() {
        super.register();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getInventory().addItem(MACE);
        }

    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onEntityDamageByEntityEvent(org.bukkit.event.entity.EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player player){
            if (player.getInventory().getItemInMainHand().getItemMeta().getPersistentDataContainer().has(new org.bukkit.NamespacedKey(Main.getInstance(), "mace"), PersistentDataType.STRING)) {

                //spieler vector y umkehren
                player.setVelocity(new Vector(player.getVelocity().getX(), -player.getVelocity().getY() * 1.25 + 0.75, player.getVelocity().getZ()));

                event.setDamage(10.5 + 5.25 * player.getFallDistance());


                if (player.getFallDistance() > 2) {
                    Location loc = event.getEntity().getLocation();
                    loc.setY(loc.getY() + 0.5);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 200, 1.5, 0.5, 1.5, 0, Material.DIRT.createBlockData());
                    loc.getWorld().spawnParticle(Particle.BLOCK_DUST, loc, 200, 1.5, 0.5, 1.5, 0, Material.DIRT.createBlockData());

                    event.getEntity().getNearbyEntities(3, 1, 3).forEach(entity -> {
                        if (!(entity instanceof Player)) {

                            //alle entities in der nähe von dem geschlagenen entity wegstoßen

                            double x = entity.getLocation().getX() - event.getEntity().getLocation().getX();
                            double y = entity.getLocation().getY() - event.getEntity().getLocation().getY();
                            double z = entity.getLocation().getZ() - event.getEntity().getLocation().getZ();

                            Vector vector = new Vector(x, y, z).normalize().multiply(1.5);
                            vector.setY(0.5);
                            entity.setVelocity(vector);

                        }
                    });

                }

                player.setFallDistance(0);
            }

        }
    }

}
