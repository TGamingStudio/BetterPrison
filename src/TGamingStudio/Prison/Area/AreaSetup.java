package TGamingStudio.Prison.Area;

import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
public class AreaSetup {

    HashMap<UUID, Area> SetupAreas = new HashMap<>();

    Prison Prison;

    public AreaSetup(Prison instance) {
        Prison = instance;
    }

    public void SendMenu(Player Player) {
        if (!SetupAreas.containsKey(Player.getUniqueId()))
            if (Prison.getAreaManager().StandingArea(Player.getLocation()) != null) {
                EditArea(Player, Prison.getAreaManager().StandingArea(Player.getLocation()));
            } else SetupAreas.put(Player.getUniqueId(), new Area());
        ComponentBuilder Builder = new ComponentBuilder("--------------------\n").color(ChatColor.GRAY);
        Builder.append("Setup an area").color(ChatColor.BLUE).bold(true);
        Builder.append("\n[Set Location 1]").color(ChatColor.GREEN).bold(false)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area location1"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Set Location 1 to current location")));
        if (SetupAreas.get(Player.getUniqueId()).getLoc1() != null)
            Builder.append(" Located").color(ChatColor.BLUE);
        Builder.append("\n[Set Location 2]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area location2"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Set Location 2 to current location")));
        if (SetupAreas.get(Player.getUniqueId()).getLoc2() != null)
            Builder.append(" Located").color(ChatColor.BLUE);
        Builder.append("\n[Set Teleport]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area teleport"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Set Teleport to current location")));
        if (SetupAreas.get(Player.getUniqueId()).getTeleport() != null)
            Builder.append(" Located").color(ChatColor.BLUE);
        Builder.append("\n[Set Icon]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area icon"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Set Icon to held item")));
        Builder.append(" " + SetupAreas.get(Player.getUniqueId()).getIcon().toString()).color(ChatColor.BLUE);
        Builder.append("\n[Set Minimal XP]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area minimalxp"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Type new value in the chat")));
        Builder.append(" " + SetupAreas.get(Player.getUniqueId()).getMinXP()).color(ChatColor.BLUE);
        Builder.append("\n[Set Name]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area name"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Type new value in the chat")));
        Builder.append(" " + SetupAreas.get(Player.getUniqueId()).getName()).color(ChatColor.BLUE);
        Builder.append("\n[Add Block]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area addblock"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Adds block held in hand")));
        for (String Mineable : SetupAreas.get(Player.getUniqueId()).getMineableBlocksAsString()) {
            Builder.append(" " + Mineable).color(ChatColor.BLUE)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area removeblock " + Mineable))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Removes " + Mineable + " from mineable blocks")));
        }
        Builder.append("\n[DELETE]").color(ChatColor.RED)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area delete"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Delete the area (Interrupt setup).")));
        Builder.append("\n[Save]").color(ChatColor.GREEN)
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/prison area save"))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Save the area.")));
        BaseComponent[] component = Builder.create();
        Player.spigot().sendMessage(component);
    }

    public boolean isEditing(Player Player) {
        return SetupAreas.containsKey(Player.getUniqueId());
    }

    private void EditArea(Player Player, Area Area) {
        Area.Editing();
        SetupAreas.put(Player.getUniqueId(), Area);
    }

    public void SetLocation1(Player Player) {
        SetupAreas.get(Player.getUniqueId()).setLoc1(Player.getLocation());
        SendMenu(Player);
    }

    public void SetLocation2(Player Player) {
        SetupAreas.get(Player.getUniqueId()).setLoc2(Player.getLocation());
        SendMenu(Player);
    }

    public void SetTeleport(Player Player){
        SetupAreas.get(Player.getUniqueId()).setTeleport(Player.getLocation());
        SendMenu(Player);
    }

    public void SetIcon(Player Player){
        SetupAreas.get(Player.getUniqueId()).setIcon(Player.getInventory().getItemInMainHand().getType());
        SendMenu(Player);
    }

    public void AddBlock(Player Player) {
        if (Player.getInventory().getItemInMainHand().getType() != Material.AIR)
            SetupAreas.get(Player.getUniqueId()).AddMineableBlock(Player.getInventory().getItemInMainHand().getType());
        else
            Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().ITEM_EXPECTED));
        SendMenu(Player);
    }

    public void RemoveBlock(Player Player, String MaterialName) {
        SetupAreas.get(Player.getUniqueId()).RemoveMineableBlock(Material.getMaterial(MaterialName));
        SendMenu(Player);
    }

    private List<UUID> AwaitingName = new ArrayList<>();

    public void AwaitName(Player Player) {
        AwaitingName.add(Player.getUniqueId());
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().AREA_SETUP_NAME));
    }

    public boolean isAwaitingName(Player Player) {
        return AwaitingName.contains(Player.getUniqueId());
    }

    public void SetName(Player Player, String Name) {
        SetupAreas.get(Player.getUniqueId()).setName(Name);
        AwaitingName.remove(Player.getUniqueId());
        SendMenu(Player);
    }

    private List<UUID> AwaitingXP = new ArrayList<>();

    public void AwaitMinimalXP(Player Player) {
        AwaitingXP.add(Player.getUniqueId());
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().AREA_SETUP_XP));
    }

    public boolean isAwaitingXP(Player Player) {
        return AwaitingXP.contains(Player.getUniqueId());
    }

    public void SetMinimalXP(Player Player, int XP) {
        SetupAreas.get(Player.getUniqueId()).setMinXP(XP);
        AwaitingXP.remove(Player.getUniqueId());
        SendMenu(Player);
    }

    public void Save(Player Player) {
        Area SetupArea = SetupAreas.get(Player.getUniqueId());
        if (SetupArea.getName() != null && SetupArea.getLoc1() != null && SetupArea.getLoc2() != null
                && SetupArea.getTeleport() != null
                && SetupArea.getMineableBlocks().size() != 0) {
            if (!SetupArea.isEdited())
                Prison.getAreaManager().AddArea(SetupArea);
            SetupAreas.remove(Player.getUniqueId());
            Prison.getAreaManager().SaveAreas();
            Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().AREA_SETUP_SAVED));
        } else {
            Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().AREA_SETUP_UNFINISHED));
        }
    }

    public void Delete(Player Player) {
        Area SetupArea = SetupAreas.get(Player.getUniqueId());
        if (SetupArea.isEdited()) {
            Prison.getAreaManager().RemoveArea(SetupArea);
        }
        SetupAreas.remove(Player.getUniqueId());
        Prison.getAreaManager().SaveAreas();
        Player.sendMessage(Prison.getMessageBuilder().Build(Prison.getMessages().AREA_SETUP_DELETED));
    }
}
