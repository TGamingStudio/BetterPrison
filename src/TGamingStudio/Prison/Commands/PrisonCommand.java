package TGamingStudio.Prison.Commands;

import TGamingStudio.Prison.Prison;
import TGamingStudio.Prison.Util.MessageBuilder;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
public class PrisonCommand implements CommandExecutor {
    Prison Prison;
    MessageBuilder MB;

    public PrisonCommand(Prison instance) {
        Prison = instance;
        MB = Prison.getMessageBuilder();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            HelpMenu((Player) sender);
        } else {
            if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("prison.reload")) {
                    Prison.ReloadPlugin();
                    sender.sendMessage(MB.Build(Prison.getMessages().CONFIG_RELOADED));
                    sender.sendMessage(MB.Build(Prison.getMessages().CONFIG_WARNING));
                } else sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
            } else if (args[0].equalsIgnoreCase("xp")) {
                if (args.length == 1) {
                    if (sender.hasPermission("prison.xp")) {
                        if (sender instanceof Player)
                            sender.sendMessage(MB.Build(Prison.getMessages().COMMAND_XP, String.valueOf(Prison.getProfileManager().getProfile(((Player) sender).getUniqueId()).getXp())));
                    } else sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
                } else if (args.length == 4) {
                    String[] actionArgs = new String[]{"add", "remove", "set"};
                    if (Arrays.asList(actionArgs).contains(args[1])) {
                        if (sender.hasPermission("prison.xp." + args[1])) {
                            Player Target = Bukkit.getPlayer(args[2]);
                            int amount;
                            if (Target != null) {
                                try {
                                    amount = Integer.parseInt(args[3]);
                                } catch (NumberFormatException Exception) {
                                    sender.sendMessage(MB.Build(Prison.getMessages().NUMBER_EXPECTED, "4"));
                                    return false;
                                }
                                switch (args[1]) {
                                    case "add" -> Prison.getProfileManager().getProfile(Target.getUniqueId()).AddXp(amount);
                                    case "remove" -> Prison.getProfileManager().getProfile(Target.getUniqueId()).AddXp(-amount);
                                    case "set" -> Prison.getProfileManager().getProfile(Target.getUniqueId()).AddXp(amount - Prison.getProfileManager().getProfile(Target.getUniqueId()).getXp());
                                }
                                Prison.getProfileManager().SaveProfile(Target.getUniqueId());
                                sender.sendMessage(MB.Build(Prison.getMessages().COMMAND_XP_CHANGE, Target.getDisplayName(), String.valueOf(Prison.getProfileManager().getProfile(Target.getUniqueId()).getXp())));
                            } else
                                sender.sendMessage(MB.Build(Prison.getMessages().INVALID_PLAYER, "3"));
                        } else
                            sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
                    } else
                        HelpMenu((Player) sender);
                } else
                    HelpMenu((Player) sender);
            } else if (args[0].equalsIgnoreCase("area")) {
                if (sender.hasPermission("prison.area")) {
                    if (args.length == 1) {
                        Prison.getAreaSetup().SendMenu((Player) sender);
                    } else if (Prison.getAreaSetup().isEditing((Player) sender)) {
                        if (args[1].equalsIgnoreCase("location1"))
                            Prison.getAreaSetup().SetLocation1((Player) sender);
                        else if (args[1].equalsIgnoreCase("location2"))
                            Prison.getAreaSetup().SetLocation2((Player) sender);
                        else if (args[1].equalsIgnoreCase("teleport"))
                            Prison.getAreaSetup().SetTeleport((Player) sender);
                        else if (args[1].equalsIgnoreCase("icon"))
                            Prison.getAreaSetup().SetIcon((Player) sender);
                        else if (args[1].equalsIgnoreCase("name"))
                            Prison.getAreaSetup().AwaitName((Player) sender);
                        else if (args[1].equalsIgnoreCase("addblock"))
                            Prison.getAreaSetup().AddBlock((Player) sender);
                        else if (args[1].equalsIgnoreCase("removeblock")) {
                            if (args.length == 3)
                                Prison.getAreaSetup().RemoveBlock((Player) sender, args[2]);
                        } else if (args[1].equalsIgnoreCase("minimalxp"))
                            Prison.getAreaSetup().AwaitMinimalXP((Player) sender);
                        else if (args[1].equalsIgnoreCase("delete"))
                            Prison.getAreaSetup().Delete((Player) sender);
                        else if (args[1].equalsIgnoreCase("save"))
                            Prison.getAreaSetup().Save((Player) sender);
                    }
                } else sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
            } else if (args[0].equalsIgnoreCase("items")) {
                if (sender.hasPermission("prison.items")) {
                    if (args.length == 1) {
                        Prison.getSellableSetup().SendMenu((Player) sender);
                    } else if (args[1].equalsIgnoreCase("edit")) {
                        if (args.length == 3)
                            Prison.getSellableSetup().EditSellable((Player) sender, args[2]);
                    } else if (args[1].equalsIgnoreCase("new")) {
                        Prison.getSellableSetup().NewSellable((Player) sender);
                    } else if (Prison.getSellableSetup().isEditing((Player) sender)) {
                        if (args[1].equalsIgnoreCase("price")) {
                            Prison.getSellableSetup().AwaitMoney((Player) sender);
                        } else if (args[1].equalsIgnoreCase("delete")) {
                            Prison.getSellableSetup().DeleteSellable((Player) sender);
                        } else if (args[1].equalsIgnoreCase("save")) {
                            Prison.getSellableSetup().SaveSellable((Player) sender);
                        }
                    }
                } else sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
            } else if (args[0].equalsIgnoreCase("blocks")) {
                if (sender.hasPermission("prison.blocks")) {
                    if (args.length == 1) {
                        Prison.getMineableSetup().SendMenu((Player) sender);
                    } else if (args[1].equalsIgnoreCase("edit")) {
                        if (args.length == 3)
                            Prison.getMineableSetup().EditMineable((Player) sender, args[2]);
                    } else if (args[1].equalsIgnoreCase("new")) {
                        Prison.getMineableSetup().NewMineable((Player) sender);
                    } else if (Prison.getMineableSetup().isEditing((Player) sender)) {
                        if (args[1].equalsIgnoreCase("xp")) {
                            Prison.getMineableSetup().AwaitXp((Player) sender);
                        } else if (args[1].equalsIgnoreCase("time")) {
                            Prison.getMineableSetup().AwaitTime((Player) sender);
                        } else if (args[1].equalsIgnoreCase("delete")) {
                            Prison.getMineableSetup().DeleteMineable((Player) sender);
                        } else if (args[1].equalsIgnoreCase("save")) {
                            Prison.getMineableSetup().SaveMineable((Player) sender);
                        }
                    }
                } else sender.sendMessage(MB.Build(Prison.getMessages().MISSING_PERMISSIONS));
            } else
                HelpMenu((Player) sender);
        }
        return false;
    }

    public void HelpMenu(Player Player) {
        TextComponent BaseComponent = new TextComponent(MessageBuilder.Color("&7----------- &6Prison v" + Prison.getDescription().getVersion() + " &7----------"));

        TextComponent UserCommandsTitle = new TextComponent(MessageBuilder.Color("\n&f&lUser Commands:"));
        BaseComponent.addExtra(UserCommandsTitle);

        List<String> UserCommands = Arrays.asList("prison", "prison xp", "sellall", "mines");
        List<String> UserCommandsDescriptions = Arrays.asList("Display this dialog", "Display current XP", "Sell all sellable items in inventory", "Display a menu with all mines");
        List<String> UserCommandsPermissions = Arrays.asList("NONE", "prison.xp (default)", "prison.sellall (default)", "prison.mines (default)");
        for (int i = 0; i < UserCommands.size(); i++) {
            TextComponent UserCommand = new TextComponent(MessageBuilder.Color("\n&c/" + UserCommands.get(i)));
            UserCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Permission: " + UserCommandsPermissions.get(i))));
            TextComponent CommandDescription = new TextComponent(MessageBuilder.Color(" " + UserCommandsDescriptions.get(i)));
            BaseComponent.addExtra(UserCommand);
            BaseComponent.addExtra(CommandDescription);
        }

        TextComponent AdminCommandsTitle = new TextComponent(MessageBuilder.Color("\n&f&lAdmin Commands:"));
        BaseComponent.addExtra(AdminCommandsTitle);

        List<String> AdminCommands = Arrays.asList("prison reload", "prison xp add <Player> <Amount>", "prison xp remove <Player> <Amount>", "prison xp set <Player> <Amount>", "prison area", "prison items", "prison blocks");
        List<String> AdminCommandsDescriptions = Arrays.asList("Reloads plugin from config file", "Adds Amount XP to Player", "Takes Amount XP from Player", "Sets Player's XP to Amount", "Displays Area setup dialog", "Displays Sellable items dialog", "Displays Mineable blocks dialog");
        List<String> AdminCommandsPermissions = Arrays.asList("prison.reload", "prison.xp.add", "prison.xp.remove", "prison.xp.set", "prison.area", "prison.items", "prison.blocks");
        for (int i = 0; i < AdminCommands.size(); i++) {
            TextComponent AdminCommand = new TextComponent(MessageBuilder.Color("\n&c/" + AdminCommands.get(i)));
            AdminCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.RED + "Permission: " + AdminCommandsPermissions.get(i))));
            TextComponent CommandDescription = new TextComponent(MessageBuilder.Color(" " + AdminCommandsDescriptions.get(i)));
            BaseComponent.addExtra(AdminCommand);
            BaseComponent.addExtra(CommandDescription);
        }

        Player.spigot().sendMessage(BaseComponent);
    }
}
