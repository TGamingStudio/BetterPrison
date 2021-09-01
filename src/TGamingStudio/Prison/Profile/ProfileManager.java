package TGamingStudio.Prison.Profile;

import TGamingStudio.Prison.Prison;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
public class ProfileManager {
    Prison Prison;
    public FileConfiguration Config;

    private Map<UUID, Profile> profiles = new HashMap<>();

    public ProfileManager(Prison instance) {
        Prison = instance;
        Config = Prison.getConfig();
    }

    public void LoadProfile(UUID UUID) {
        String prefix = "profile." + UUID.toString();
        if (Config.getConfigurationSection(prefix) == null) {
            this.profiles.put(UUID, new Profile(UUID, 0, 0));
            SaveProfile(UUID);
            return;
        }
        int Xp = Config.getInt(prefix + ".xp");
        int Blocks = Config.getInt(prefix + ".blocks");
        this.profiles.put(UUID, new Profile(UUID, Xp, Blocks));
    }

    public Profile getProfile(UUID uuid) {
        return profiles.get(uuid);
    }

    public void SaveProfile(UUID uuid){
        String prefix = "profile." + uuid.toString();
        Profile profile = getProfile(uuid);
        if(profile == null) return;
        Config.set(prefix + ".xp", profile.getXp());
        Config.set(prefix + ".blocks", profile.getBlocksMined());
        Prison.saveConfig();
    }
}
