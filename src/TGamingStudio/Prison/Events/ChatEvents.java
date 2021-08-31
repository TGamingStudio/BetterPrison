package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Prison;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
public class ChatEvents implements Listener {
    Prison Prison;

    public ChatEvents(Prison instance) {
        Prison = instance;
    }

    @EventHandler
    public void ChatEvent(AsyncPlayerChatEvent Event) {
        if (Prison.getAreaSetup().isAwaitingName(Event.getPlayer())) {
            Prison.getAreaSetup().SetName(Event.getPlayer(), Event.getMessage());
            Event.setCancelled(true);
        }
        try {
            if (Prison.getAreaSetup().isAwaitingXP(Event.getPlayer())) {
                Prison.getAreaSetup().SetMinimalXP(Event.getPlayer(), Integer.parseInt(Event.getMessage()));
                Event.setCancelled(true);
            }
            if (Prison.getSellableSetup().isAwaitingMoney(Event.getPlayer())) {
                Prison.getSellableSetup().SetMoney(Event.getPlayer(), Integer.parseInt(Event.getMessage()));
                Event.setCancelled(true);
            }
            if (Prison.getMineableSetup().isAwaitingTime(Event.getPlayer())) {
                Prison.getMineableSetup().SetTime(Event.getPlayer(), Integer.parseInt(Event.getMessage()));
                Event.setCancelled(true);
            }
            if (Prison.getMineableSetup().isAwaitingXp(Event.getPlayer())) {
                Prison.getMineableSetup().SetXp(Event.getPlayer(), Integer.parseInt(Event.getMessage()));
                Event.setCancelled(true);
            }
        } catch (NumberFormatException E) {
            Event.getPlayer().sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().NUMBER_EXPECTED, "0"));
        }
    }
}
