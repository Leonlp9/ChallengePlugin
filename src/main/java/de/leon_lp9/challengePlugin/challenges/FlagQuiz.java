package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.Flags;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;

@LoadChallenge
public class FlagQuiz extends Challenge {

    private transient TextDisplay textDisplay;
    private UUID selectedPlayerUUID;
    private Flags currentFlag;

    @ConfigurationValue(title = "multipleChoice", icon = Material.PAPER)
    private boolean multipleChoice = true;

    @ConfigurationValue(title = "resolution", icon = Material.MAP, min = 10, max = 120)
    private int resolution = 60;

    @ConfigurationValue(title = "minWaitTime", icon = Material.CLOCK, min = 10, max = 300)
    private int minWaitTime = 60;

    @ConfigurationValue(title = "maxWaitTime", icon = Material.CLOCK, min = 10, max = 300)
    private int maxWaitTime = 300;

    private int nextFlagTimer;

    public FlagQuiz() {
        super(Material.MAP, ChallengeType.ZWISCHENZIELE);

        setNextRandomTime();
    }

    public void setNextRandomTime(){
        Random random = new Random();
        nextFlagTimer = random.nextInt(maxWaitTime)+minWaitTime;
    }

    @Override
    public void update(){
        super.update();
        setNextRandomTime();
    }

    @Override
    public void register() {
        super.register();

        if (nextFlagTimer <= 0) {
            newFlag();
        }
    }

    @Override
    public void unload() {
        super.unload();

        if (textDisplay != null){
            textDisplay.remove();
        }
    }
    @Override
    public void unregister() {
        super.unregister();

        if (textDisplay != null){
            textDisplay.remove();
        }
    }

    @Override
    public void skipIfIsPossible() {
        super.skipIfIsPossible();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage("Es war die " + currentFlag.getName() + " Flagge");
        });
        newFlag();
    }

    public void newFlag(){
        if (textDisplay != null){
            textDisplay.remove();
        }


        currentFlag = Flags.getRand();

        //Get Random Player that is in Survival
        Player selectedPlayer = null;
        ArrayList<Player> players = new ArrayList<>(getAllSurvivalPlayers());
        selectedPlayer = players.get(new Random().nextInt(players.size()));

        if (selectedPlayer == null) return;
        selectedPlayerUUID = selectedPlayer.getUniqueId();
        plugin.getChallengeManager().getTimer().setResumed(false);

        textDisplay = spawnFlagTextDisplay(currentFlag, selectedPlayer.getEyeLocation());

        if (multipleChoice) {
            ArrayList<String> answers = new ArrayList<>();
            answers.add(currentFlag.getName());
            answers.add(Flags.getRand().getName());
            answers.add(Flags.getRand().getName());
            answers.add(Flags.getRand().getName());
            answers.add(Flags.getRand().getName());
            answers.add(Flags.getRand().getName());

            Collections.shuffle(answers);

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendMessage("§7Welche Flagge ist das?");
                answers.forEach(answer -> {
                    TextComponent tc = new TextComponent("- " + answer);
                    tc.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, answer));
                    tc.setHoverEvent(new net.md_5.bungee.api.chat.HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new net.md_5.bungee.api.chat.ComponentBuilder("§7Klicke um für " + answer + " zu antworten").create()));
                    player.spigot().sendMessage(tc);
                });
            });
        }
    }

    public void correctAnswer(){
        if (textDisplay != null){
            textDisplay.remove();
        }
        plugin.getChallengeManager().getTimer().setResumed(true);
        selectedPlayerUUID = null;
        setNextRandomTime();
    }

    public TextDisplay spawnFlagTextDisplay(Flags flag, Location loc){

        TextDisplay td = (TextDisplay) loc.getWorld().spawnEntity(getLocation(loc), EntityType.TEXT_DISPLAY);
        td.setText(pixelAnalyseFromImage(flag.getUrl(), resolution));
        td.setLineWidth(5000);
        //change scale
        td.setTransformation(new Transformation(new Vector3f(0.075f, 0.075f, 0.075f), new Quaternionf(), new Vector3f(0.075f, 0.075f, 0.075f), new Quaternionf()));

        return td;
    }

    public Location getLocation(Location eyeLocation){
        Location loc = eyeLocation.clone();
        //2 blöcke in blickrichtung nach vorne
        loc.add(loc.getDirection().multiply(1.2));
        loc.setYaw(loc.getYaw() + 180);
        //pitch angle flip
        loc.setPitch(-loc.getPitch());

        return loc;
    }

    public String pixelAnalyseFromImage(String imageUrl, int width){
        try {
            // Bild von URL herunterladen
            BufferedImage originalImage = ImageIO.read(new URL(imageUrl));

            // Verhältnis von Originalbild berechnen
            double ratio = (double) originalImage.getHeight() / (double) originalImage.getWidth();

            // Zielbreite festlegen
            int targetHeight = (int) (width * ratio);

            // Bild verkleinern unter Berücksichtigung des Verhältnisses
            BufferedImage resizedImage = new BufferedImage(width, targetHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(originalImage, 0, 0, width, targetHeight, null);
            g2d.dispose();

            int height = resizedImage.getHeight();

            StringBuilder sb = new StringBuilder();

            // Durch jeden Pixel des verkleinerten Bildes gehen und auswerten
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = resizedImage.getRGB(x, y);

                    // Pixelinformationen extrahieren
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;

                    ChatColor color = ChatColor.of(new Color(red, green, blue));

                    sb.append(color.toString() + "\uDAC0\uDC32" + "\uDAFF\uDFFF");

                }
                sb.append("\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @EventHandler
    public void onPlayerMove(org.bukkit.event.player.PlayerMoveEvent event) {
        if (selectedPlayerUUID == null) return;
        if (event.getPlayer().getUniqueId().equals(selectedPlayerUUID)){
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getZ() != event.getTo().getZ() || event.getFrom().getY() != event.getTo().getY()){
                event.getPlayer().teleport(event.getFrom());
            }
            textDisplay.teleport(getLocation(event.getPlayer().getEyeLocation()));
        }
    }

    @Override
    public void tick() {
        super.tick();

        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (selectedPlayerUUID == null) return;
                if (selectedPlayerUUID.equals(player.getUniqueId())) {
                    if(textDisplay == null) return;
                    textDisplay.teleport(getLocation(player.getEyeLocation()));
                }
            });
        });
    }

    @Override
    public void timerTick(int second) {
        super.timerTick(second);

        nextFlagTimer--;
        if (nextFlagTimer <= 0){
            Bukkit.getScheduler().runTask(plugin, this::newFlag);
        }
    }

    @EventHandler
    public void onPlayerChat(org.bukkit.event.player.AsyncPlayerChatEvent event) {
        if (event.getPlayer().getUniqueId().equals(selectedPlayerUUID)){
            event.setCancelled(true);
            if (event.getMessage().equalsIgnoreCase(currentFlag.getName())){
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage("§a" + event.getPlayer().getName() + " §7hat die Flagge erraten!");
                });
                Bukkit.getScheduler().runTask(plugin, this::correctAnswer);
            }else{
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.sendMessage("§a" + event.getPlayer().getName() + " §7hat die Flagge falsch erraten!");
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        player.setHealth(0);

                        if (textDisplay != null){
                            textDisplay.remove();
                        }
                        plugin.getChallengeManager().getTimer().setResumed(true);
                        selectedPlayerUUID = null;
                        Random random = new Random();
                        setNextRandomTime();
                    });
                });
            }
        }
    }

}
