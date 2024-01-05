package de.leon_lp9.challengePlugin.challenges.config;

import lombok.Data;

import java.lang.reflect.Field;
import java.util.UUID;

@Data
public class ConfigurableField {

    private final UUID id;
    private final Field field;
    private final ConfigurationValue metadata;
    private final Class<?> type;

    public ConfigurableField(Field field) {
        this.field = field;
        this.id = UUID.randomUUID();
        this.metadata = field.getAnnotation(ConfigurationValue.class);
        this.type = field.getType();
    }

}
