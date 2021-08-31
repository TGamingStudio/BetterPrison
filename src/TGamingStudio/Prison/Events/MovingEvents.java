package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.Hash;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.UUID;
public class MovingEvents implements Listener {

    Prison Prison;

    HashMap<UUID, Area> Entered = new HashMap<>();

    public MovingEvents(Prison instance) {
        Prison = instance;
    }

    @EventHandler
    public void MoveEvent(PlayerMoveEvent Event) {
        Player Player = Event.getPlayer();
        Area StandingArea = Prison.getAreaManager().StandingArea(Player.getLocation());
        if (Entered.get(Player.getUniqueId()) == null) {
            if (StandingArea != null) {
                Entered.put(Player.getUniqueId(), StandingArea);
                if (Prison.getMessages().AREA_ENTER_ENABLED)
                    Player.sendTitle(Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().AREA_ENTER_TITLE, StandingArea.getName(), String.valueOf(StandingArea.getMinXP())),
                            Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().AREA_ENTER_SUBTITLE, StandingArea.getName(), String.valueOf(StandingArea.getMinXP())),
                            (int) (Prison.getMessages().AREA_ENTER_TIME * 0.05),
                            Prison.getMessages().AREA_ENTER_TIME,
                            (int) (Prison.getMessages().AREA_ENTER_TIME * 0.05));
            }
        } else if (StandingArea == null || StandingArea != Entered.get(Player.getUniqueId()))
            Entered.remove(Player.getUniqueId());
    }
}
