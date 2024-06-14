package me.mineman.network.rank;

import net.kyori.adventure.text.format.TextColor;

import java.awt.*;

public enum Rank{

    ADMINISTRATOR(TextColor.fromHexString("#a80a52"), 'a', "Admin"),
    MEMBER(TextColor.fromHexString("#ff8300"), 'b', "Member"),
    PLAYER(TextColor.fromHexString("#d4af37"), 'c', "Player"),
    GUEST(TextColor.fromHexString("#8a8a8a"), 'd', "Guest");

    private final TextColor color;
    private final TextColor messageColor;
    private final char tabSorter;
    private final String display;

    Rank(TextColor color, char tabSorter, String display){
        this.color = color;
        this.messageColor = lightenColor(color);
        this.tabSorter = tabSorter;
        this.display = display;
    }

    private TextColor lightenColor(TextColor color){
        return TextColor.color(new Color(color.value()).brighter().getRGB());
    }

    public TextColor getColor() {
        return color;
    }

    public TextColor getMessageColor() {
        return messageColor;
    }

    public char getTabSorter() {
        return tabSorter;
    }

    public String getDisplay() {
        return display;
    }

}
