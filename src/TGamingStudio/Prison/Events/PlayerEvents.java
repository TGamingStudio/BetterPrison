package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Prison;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
public class PlayerEvents implements Listener {

    Prison Prison;

    public PlayerEvents(Prison instance) {
        Prison = instance;
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent Event) {
        Prison.getProfileManager().LoadProfile(Event.getPlayer().getUniqueId());
        if (Event.getPlayer().hasPermission("prison.*") && Prison.checkForUpdate()) {
            Event.getPlayer().sendMessage("You are running an older version of BetterPrison. Download a newer version here https://www.spigotmc.org/resources/betterprison.95811/");
        }
    }

    private void UnloadPlayer(Player Player) {
        Prison.getProfileManager().SaveProfile(Player.getUniqueId());
        Prison.getMovingEvents().getEntered().remove(Player.getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent Event) {
        if (Event.isCancelled()) return;
        UnloadPlayer(Event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent Event) {
        UnloadPlayer(Event.getPlayer());
    }

}
