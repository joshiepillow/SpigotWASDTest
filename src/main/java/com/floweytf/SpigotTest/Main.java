package com.floweytf.SpigotTest;

import com.floweytf.SpigotTest.commands.Test;
import com.floweytf.SpigotTest.commands.TestStop;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("test").setExecutor(new Test());
        this.getCommand("teststop").setExecutor(new TestStop());
    }

    @Override
    public void onDisable() {

    }
}