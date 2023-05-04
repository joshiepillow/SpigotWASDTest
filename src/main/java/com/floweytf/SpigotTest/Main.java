package com.floweytf.SpigotTest;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.FieldAccessException;
import com.floweytf.SpigotTest.commands.Test;
import com.floweytf.SpigotTest.commands.TestStop;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        this.getCommand("test").setExecutor(new Test());
        this.getCommand("teststop").setExecutor(new TestStop());
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, PacketType.Play.Client.STEER_VEHICLE){
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if (event.isCancelled()) return;
                try {
                    float swSpeed = event.getPacket().getFloat().read(0);
                    float fwSpeed = event.getPacket().getFloat().read(1);
                    Test.update(event.getPlayer(), swSpeed, fwSpeed);

                } catch (FieldAccessException ex){
                    ex.printStackTrace();
                    return; // if something goes wrong, we exit and don't do anything with the packet.
                }
            }
        });
    }

    @Override
    public void onDisable() {

    }
}