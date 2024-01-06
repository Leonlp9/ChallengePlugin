package de.leon_lp9.challengePlugin.challenges.config;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ConfigurationValue {

    String title();
    String description();
    Material icon();

    int min() default 0;
    int max() default 60;

}
