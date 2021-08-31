package TGamingStudio.Prison.Commands;

import TGamingStudio.Prison.Prison;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
public class ShowXP implements CommandExecutor {

    Prison Prison;

    public ShowXP(Prison instance) {
        Prison = instance;
    }

    private List<Player> enabled = new ArrayList<>();

    public boolean isEnabled(Player Player) {
        return enabled.contains(Player);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player Player = (Player) sender;
        if (!sender.hasPermission("prison.showxp")) {
            sender.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().MISSING_PERMISSIONS));
            return false;
        }

        if (isEnabled(Player)) enabled.remove(Player);
        else enabled.add(Player);

        sender.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().COMMAND_SHOWXP, Prison.getMessages().getEnabled(isEnabled(Player))));
        return false;
    }
}
