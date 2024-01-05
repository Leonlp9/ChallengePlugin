package de.leon_lp9.challengePlugin.command;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Constructor;

@Data
@Builder
public class CommandInfo {

    private final String className;
    private final String name;
    private final String description;
    private final String permission;
    private final Constructor<?> constructor;

    private final Runner run;
    private final TabCompleter tabCompleter;

}
