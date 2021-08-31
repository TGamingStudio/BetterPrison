package TGamingStudio.Prison.Items;

import TGamingStudio.Prison.Area.Area;
import TGamingStudio.Prison.Prison;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
public class SellableManager {
    private HashMap<Material, Sellable> Sellables = new HashMap<>();

    Prison Prison;
    public FileConfiguration Config;

    public SellableManager(Prison instance) {
        Prison = instance;
        Config = Prison.getConfig();
    }

    public void LoadSellable() {
        if (Config.get("items") == null) {
            GenerateDummySellable();
        }
        Sellables = new HashMap<>();
        for (String SelalbleConfigPath : Config.getConfigurationSection("items").getKeys(false)) {
            int money = Config.getInt("items." + SelalbleConfigPath + ".money");
            AddSellable(Material.getMaterial(SelalbleConfigPath), new Sellable(Material.getMaterial(SelalbleConfigPath), money));
        }
    }

    public Sellable getSellable(Material Material) {
        return Sellables.get(Material);
    }

    public List<String> getSellables() {
        List<String> MaterialList = new ArrayList<>(Sellables.size());
        for (Material Sellable : Sellables.keySet()) {
            MaterialList.add(String.valueOf(Sellable.toString()));
        }
        return MaterialList;
    }

    private void GenerateDummySellable() {
        AddSellable(Material.getMaterial("COBBLESTONE"), new Sellable(Material.getMaterial("COBBLESTONE"), 1));
        SaveSellables();
    }

    public void SaveSellables() {
        for (Material Material : Sellables.keySet()) {
            Config.set("items." + Material + ".money", Sellables.get(Material).getMoney());
        }
        Prison.saveConfig();
    }

    public void AddSellable(Material Material, Sellable Sellable) {
        Sellables.put(Material, Sellable);
    }

    public void RemoveSellable(Sellable Sellable) {
        Config.set("items." + Sellable.getMaterial().toString(), null);
        Sellables.remove(Sellable.getMaterial());
    }
}
