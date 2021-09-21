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
public class SellableSetup {
    HashMap<UUID, Sellable> SetupSellables = new HashMap<>();

    Prison Prison;

    public SellableSetup(Prison instance) {
        Prison = instance;
    }

    public void SendMenu(Player Player) {
        ComponentBuilder Builder;
        if (!SetupSellables.containsKey(Player.getUniqueId())) {
            Builder = new ComponentBuilder("--------------------\n").color(ChatColor.GRAY);
            Builder.append("Sellable items\n").color(ChatColor.BLUE).bold(true);
            for (String Sellable : Prison.getSellableManager().getSellables()) {
                Builder.append(Sellable + " ").color(ChatColor.RED).bold(false)
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison items edit " + Sellable))
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Edit " + Sellable)));
            }
            Builder.append("\n[New Item]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison items new"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Add new item from main hand")));
        } else {
            Builder = new ComponentBuilder("--------------------\n").color(ChatColor.GRAY);
            Builder.append(SetupSellables.get(Player.getUniqueId()).getMaterial().toString()).color(ChatColor.BLUE).bold(true);
            Builder.append("\n[Set Price]").color(ChatColor.GREEN).bold(false)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison items price"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Type new value in the chat")));
            Builder.append(" " + SetupSellables.get(Player.getUniqueId()).getMoney()).color(ChatColor.BLUE);
            Builder.append("\n[DELETE]").color(ChatColor.RED)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison items delete"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Delete the item.")));
            Builder.append("\n[Save]").color(ChatColor.GREEN)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison items save"))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Save the item.")));
        }
        Player.spigot().sendMessage(Builder.create());
    }

    public boolean isEditing(Player Player) {
        return SetupSellables.containsKey(Player.getUniqueId());
    }

    public void EditSellable(Player Player, String MaterialName) {
        SetupSellables.put(Player.getUniqueId(), Prison.getSellableManager().getSellable(Material.getMaterial(MaterialName)));
        SendMenu(Player);
    }

    private List<UUID> AwaitingMoney = new ArrayList<>();

    public void AwaitMoney(Player Player) {
        AwaitingMoney.add(Player.getUniqueId());
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEMS_SETUP_PRICE));
    }

    public boolean isAwaitingMoney(Player Player) {
        return AwaitingMoney.contains(Player.getUniqueId());
    }

    public void SetMoney(Player Player, int Money) {
        SetupSellables.get(Player.getUniqueId()).setMoney(Money);
        AwaitingMoney.remove(Player.getUniqueId());
        SendMenu(Player);
    }

    public void DeleteSellable(Player Player) {
        Sellable SetupSellable = SetupSellables.get(Player.getUniqueId());
        SetupSellables.remove(Player.getUniqueId());
        Prison.getSellableManager().RemoveSellable(SetupSellable);
        Prison.getSellableManager().SaveSellables();
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEMS_SETUP_DELETED));
        SendMenu(Player);
    }

    public void SaveSellable(Player Player) {
        SetupSellables.remove(Player.getUniqueId());
        Prison.getSellableManager().SaveSellables();
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEMS_SETUP_SAVED));
        SendMenu(Player);
    }

    public void NewSellable(Player Player) {
        if (Player.getInventory().getItemInMainHand().getType() != Material.AIR) {
            Prison.getSellableManager().AddSellable(Player.getInventory().getItemInMainHand().getType(), new Sellable(Player.getInventory().getItemInMainHand().getType()));
            EditSellable(Player, Player.getInventory().getItemInMainHand().getType().toString());
        } else
            Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEM_EXPECTED));
        SendMenu(Player);
    }
}
