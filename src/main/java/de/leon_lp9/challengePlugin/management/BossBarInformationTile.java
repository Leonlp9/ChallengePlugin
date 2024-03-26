package de.leon_lp9.challengePlugin.management;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public final class BossBarInformationTile {
    private final String key;
    @Setter
    private String title;
    @Setter
    private String value;
    private final Spacing padding;

    public BossBarInformationTile(String key, String title, String value, Spacing padding) {
        this.key = key;
        this.title = title;
        this.value = value;
        this.padding = padding;
    }


}
