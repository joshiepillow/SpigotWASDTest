package com.floweytf.SpigotTest.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TestStop implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player psender = (Player) sender;
            Bukkit.getScheduler().cancelTask(Test.hashMap.get(psender.getUniqueId()).getValue0());
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.toString());
            return false;
        }
        return true;
    }
}
