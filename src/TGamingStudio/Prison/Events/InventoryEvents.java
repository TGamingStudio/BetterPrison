package TGamingStudio.Prison.Events;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
public class InventoryEvents implements Listener {
    Prison Prison;

    public InventoryEvents(Prison instance) {
        Prison = instance;
    }

    @EventHandler
    public void InventoryClick(InventoryClickEvent Event) {
        if (Event.getView().getTitle().equals(Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().MINES_GUI_TITLE))) {
            String AreaName = ChatColor.stripColor(Event.getCurrentItem().getItemMeta().getDisplayName());
            Area Area = Prison.getAreaManager().getArea(AreaName);
            Player Player = (Player) Event.getWhoClicked();
            if(Prison.getConfig().getBoolean("settings.teleport-to-locked") || Prison.getAreaManager().isUnlocked(Area, Player))
                Area.Teleport(Player);
            Event.setCancelled(true);
        }
    }
}
