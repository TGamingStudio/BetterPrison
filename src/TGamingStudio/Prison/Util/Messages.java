package TGamingStudio.Prison.Util;

import TGamingStudio.Prison.Prison;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
public class Messages {
    Prison Prison;
    private File CustomConfigFile;
    private FileConfiguration CustomConfig;

    public Messages(Prison instance) {
        Prison = instance;
        CustomConfigFile = new File(Prison.getDataFolder(), "messages.yml");
        if (!CustomConfigFile.exists()) {
            CustomConfigFile.getParentFile().mkdirs();
            Prison.saveResource("messages.yml", false);
        }
        CustomConfig = new YamlConfiguration();
        try {
            CustomConfig.load(CustomConfigFile);
            Reader Reader = new InputStreamReader(Prison.getResource("messages.yml"));
            YamlConfiguration.loadConfiguration(Reader).getKeys(true).forEach(Key -> {
                if (CustomConfig.get(Key) == null) {
                    CustomConfig.set(Key, YamlConfiguration.loadConfiguration(new InputStreamReader(Prison.getResource("messages.yml"))).get(Key));
                    Prison.getLogger().info("Generating missing property in messages.yml config due to an update or being deleted by user (" + Key + ")");
                }
            });
            CustomConfig.save(CustomConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            Prison.getLogger().info("Invalid Config File. (messages.yml)");
        }
        LoadMessages();
    }

    public void Reload() {
        try {
            CustomConfig.load(CustomConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            Prison.getLogger().info("Invalid Config File. (messages.yml)");
        }
        LoadMessages();
    }

    public String PREFIX;

    public String MISSING_PERMISSIONS;
    public String NOT_ENOUGH_EXPERIENCE;
    public String NUMBER_EXPECTED;
    public String ITEM_EXPECTED;
    public String INVALID_PLAYER;

    public String CONFIG_RELOADED;
    public String CONFIG_WARNING;
    public String COMMAND_XP;
    public String COMMAND_XP_CHANGE;
    public String COMMAND_SELLALL;
    public String COMMAND_SHOWXP;

    public String ENABLED;
    public String DISABLED;


    public String AREA_LOCKED;
    public String AREA_UNLOCKED;
    public String STANDING_AREA_NONE;
    public String MAX_AREA_REACHED;
    public String NO_AREA_UNLOCKED;
    public String INVALID_ARENA_NAME;

    public boolean AREA_ENTER_ENABLED;
    public int AREA_ENTER_TIME;
    public String AREA_ENTER_TITLE;
    public String AREA_ENTER_SUBTITLE;

    public String AREA_SETUP_NAME;
    public String AREA_SETUP_XP;
    public String AREA_SETUP_UNFINISHED;
    public String AREA_SETUP_SAVED;
    public String AREA_SETUP_DELETED;

    public String ITEMS_SETUP_PRICE;
    public String ITEMS_SETUP_SAVED;
    public String ITEMS_SETUP_DELETED;

    public String BLOCKS_SETUP_XP;
    public String BLOCKS_SETUP_TIME;
    public String BLOCKS_SETUP_SAVED;
    public String BLOCKS_SETUP_DELETED;

    public String MINES_GUI_TELEPORT;
    public String MINES_GUI_TITLECOLOR;
    public String MINES_GUI_XP;
    public String MINES_GUI_TITLE;

    public void LoadMessages() {
        PREFIX = CustomConfig.getString("prefix") + " ";

        MISSING_PERMISSIONS = CustomConfig.getString("missing_permissions");
        NOT_ENOUGH_EXPERIENCE = CustomConfig.getString("not_enough_experience");
        NUMBER_EXPECTED = CustomConfig.getString("number_expected");
        ITEM_EXPECTED = CustomConfig.getString("item_expected");
        INVALID_PLAYER = CustomConfig.getString("invalid_player");

        CONFIG_RELOADED = CustomConfig.getString("config.reloaded");
        CONFIG_WARNING = CustomConfig.getString("config.warning");
        COMMAND_XP = CustomConfig.getString("commands.xp");
        COMMAND_XP_CHANGE = CustomConfig.getString("commands.xp_change");
        COMMAND_SELLALL = CustomConfig.getString("commands.sellall");
        COMMAND_SHOWXP = CustomConfig.getString("commands.showxp");

        ENABLED = CustomConfig.getString("enabled");
        DISABLED = CustomConfig.getString("disabled");


        AREA_LOCKED = CustomConfig.getString("area.locked");
        AREA_UNLOCKED = CustomConfig.getString("area.unlocked");
        STANDING_AREA_NONE = CustomConfig.getString("area.current_area_none");
        MAX_AREA_REACHED = CustomConfig.getString("area.max_area_reached");
        NO_AREA_UNLOCKED = CustomConfig.getString("area.no_area_unlocked");
        INVALID_ARENA_NAME = CustomConfig.getString("area.invalid_arena_name");

        AREA_ENTER_ENABLED = CustomConfig.getBoolean("area.enter.enabled");
        AREA_ENTER_TIME = CustomConfig.getInt("area.enter.time");
        AREA_ENTER_TITLE = CustomConfig.getString("area.enter.title");
        AREA_ENTER_SUBTITLE = CustomConfig.getString("area.enter.subtitle");

        AREA_SETUP_NAME = CustomConfig.getString("area.setup.name");
        AREA_SETUP_XP = CustomConfig.getString("area.setup.xp");
        AREA_SETUP_UNFINISHED = CustomConfig.getString("area.setup.unfinished");
        AREA_SETUP_SAVED = CustomConfig.getString("area.setup.saved");
        AREA_SETUP_DELETED = CustomConfig.getString("area.setup.deleted");

        ITEMS_SETUP_PRICE = CustomConfig.getString("items.setup.price");
        ITEMS_SETUP_SAVED = CustomConfig.getString("items.setup.saved");
        ITEMS_SETUP_DELETED = CustomConfig.getString("items.setup.deleted");

        BLOCKS_SETUP_XP = CustomConfig.getString("blocks.setup.xp");
        BLOCKS_SETUP_TIME = CustomConfig.getString("blocks.setup.time");
        BLOCKS_SETUP_SAVED = CustomConfig.getString("blocks.setup.saved");
        BLOCKS_SETUP_DELETED = CustomConfig.getString("blocks.setup.deleted");

        MINES_GUI_TITLE = CustomConfig.getString("mines-gui.title");
        MINES_GUI_TELEPORT = CustomConfig.getString("mines-gui.teleport");
        MINES_GUI_TITLECOLOR = CustomConfig.getString("mines-gui.itemcolor");
        MINES_GUI_XP = CustomConfig.getString("mines-gui.xp");
    }

    public String getAreaLocked(boolean unlocked) {
        return unlocked ? AREA_UNLOCKED : AREA_LOCKED;
    }

    public String getEnabled(boolean enabled) {
        return enabled ? ENABLED : DISABLED;
    }
}
