package TGamingStudio.Prison.Commands.TabCompleters;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class PrisonCommandCompleter implements TabCompleter {

    private List<String> allArgs = Arrays.asList("reload", "xp", "area", "items", "blocks");
    private List<String> xpActions = Arrays.asList("add", "remove", "set");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> allowedArgs = new ArrayList<>();
            for (String arg : allArgs)
                if (sender.hasPermission("prison." + arg))
                    allowedArgs.add(arg);
            return allowedArgs;
        } else if (args.length == 2) {
            List<String> allowedActions = new ArrayList<>();
            switch (args[0]) {
                case "xp":
                    for (String arg : xpActions)
                        if (sender.hasPermission("prison.xp." + arg))
                            allowedActions.add(arg);
            }
            return allowedActions;
        }else if(args.length == 3){
            List<String> Args = new ArrayList<>();
            switch (args[0]) {
                case "xp":
                    Bukkit.getOnlinePlayers().forEach(Player -> Args.add(Player.getName()));
            }
            return Args;
        }
        else return Arrays.asList();
    }
}
