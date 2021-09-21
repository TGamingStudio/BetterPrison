package TGamingStudio.Prison.Items;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class MineableManager {
    private HashMap<Material, Mineable> Mineables = new HashMap<>();

    Prison Prison;
    public FileConfiguration Config;

    public MineableManager(Prison instance) {
        Prison = instance;
        Config = Prison.getConfig();
    }

    public void LoadMineables() {
        if (Config.get("blocks") == null) {
            GenerateDummyMineable();
        }
        Mineables = new HashMap<>();
        for (String MineableConfigPath : Config.getConfigurationSection("blocks").getKeys(false)) {
            int xp = Config.getInt("blocks." + MineableConfigPath + ".xp");
            int time = Config.getInt("blocks." + MineableConfigPath + ".time");
            AddMineable(Material.getMaterial(MineableConfigPath), new Mineable(Material.getMaterial(MineableConfigPath), xp, time));
        }
    }

    public Mineable getMineable(Material Material) {
        return Mineables.get(Material);
    }

    public List<String> getMineables() {
        List<String> MaterialList = new ArrayList<>(Mineables.size());
        for (Material Mineable : Mineables.keySet()) {
            MaterialList.add(String.valueOf(Mineable.toString()));
        }
        return MaterialList;
    }

    private void GenerateDummyMineable() {
        AddMineable(Material.getMaterial("STONE"), new Mineable(Material.getMaterial("STONE"), 1, 20));
        SaveMineables();
    }

    public void SaveMineables() {
        for (Material Material : Mineables.keySet()) {
            Config.set("blocks." + Material + ".xp", Mineables.get(Material).getXp());
            Config.set("blocks." + Material + ".time", Mineables.get(Material).getTime());
        }
        Prison.saveConfig();
    }

    public void AddMineable(Material Material, Mineable Mineable) {
        Mineables.put(Material, Mineable);
    }

    public void RemoveMineable(Mineable Mineable) {
        Config.set("blocks." + Mineable.getMaterial().toString(), null);
        Mineables.remove(Mineable.getMaterial());
    }
}
