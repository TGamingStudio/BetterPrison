package TGamingStudio.Prison.Commands;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
public class Mines implements CommandExecutor {
    Prison Prison;

    public Mines(TGamingStudio.Prison.Prison instance) {
        Prison = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!sender.hasPermission("prison.mines")) {
            sender.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().MISSING_PERMISSIONS));
            return false;
        }
        Player Player = (Player) sender;
        Inventory inv = Bukkit.createInventory(null,
                (int) Math.ceil(Prison.getAreaManager().getAreas().size() /  9.0) * 9,
                Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().MINES_GUI_TITLE));
        for (Area Area : Prison.getAreaManager().getAreas()) {
            ItemStack AreaItem = new ItemStack(Area.getIcon());
            ItemMeta AreaItemMeta = AreaItem.getItemMeta();


            AreaItemMeta.setDisplayName(ChatColor.of(Prison.getMessages().MINES_GUI_TITLECOLOR) + Area.getName());
            List<String> Lore = new ArrayList<>();
            Lore.add(Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().MINES_GUI_TELEPORT));
            Lore.add(Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().MINES_GUI_XP, String.valueOf(Area.getMinXP())));
            Lore.add(Prison.getMessageBuilder().BuildPrefixless(Prison.getMessages().getAreaLocked(Prison.getAreaManager().isUnlocked(Area, Player))));
            AreaItemMeta.setLore(Lore);

            AreaItem.setItemMeta(AreaItemMeta);
            inv.addItem(AreaItem);
        }
        Player.openInventory(inv);
        return false;
    }
}
