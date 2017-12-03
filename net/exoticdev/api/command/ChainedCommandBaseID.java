package net.exoticdev.api.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ChainedCommandBaseID {

    String name();

    String[] aliases();

    boolean allowConsole();

    String description();

}