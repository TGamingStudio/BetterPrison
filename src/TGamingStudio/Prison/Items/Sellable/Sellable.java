package TGamingStudio.Prison.Items;

import org.bukkit.Material;
public class Sellable {
    private int Money;
    Material Material;

    public Sellable(Material material, int money) {
        Money = money;
        Material = material;
    }

    public Sellable(Material material) {
        Material = material;
        Money = 0;
    }

    public int getMoney() {
        return Money;
    }

    public void setMoney(int newMoney) {
        Money = newMoney;
    }

    public Material getMaterial() {
        return Material;
    }
}
