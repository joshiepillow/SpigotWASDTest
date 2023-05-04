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
        DataStructure(int nD, Direction lD, HashMap<Direction, Integer> lP)  {
            noteDelay = nD;
            lastDirection = lD;
            lastPressWASD = lP;
        }
        public int noteDelay;
        public Direction lastDirection;
        public HashMap<Direction, Integer> lastPressWASD;
    }
    public static HashMap<UUID, DataStructure> hashMap = new HashMap<>();
    public static List<String> correctSequence = List.of("+X", "+X", "+Z", "+Z");
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        try {
            Player psender = (Player) sender;
            int delayTicks = Integer.parseInt(args[0]);
            Location l = psender.getLocation().setDirection(new Vector(1, 0, 0));
            psender.teleport(l);
            psender.
            int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(Main.class), () -> {
                Player player = Bukkit.getServer().getPlayer(psender.getUniqueId());
                if (player == null) { // If player is not online
                    Bukkit.getScheduler().cancelTask(hashMap.get(psender.getUniqueId()).getValue0());
                    hashMap.remove(psender.getUniqueId());
                    return;
                }
                player.sendTitle(ChatColor.DARK_RED + "Test", null, 0, delayTicks, 0);
                Vector v = player.getLocation().subtract(l).toVector();
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
                    if (!hashMap.get(player.getUniqueId()).getValue1().equals(s)) {
                        player.sendMessage(s);
                    }
                    player.teleport(l);

                }
                hashMap.put(player.getUniqueId(), new Pair<>(hashMap.get(player.getUniqueId()).getValue0(), s));
            }, 0, delayTicks);
            hashMap.put(psender.getUniqueId(), new Pair<>(id, ""));
        } catch (Exception e) {
            Bukkit.getLogger().warning(e.toString());
            return false;
        }
        return true;
    }


    void setup(Player p, int noteDelay) {
        Location l = p.getLocation().setDirection(new Vector(1, 0, 0));
        p.teleport(l);
        Minecart m = (Minecart) p.getWorld().spawnEntity(l, EntityType.MINECART);
        NBTEntity e = new NBTEntity(m);
        e.setByte("NoAI", (byte) 1);
        m.addPassenger(p);

        hashMap.put(p.getUniqueId(), new DataStructure(noteDelay, Direction.NONE, new HashMap<>()));
    }

    void loop(int n, Player p) {


    }

    public static void update(Player p, float swSpeed, float fwSpeed) {
        if (!hashMap.containsKey(p.getUniqueId())) return;
        Direction d = Direction.NONE;
        if (fwSpeed != 0 || swSpeed != 0)
            d = Math.abs(fwSpeed) > Math.abs(swSpeed) ? (fwSpeed > 0 ? Direction.UP : Direction.DOWN) : (swSpeed > 0 ? Direction.RIGHT : Direction.LEFT);
        HashMap<UUID, DataStructure> hm = hashMap.get(p.getUniqueId());
        if (hm.lastDirection == d) return;
        

    }
}
