package com.floweytf.SpigotTest.commands;

import com.floweytf.SpigotTest.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Test implements CommandExecutor {
    public static HashMap<UUID, Pair<Integer, String>> hashMap = new HashMap<>();
    public static List<String> correctSequence = List.of("+X", "+X", "+Z", "+Z");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player psender = (Player) sender;
            int delayTicks = Integer.parseInt(args[0]);
            Location l = psender.getLocation().setDirection(new Vector(0, 0, 1));
            psender.teleport(l);
            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                Player psender2 = Bukkit.getServer().getPlayer(psender.getUniqueId());
                if (psender2 == null) {
                    Bukkit.getScheduler().cancelTask(hashMap.get(psender.getUniqueId()).getValue0());
                    hashMap.remove(psender.getUniqueId());
                    return;
                }
                psender2.sendTitle(ChatColor.DARK_RED + "Test", null, 0, delayTicks, 0);
                Vector v = psender2.getLocation().subtract(l).toVector();
                String s = "";
                if (!v.isZero()) {
                    if (v.getX() + v.getZ() > 0) {
                        if (v.getX() - v.getZ() > 0) {

                            s = "+X";
                        } else {
                            s = "+Z";
                        }
                    } else {
                        if (v.getX() - v.getZ() > 0) {
                            s = "-Z";
                        } else {
                            s = "-X";
                        }
                    }
                    if (!hashMap.get(psender2.getUniqueId()).getValue1().equals(s)) {
                        psender2.sendMessage(s);
                    }
                    psender2.teleport(l);

                }
                hashMap.put(psender2.getUniqueId(), new Pair<>(hashMap.get(psender2.getUniqueId()).getValue0(), s));
            }, 0, delayTicks);
            hashMap.put(psender.getUniqueId(), new Pair<>(id, ""));
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.toString());
            return false;
        }
        return true;
    }



}
