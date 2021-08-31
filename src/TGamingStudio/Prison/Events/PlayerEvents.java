package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Prison;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
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
        if(Event.getPlayer().hasPermission("prison.*") && Prison.checkForUpade()){
            Event.getPlayer().sendMessage("You are running an older version of BetterPrison. Download a newer version here https://www.spigotmc.org/resources/betterprison.95811/");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent Event) {
        if (Event.isCancelled()) return;
        Prison.getProfileManager().SaveProfile(Event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent Event) {
        Prison.getProfileManager().SaveProfile(Event.getPlayer().getUniqueId());
    }

}
