package com.floweytf.SpigotTest.commands;

import com.floweytf.SpigotTest.Main;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Test implements CommandExecutor {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT, NONE
    }
    private class DataStructure {
        DataStructure(int nD, Direction lD, int tID)  {
            noteDelay = nD;
            noteNumber = 0;
            lastDirection = lD;
            lastPressWASD = new HashMap<>();
            lastPressWASD.put(Direction.UP, Long.MIN_VALUE/2);
            lastPressWASD.put(Direction.DOWN, Long.MIN_VALUE/2);
            lastPressWASD.put(Direction.LEFT, Long.MIN_VALUE/2);
            lastPressWASD.put(Direction.RIGHT, Long.MIN_VALUE/2);
            lastPressWASD.put(Direction.NONE, Long.MIN_VALUE/2);
            taskID = tID;
        }
        public int noteDelay;
        public int noteNumber;
        public Direction lastDirection;
        public HashMap<Direction, Long> lastPressWASD;
        public int taskID;
    }
    public static HashMap<UUID, DataStructure> hashMap = new HashMap<>();
    public static List<Direction> correctSequence = List.of(Direction.UP, Direction.DOWN, Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT);
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player p = (Player) sender;
            setup(p, Integer.parseInt(args[0]));
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.toString());
            return false;
        }
        return true;
    }


    void setup(Player p, int noteDelay) {
        cancelTask(p);
        Location l = p.getLocation().setDirection(new Vector(1, 0, 0));
        p.teleport(l);
        Minecart m = (Minecart) p.getWorld().spawnEntity(l, EntityType.MINECART);
        NBTEntity e = new NBTEntity(m);
        e.setByte("NoAI", (byte) 1);
        m.addPassenger(p);

        int taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> loop(p), noteDelay, noteDelay);
        p.sendMessage(String.format("Next direction is %s", correctSequence.get(0)));
        hashMap.put(p.getUniqueId(), new DataStructure(noteDelay, Direction.NONE, taskID));
    }

    void loop(Player p) {
        if (!hashMap.containsKey(p.getUniqueId())) cancelTask(p);

        DataStructure ds = hashMap.get(p.getUniqueId());
        p.sendMessage(String.format("You were %d ticks early.", p.getWorld().getTime() - ds.lastPressWASD.get(correctSequence.get(ds.noteNumber))));
        ds.noteNumber++;
        if (ds.noteNumber >= correctSequence.size()) {
            cancelTask(p);
            return;
        }
        p.sendMessage(String.format("Next direction is %s", correctSequence.get(ds.noteNumber)));
    }

    public static void update(Player p, float swSpeed, float fwSpeed) {
        if (!hashMap.containsKey(p.getUniqueId())) return;

        Direction d = Direction.NONE;
        if (fwSpeed != 0 || swSpeed != 0)
            d = Math.abs(fwSpeed) > Math.abs(swSpeed) ? (fwSpeed > 0 ? Direction.UP : Direction.DOWN) : (swSpeed > 0 ? Direction.RIGHT : Direction.LEFT);

        DataStructure ds = hashMap.get(p.getUniqueId());
        if (ds.lastDirection == d) return;

        ds.lastDirection = d;
        ds.lastPressWASD.put(d, p.getWorld().getTime());
    }

    public static void cancelTask(Player p) {
        if (!hashMap.containsKey(p.getUniqueId())) return;

        Bukkit.getScheduler().cancelTask(hashMap.get(p.getUniqueId()).taskID);
        if (p.getVehicle() != null) p.getVehicle().remove();
        hashMap.remove(p.getUniqueId());
    }
}
