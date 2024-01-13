package de.leon_lp9.challengePlugin.management;

import lombok.Getter;

@Getter
public enum KeyBinds {
    JUMP("key.jump"),
    SNEAK("key.sneak"),
    SPRINT("key.sprint"),
    LEFT("key.left"),
    RIGHT("key.right"),
    BACK("key.back"),
    FORWARD("key.forward"),
    ATTACK("key.attack"),
    PICK_ITEM("key.pickItem"),
    USE("key.use"),
    DROP("key.drop"),
    HOTBAR_1("key.hotbar.1"),
    HOTBAR_2("key.hotbar.2"),
    HOTBAR_3("key.hotbar.3"),
    HOTBAR_4("key.hotbar.4"),
    HOTBAR_5("key.hotbar.5"),
    HOTBAR_6("key.hotbar.6"),
    HOTBAR_7("key.hotbar.7"),
    HOTBAR_8("key.hotbar.8"),
    HOTBAR_9("key.hotbar.9"),
    INVENTORY("key.inventory"),
    SWAP_OFF_HANDS("key.swapOffhand"),
    LOAD_TOOLBAR_ACTIVATOR("key.loadToolbarActivator"),
    SAVE_TOOLBAR_ACTIVATOR("key.saveToolbarActivator"),
    PLAYERLIST("key.playerlist"),
    CHAT("key.chat"),
    COMMAND("key.command"),
    SOCIAL_INTERACTIONS("key.socialInteractions"),
    ADVANCEMENTS("key.advancements"),
    SPECATOR_OUTLINES("key.spectatorOutlines"),
    SCREENSHOT("key.screenshot"),
    SMOOTH_CAMERA("key.smoothCamera"),
    FULLSCREEN("key.fullscreen"),
    TOGGLE_PERSPECTIVE("key.togglePerspective");

    private final String key;

    KeyBinds(String key) {
        this.key = key;
    }

}
