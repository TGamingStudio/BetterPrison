package TGamingStudio.Prison.Profile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public class Profile {
    private UUID Uuid;
    private int xp;

    public Profile(UUID Uuid, int Xp) {
        this.Uuid = Uuid;
        this.xp = Xp;
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

    public void AddXp(int Xp) {
        xp += Xp;
    }
}
