package TGamingStudio.Prison.Area;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Area {
    private String Name;
    private int MinXP;
    private Location Loc1;
    private Location Loc2;
    private World World;
    private Vector P1;
    private Vector P2;

    private Location Teleport;
    private Material Icon;

    private List<Material> MineableBlocks = new ArrayList<>();

    public Area(String tName, int tMinXP, World tWorld, Location tPos1, Location tPos2, Location tTeleport, Material tIcon) {
        Name = tName;
        MinXP = tMinXP;
        World = tWorld;
        Loc1 = tPos1;
        Loc2 = tPos2;
        Teleport = tTeleport;
        Icon = tIcon;
        CalculateVectors();
    }

    List<Material> RandomIcons = Arrays.asList(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.GOLDEN_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);

    public Area() {
        Icon = RandomIcons.get(new Random().nextInt(RandomIcons.size()));
    }

    public void CalculateVectors() {
        int x1 = Math.min((int) Loc1.getX(), (int) Loc2.getX());
        int y1 = Math.min((int) Loc1.getY(), (int) Loc2.getY());
        int z1 = Math.min((int) Loc1.getZ(), (int) Loc2.getZ());
        int x2 = Math.max((int) Loc1.getX(), (int) Loc2.getX());
        int y2 = Math.max((int) Loc1.getY(), (int) Loc2.getY());
        int z2 = Math.max((int) Loc1.getZ(), (int) Loc2.getZ());
        P1 = new Vector(x1, y1, z1);
        P2 = new Vector(x2, y2, z2);
    }

    public boolean contains(Location loc) {
        if (loc.getWorld() != World)
            return false;
        if (P1 == null || P2 == null) CalculateVectors();
        return loc.getBlockX() >= P1.getBlockX() && loc.getBlockX() <= P2.getBlockX()
                && loc.getBlockY() >= P1.getBlockY() && loc.getBlockY() <= P2.getBlockY()
                && loc.getBlockZ() >= P1.getBlockZ() && loc.getBlockZ() <= P2.getBlockZ();
    }

    public void AddMineableBlock(Material Material) {
        if (!MineableBlocks.contains(Material))
            MineableBlocks.add(Material);
    }

    public void RemoveMineableBlock(Material Material) {
        MineableBlocks.remove(Material);
    }

    public boolean isMineable(Material Material) {
        return MineableBlocks.contains(Material);
    }

    private boolean Edited = false;

    public void Editing() {
        Edited = true;
    }

    public boolean isEdited() {
        return Edited;
    }

    public String getName() {
        return Name;
    }

    public void setName(String newName) {
        Name = newName;
    }

    public int getMinXP() {
        return MinXP;
    }

    public void setMinXP(int newMinXP) {
        MinXP = newMinXP;
    }

    public World getWorld() {
        return World;
    }

    public Location getLoc1() {
        return Loc1;
    }

    public void setLoc1(Location newLoc) {
        Loc1 = newLoc;
        World = Loc1.getWorld();
    }

    public Location getLoc2() {
        return Loc2;
    }

    public void setLoc2(Location newLoc) {
        Loc2 = newLoc;
    }

    public Location getTeleport() {
        return Teleport;
    }

    public void setTeleport(Location newTeleport) {
        Teleport = newTeleport;
    }

    public Material getIcon() {
        return Icon;
    }

    public void setIcon(Material newIcon) {
        Icon = newIcon;
    }

    public List<Material> getMineableBlocks() {
        return MineableBlocks;
    }

    public List<String> getMineableBlocksAsString() {
        List<String> MaterialList = new ArrayList<>(MineableBlocks.size());
        for (Material MineableMaterial : MineableBlocks) {
            MaterialList.add(String.valueOf(MineableMaterial.toString()));
        }
        return MaterialList;
    }

    public void Teleport(Player Player){
        Player.teleport(Teleport);
    }
}
