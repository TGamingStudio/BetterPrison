package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.network.chat.ChatComponentText;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.decoration.EntityArmorStand;
import net.minecraft.world.level.World;
import net.minecraft.world.phys.Vec3D;
import org.apache.logging.log4j.core.net.Priority;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_17_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collection;

public class MiningEvents implements Listener {
    Prison Prison;

    public MiningEvents(Prison instance) {
        Prison = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void BlockMined(BlockBreakEvent Event) {
        if (Event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        Area MiningInArea = Prison.getAreaManager().StandingArea(Event.getBlock().getLocation());
        if (MiningInArea == null) return;
        if (Prison.getProfileManager().getProfile(Event.getPlayer().getUniqueId()).getXp() < MiningInArea.getMinXP()) {
            Event.setCancelled(true);
            Event.getPlayer().sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().NOT_ENOUGH_EXPERIENCE));
            return;
        }
        if (!MiningInArea.isMineable(Event.getBlock().getType())) {
            Event.setCancelled(true);
            return;
        }
        Prison.getMinedBlocks().AddBlock(Event.getBlock());
        if (Prison.getMineableManager().getMineable(Event.getBlock().getType()) != null) {
            int minedXP = Prison.getMineableManager().getMineable(Event.getBlock().getType()).getXp();
            Prison.getProfileManager().getProfile(Event.getPlayer().getUniqueId()).AddXp(minedXP);
            Prison.getProfileManager().getProfile(Event.getPlayer().getUniqueId()).AddBlockMined();
            if(Prison.getConfig().getBoolean("settings.auto-pickup")){
                Event.setDropItems(false);
                Event.getBlock().getDrops(Event.getPlayer().getInventory().getItemInMainHand()).forEach(Event.getPlayer().getInventory()::addItem);
            }
            if(Prison.getShowXP().isEnabled(Event.getPlayer()))
                PlaceHologram(ChatColor.GREEN + "+" + minedXP + "XP", Event.getPlayer(), Event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void BlockPlaced(BlockPlaceEvent Event) {
        if (Event.getPlayer().getGameMode() != GameMode.SURVIVAL) return;
        Area MiningInArea = Prison.getAreaManager().StandingArea(Event.getBlock().getLocation());
        if (MiningInArea == null) return;
        Event.setCancelled(true);
    }

    public void PlaceHologram(String Text, Player Player, Location Location) {
        CraftPlayer CraftPlayer = (CraftPlayer) Player;
        World World = ((CraftWorld) CraftPlayer.getLocation().getWorld()).getHandle();
        EntityArmorStand armorStand = new EntityArmorStand(EntityTypes.c, World);
        armorStand.setLocation(Location.getX() + 0.5, Location.getY() - 0.5, Location.getZ() + 0.5, Location.getPitch(), Location.getYaw());
        armorStand.setCustomName(new ChatComponentText(Text));
        armorStand.setCustomNameVisible(true);
        armorStand.setInvisible(true);
        armorStand.setSmall(true);
        armorStand.setNoGravity(true);
        armorStand.setInvulnerable(true);
        PacketPlayOutSpawnEntity EntitySpawnPacket = new PacketPlayOutSpawnEntity(armorStand);
        PacketPlayOutEntityMetadata EntityMetaDataPacket = new PacketPlayOutEntityMetadata(armorStand.getId(), armorStand.getDataWatcher(), true);
        PacketPlayOutEntityDestroy EntityDestroyPacket = new PacketPlayOutEntityDestroy(armorStand.getId());
        PacketPlayOutEntity.PacketPlayOutRelEntityMove EntityMovePacket = new PacketPlayOutEntity.PacketPlayOutRelEntityMove(armorStand.getId(), (byte) 0, (byte) 1400, (byte) 0, false);
        CraftPlayer.getHandle().b.sendPacket(EntitySpawnPacket);
        CraftPlayer.getHandle().b.sendPacket(EntityMetaDataPacket);
        int TaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Prison, () -> CraftPlayer.getHandle().b.sendPacket(EntityMovePacket), 0L, 1L);
        Bukkit.getScheduler().runTaskLater(Prison, () -> {
            CraftPlayer.getHandle().b.sendPacket(EntityDestroyPacket);
            Bukkit.getScheduler().cancelTask(TaskID);
        }, 40L);
    }
}
