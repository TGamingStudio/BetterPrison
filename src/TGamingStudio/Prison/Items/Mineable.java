package TGamingStudio.Prison.Items;

import org.bukkit.Material;
public class Mineable {
    private int Xp, Time;
    Material Material;

    public Mineable(Material material, int xp, int time) {
        Xp = xp;
        Time = time;
        Material = material;
    }

    public Mineable(Material Material){
        Xp = 0;
        Time = 1;
        this.Material = Material;
    }

    public int getXp() {
        return Xp;
    }

    public void setXp(int newXP) { Xp = newXP;}

    public int getTime() {
        return Time;
    }

    public void setTime(int newTime) { Time = newTime;}

    public Material getMaterial() {
        return Material;
    }
}
