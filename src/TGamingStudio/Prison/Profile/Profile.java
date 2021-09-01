package TGamingStudio.Prison.Profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Profile {
    private UUID Uuid;
    private int xp;
    private int blocksMined;

    public Profile(UUID Uuid, int Xp, int Blocks) {
        this.Uuid = Uuid;
        this.xp = Xp;
        this.blocksMined = Blocks;
    }

    public UUID getUUID() {
        return Uuid;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(Uuid);
    }

    public int getXp() {
        return xp;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public void AddBlockMined() {
        blocksMined += 1;
    }

    public void AddXp(int Xp) {
        xp += Xp;
    }
}
