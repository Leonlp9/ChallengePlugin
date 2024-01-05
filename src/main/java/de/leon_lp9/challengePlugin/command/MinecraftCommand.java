package de.leon_lp9.challengePlugin.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MinecraftCommand {

    String name();
    String description() default "";
    String permission() default "";

}
