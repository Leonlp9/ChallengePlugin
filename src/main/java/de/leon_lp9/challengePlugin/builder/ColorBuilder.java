package de.leon_lp9.challengePlugin.builder;

import net.md_5.bungee.api.ChatColor;
import java.awt.Color;

public class ColorBuilder {

    String text;

    public ColorBuilder(String text) {
        this.text = text;
    }

    public ColorBuilder addColorToString(Color color) {
        text = ChatColor.of(new Color(color.getRed(), color.getGreen(), color.getBlue())) + text;
        return this;
    }

    public ColorBuilder addColorGradientToString(Color firstColor, Color secondColor, int startPosition, int width, boolean bold) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int position = (i + startPosition) % width * 2;
            position = adjustPosition(position, width);
            Color color = getColorBetweenTwoColors(firstColor, secondColor, (double) position / width);
            stringBuilder.append(ChatColor.of(new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue())));
            if (bold) stringBuilder.append(ChatColor.BOLD);
            stringBuilder.append(text.charAt(i));
        }
        text = stringBuilder.toString();
        return this;
    }

    private int adjustPosition(int position, int width) {
        if (position < width) {
            return position;
        }
        return width - (position % width);
    }

    private Color getColorBetweenTwoColors(Color firstColor, Color secondColor, double position) {
        int red = (int) (firstColor.getRed() + (secondColor.getRed() - firstColor.getRed()) * position);
        int green = (int) (firstColor.getGreen() + (secondColor.getGreen() - firstColor.getGreen()) * position);
        int blue = (int) (firstColor.getBlue() + (secondColor.getBlue() - firstColor.getBlue()) * position);
        return new Color(red, green, blue);
    }

    public String getText() {
        return text;
    }
}
