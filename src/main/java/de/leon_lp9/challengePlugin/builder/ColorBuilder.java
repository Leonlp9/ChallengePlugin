package de.leon_lp9.challengePlugin.builder;

import de.leon_lp9.challengePlugin.management.Spacing;
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

    public ColorBuilder addBackground(){
        //nimm den text und füge hinter jedem char ein "󀀳󏿹" ein
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder color = new StringBuilder();
        stringBuilder.append("\uDAC0\uDC34\uDAFF\uDFFD");

        boolean skip = false;

        for (int i = 0; i < text.length(); i++) {

            if (text.charAt(i) == Spacing.ZEROPIXEl.getSpacing().charAt(0) && text.charAt(i + 1) == Spacing.ZEROPIXEl.getSpacing().charAt(1)) {
                skip = !skip;
                i++;
                if (skip){
                    stringBuilder.append("§f" + Spacing.POSITIVE1PIXEl.getSpacing() + "\uDAC0\uDC33" + Spacing.NEGATIVE1PIXEl.getSpacing() + "\uDAC0\uDC33" + Spacing.NEGATIVE2PIXEl.getSpacing() + "\uDAFF\uDFF7\uDAFF\uDFF7" + Spacing.POSITIVE1PIXEl.getSpacing());
                    stringBuilder.append(color);
                }
                continue;
            }

            if (skip) {
                stringBuilder.append(text.charAt(i));
                continue;
            }

            //wenn an der stelle ein "§" ist, füge nur den char ein und den darauffolgenden char
            if (text.charAt(i) == '§') {
                color.append(text.charAt(i));
                color.append(text.charAt(i + 1));
                i++;
                continue;
            }

            stringBuilder.append("§f\uDAC0\uDC33\uDAFF\uDFF7");
            stringBuilder.append(color);
            stringBuilder.append(text.charAt(i));
            color = new StringBuilder();
        }
        stringBuilder.append("§f" + Spacing.NEGATIVE1PIXEl.getSpacing() + "\uDAC0\uDC35");
        text = stringBuilder.toString();
        return this;
    }

    public String getText() {
        return text;
    }
}
