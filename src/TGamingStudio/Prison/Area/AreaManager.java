package TGamingStudio.Prison.Area;

import TGamingStudio.Prison.Prison;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class AreaManager {
    private List<Area> Areas = new ArrayList<>();

    Prison Prison;
    public FileConfiguration Config;

    public AreaManager(Prison instance) {
        Prison = instance;
        Config = Prison.getConfig();
    }

    public void LoadAreas() {
        if (Config.get("areas") == null) {
            GenerateDummyArea();
        }
        Areas = new ArrayList<>();
        for (String AreaConfigPath : Config.getConfigurationSection("areas").getKeys(false)) {
            Location Loc1 = (Location) Config.get("areas." + AreaConfigPath + ".Location1");
            Location Loc2 = (Location) Config.get("areas." + AreaConfigPath + ".Location2");
            int MinXP = Config.getInt("areas." + AreaConfigPath + ".MinXP");
            World W = Prison.getServer().getWorld((String) Config.get("areas." + AreaConfigPath + ".World"));
            Location Teleport = (Location) Config.get("areas." + AreaConfigPath + ".Teleport");
            String Icon = Config.getString("areas." + AreaConfigPath + ".Icon");
            Area NewArea = new Area(AreaConfigPath, MinXP, W, Loc1, Loc2, Teleport, Material.getMaterial(Icon));
            List<String> MineableBlocks = (List<String>) Config.get("areas." + AreaConfigPath + ".MineableBlocks");
            for (String MineableBlock : MineableBlocks) {
                NewArea.AddMineableBlock(Material.getMaterial(MineableBlock));
            }
            AddArea(NewArea);
        }
    }

    private void GenerateDummyArea() {
        World W = Prison.getServer().getWorld("world");
        Area DummyArea = new Area("Dummy Area", 0, W, new Location(null, 0, 0, 0), new Location(null, 10, 10, 10), new Location(W, 5, 5, 5), Material.DIAMOND_PICKAXE);
        DummyArea.AddMineableBlock(Material.getMaterial("COAL_ORE"));
        DummyArea.AddMineableBlock(Material.getMaterial("STONE"));
        AddArea(DummyArea);
        SaveAreas();
    }

    public void SaveAreas() {
        for (Area Area : Areas) {
            if (Area.isEdited())
                if (Area.getOldName() != null && !Area.getOldName().equals(Area.getName()))
                    Config.set("areas." + Area.getOldName(), null);
            Config.set("areas." + Area.getName() + ".World", Area.getWorld().getName());
            Config.set("areas." + Area.getName() + ".MinXP", Area.getMinXP());
            Config.set("areas." + Area.getName() + ".Location1", Area.getLoc1());
            Config.set("areas." + Area.getName() + ".Location2", Area.getLoc2());
            Config.set("areas." + Area.getName() + ".Teleport", Area.getTeleport());
            Config.set("areas." + Area.getName() + ".Icon", Area.getIcon().toString());
            Config.set("areas." + Area.getName() + ".MineableBlocks", Area.getMineableBlocksAsString());
        }
        Prison.saveConfig();
    }

    public void AddArea(Area NewArea) {
        Areas.add(NewArea);
    }

    public void RemoveArea(Area OldArea) {
        Config.set("areas." + OldArea.getName(), null);
        Areas.remove(OldArea);
    }

    public List<Area> getAreas() {
        return Areas;
    }

    public Area getArea(String Name) {
        if (Areas.stream().filter(Area -> Area.getName().equals(Name)).findFirst().isPresent())
            return Areas.stream().filter(Area -> Area.getName().equals(Name)).findFirst().get();
        else return null;
    }

    public Area StandingArea(Location Loc) {
        for (Area Area : Areas) {
            if (Area.contains(Loc)) return Area;
        }
        return null;
    }

    public boolean isUnlocked(Area Area, Player Player) {
        return Prison.getProfileManager().getProfile(Player.getUniqueId()).getXp() >= Area.getMinXP();
    }

    public List<Area> getUnlockedAreas(Player Player) {
        return getAreas().stream().filter(Area -> isUnlocked(Area, Player)).collect(Collectors.toList());
    }

    public List<Area> SortByRequiredXP(List<Area> ToCompare) {
        return ToCompare.stream().sorted(Comparator.comparingInt(Area::getMinXP)).collect(Collectors.toList());
    }

    public Area HighestByRequiredXP() {
        List<Area> Sorted = SortByRequiredXP(getAreas());
        return Sorted.get(Sorted.size() - 1);
    }

    public Area NextArea(Player Player) {
        List<Area> Sorted = SortByRequiredXP(getAreas());
        List<Area> NotUnlocked = Sorted.subList(getUnlockedAreas(Player).size(), Sorted.size());
        if (NotUnlocked.size() > 0)
            return NotUnlocked.get(0);
        else
            return null;
    }

    public Area HighestUnlockedByRequiredXP(Player Player) {
        List<Area> Sorted = SortByRequiredXP(getUnlockedAreas(Player));
        return Sorted.get(Sorted.size() - 1);
    }

    public List<Player> PlayersInArea(Area Area) {
        List<UUID> UUIDs = Prison.getMovingEvents().getEntered().keySet().stream().filter(PlayerUUID -> Prison.getMovingEvents().getEntered().get(PlayerUUID).equals(Area)).collect(Collectors.toList());
        List<Player> Players = new ArrayList<>();
        for (UUID id : UUIDs) Players.add(Bukkit.getPlayer(id));
        return Players;
    }


}
