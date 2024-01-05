package de.leon_lp9.challengePlugin.command;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

public class CommandManager {

    @SneakyThrows
    public void init() {
        Reflections reflections = new Reflections("de.leon_lp9.challengePlugin.commands");

        Set<CommandInfo> collected = reflections.getTypesAnnotatedWith(MinecraftCommand.class).stream()
                .map(this::constructCommandInfo)
                .collect(Collectors.toSet());

        Map<CommandInfo, Object> commands = new HashMap<>();
        for (CommandInfo commandInfo : collected) {
            Object instance = commandInfo.getConstructor().newInstance();
            commands.put(commandInfo, instance);
        }

        for (Map.Entry<CommandInfo, Object> entry : commands.entrySet()) {
            InternalCommandExecutor internalCommandExecutor = new InternalCommandExecutor(entry.getValue(), entry.getKey());
            Command command = new Command(internalCommandExecutor.getInfo().getName()) {
                @Override
                public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                    return internalCommandExecutor.onCommand(commandSender, this, s, strings);
                }

                @Override
                public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
                    List<String> strings = internalCommandExecutor.onTabComplete(sender, this, alias, args);

                    if (strings == null) {
                        return new ArrayList<>();
                    }

                    return strings;
                }

                @Override
                public @NotNull String getDescription() {
                    return internalCommandExecutor.getInfo().getDescription();
                }
            };

            Field commandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMap.setAccessible(true);
            CommandMap commandMap1 = (CommandMap) commandMap.get(Bukkit.getServer());
            commandMap1.register(internalCommandExecutor.getInfo().getName(), command);
        }
    }

    @SneakyThrows
    private CommandInfo constructCommandInfo(Class<?> command) {
        MinecraftCommand minecraftCommand = command.getAnnotation(MinecraftCommand.class);
        Constructor<?> constructor = command.getConstructor();

        Runner run = null;

        TabCompleter tabCompleter = null;

        for (Method method : command.getMethods()) {
            if (method.isAnnotationPresent(Run.class)) {
                Run annotation = method.getAnnotation(Run.class);

                run = (instance, args, executor) -> {
                    try {
                        Object[] mArgs = new Object[method.getParameterCount()];

                        Parameter[] parameters = method.getParameters();
                        for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
                            Parameter parameter = parameters[i];
                            if (parameter.getType().equals(String[].class)) {
                                mArgs[i] = args;
                            } else if (parameter.getType().isAssignableFrom(executor.getClass())) {
                                mArgs[i] = executor;
                            } else {
                                mArgs[i] = null;
                            }
                        }

                        method.invoke(instance, mArgs);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            }

            if (method.isAnnotationPresent(TabComplete.class)) {
                TabComplete annotation = method.getAnnotation(TabComplete.class);

                tabCompleter = (instance, executor, args) -> {
                    try {
                        Object[] mArgs = new Object[method.getParameterCount()];

                        Parameter[] parameters = method.getParameters();
                        for (int i = 0, parametersLength = parameters.length; i < parametersLength; i++) {
                            Parameter parameter = parameters[i];
                            if (parameter.getType().equals(String[].class)) {
                                mArgs[i] = args;
                            } else if (parameter.getType().isAssignableFrom(executor.getClass())) {
                                mArgs[i] = executor;
                            } else {
                                mArgs[i] = null;
                            }
                        }

                        return (List<String>) method.invoke(instance, mArgs);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException(e);
                    }
                };
            }
        }

        return CommandInfo.builder()
                .name(minecraftCommand.name())
                .description(minecraftCommand.description())
                .permission(minecraftCommand.permission())
                .className(command.getName())
                .constructor(constructor)
                .run(run)
                .tabCompleter(tabCompleter)
                .build();
    }

}
