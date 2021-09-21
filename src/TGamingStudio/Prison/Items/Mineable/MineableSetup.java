package TGamingStudio.Prison.Items;

import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
public class MineableSetup {
    HashMap<UUID, Mineable> SetupMineables = new HashMap<>();

    Prison Prison;

    public MineableSetup(Prison instance) {
        Prison = instance;
    }

    public void SendMenu(Player Player) {
        ComponentBuilder Builder;
        if (!SetupMineables.containsKey(Player.getUniqueId())) {
            Builder = new ComponentBuilder("--------------------\n").color(ChatColor.GRAY);
            Builder.append("Mineable items\n").color(ChatColor.BLUE).bold(true);
            for (String Mineable : Prison.getMineableManager().getMineables()) {
                Builder.append(Mineable + " ").color(ChatColor.RED).bold(false)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks edit " + Mineable))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Edit " + Mineable)));
            }
            Builder.append("\n[New Item]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks new"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Add new block from main hand")));
        } else {
            Builder = new ComponentBuilder("--------------------\n").color(ChatColor.GRAY);
            Builder.append(SetupMineables.get(Player.getUniqueId()).getMaterial().toString()).color(ChatColor.BLUE).bold(true);
            Builder.append("\n[Set Xp]").color(ChatColor.GREEN).bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks xp"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Type new value in the chat")));
            Builder.append(" " + SetupMineables.get(Player.getUniqueId()).getXp()).color(ChatColor.BLUE);
            Builder.append("\n[Set Time]").color(ChatColor.GREEN).bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks time"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Type new value in the chat")));
            Builder.append(" " + SetupMineables.get(Player.getUniqueId()).getTime()).color(ChatColor.BLUE);
            Builder.append("\n[DELETE]").color(ChatColor.RED)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks delete"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Delete the item.")));
            Builder.append("\n[Save]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison blocks save"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Save the item.")));
        }
        Player.spigot().sendMessage(Builder.create());
    }

    public boolean isEditing(Player Player) {
        return SetupMineables.containsKey(Player.getUniqueId());
    }

    public void EditMineable(Player Player, String MaterialName) {
        SetupMineables.put(Player.getUniqueId(), Prison.getMineableManager().getMineable(Material.getMaterial(MaterialName)));
        SendMenu(Player);
    }

    private List<UUID> AwaitingXp = new ArrayList<>();

    public void AwaitXp(Player Player) {
        AwaitingXp.add(Player.getUniqueId());
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().BLOCKS_SETUP_XP));
    }

    public boolean isAwaitingXp(Player Player) {
        return AwaitingXp.contains(Player.getUniqueId());
    }

    public void SetXp(Player Player, int XP) {
        SetupMineables.get(Player.getUniqueId()).setXp(XP);
        AwaitingXp.remove(Player.getUniqueId());
        SendMenu(Player);
    }

    private List<UUID> AwaitingTime = new ArrayList<>();

    public void AwaitTime(Player Player) {
        AwaitingTime.add(Player.getUniqueId());
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().BLOCKS_SETUP_TIME));
    }

    public boolean isAwaitingTime(Player Player) {
        return AwaitingTime.contains(Player.getUniqueId());
    }

    public void SetTime(Player Player, int Time) {
        SetupMineables.get(Player.getUniqueId()).setTime(Time);
        AwaitingTime.remove(Player.getUniqueId());
        SendMenu(Player);
    }

    public void DeleteMineable(Player Player) {
        Mineable SetupMineable = SetupMineables.get(Player.getUniqueId());
        SetupMineables.remove(Player.getUniqueId());
        Prison.getMineableManager().RemoveMineable(SetupMineable);
        Prison.getMineableManager().SaveMineables();
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().BLOCKS_SETUP_DELETED));
        SendMenu(Player);
    }

    public void SaveMineable(Player Player) {
        SetupMineables.remove(Player.getUniqueId());
        Prison.getMineableManager().SaveMineables();
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().BLOCKS_SETUP_SAVED));
        SendMenu(Player);
    }

    public void NewMineable(Player Player) {
        if (Player.getInventory().getItemInMainHand().getType() != Material.AIR
                && Player.getInventory().getItemInMainHand().getType().isBlock()) {
            Prison.getMineableManager().AddMineable(Player.getInventory().getItemInMainHand().getType(), new Mineable(Player.getInventory().getItemInMainHand().getType()));
            EditMineable(Player, Player.getInventory().getItemInMainHand().getType().toString());
        } else {
            Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEM_EXPECTED));
            SendMenu(Player);
        }
    }
}
