package TGamingStudio.Prison.Commands;

import TGamingStudio.Prison.Items.Mineable;
import TGamingStudio.Prison.Items.Sellable;
import TGamingStudio.Prison.Prison;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
public class SellAll implements CommandExecutor {

    Prison Prison;

    public SellAll(Prison instance) {
        Prison = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        if (!Prison.isEnabledEconomy()) {
            Prison.getLogger().info(Prison.getMessages().PREFIX + ChatColor.RED + "This server doesn't support economy. Please install Vault.");
            return false;
        }
        if(!sender.hasPermission("prison.sellall")){
            sender.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().MISSING_PERMISSIONS));
            return false;
        }
        Player Player = (Player) sender;
        int price = 0;
        int amount = 0;
        for (ItemStack Items : Player.getInventory().getContents()) {
            if (Items != null) {
                Sellable Item = Prison.getSellableManager().getSellable(Items.getType());
                if (Item != null) {
                    amount += Items.getAmount();
                    price += Items.getAmount() * Item.getMoney();
                    Player.getInventory().remove(Items);
                }
            }
        }
        Prison.getEconomy().depositPlayer(Player, price);
        sender.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().COMMAND_SELLALL, String.valueOf(amount), String.valueOf(price)));
        return false;
    }

}
