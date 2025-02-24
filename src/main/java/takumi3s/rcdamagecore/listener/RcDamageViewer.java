package takumi3s.rcdamagecore.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class RcDamageViewer implements Listener {
    @EventHandler
    public void onDamaged(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)) return;
        ProtocolManager pm = ProtocolLibrary.getProtocolManager();
        PacketContainer pc = pm.createPacket(PacketType.Play.Server.SPAWN_ENTITY);

        Location location = e.getEntity().getLocation();

        pc.getModifier().writeDefaults();
        pc.getEntityTypeModifier().write(0, EntityType.ARMOR_STAND);
        pc.getUUIDs().write(0, UUID.randomUUID());
        pc.getIntegers().write(1, 1);
        pc.getDoubles().write(0, location.getX() + (Math.random() * 6) - 3);
        pc.getDoubles().write(1, location.getY() + (Math.random()) + 2);
        pc.getDoubles().write(2, location.getZ() + (Math.random() * 6) - 3);

        PacketContainer pc2 = pm.createPacket(PacketType.Play.Server.ENTITY_METADATA);


        try {
            pm.sendServerPacket(player, pc);
            pm.sendServerPacket(player, pc2);
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
        }
    }
}
