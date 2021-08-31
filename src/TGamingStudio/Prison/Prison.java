package TGamingStudio.Prison;

import TGamingStudio.Prison.Area.AreaSetup;
import TGamingStudio.Prison.Commands.Mines;
import TGamingStudio.Prison.Commands.SellAll;
import TGamingStudio.Prison.Commands.ShowXP;
import TGamingStudio.Prison.Events.*;
import TGamingStudio.Prison.Items.MineableSetup;
import TGamingStudio.Prison.Items.SellableManager;
import TGamingStudio.Prison.Items.SellableSetup;
import TGamingStudio.Prison.Util.MessageBuilder;
import TGamingStudio.Prison.Util.Messages;
import com.mojang.authlib.minecraft.client.ObjectMapper;
import com.mojang.brigadier.Message;
import net.milkbowl.vault.economy.Economy;
import TGamingStudio.Prison.Area.AreaManager;
import TGamingStudio.Prison.Commands.PrisonCommand;
import TGamingStudio.Prison.Commands.TabCompleters.PrisonCommandCompleter;
import TGamingStudio.Prison.Items.MineableManager;
import TGamingStudio.Prison.Profile.ProfileManager;
import TGamingStudio.Prison.Util.MinedBlocks;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

public class Prison extends JavaPlugin {
    private AreaManager AreaManager;
    private MineableManager MineableManager;
    private SellableManager SellableManager;
    private ProfileManager ProfileManager;
    private MinedBlocks MinedBlocks;
    private Messages Messages;
    private MessageBuilder MessageBuilder;
    private AreaSetup AreaSetup;
    private SellableSetup SellableSetup;
    private MineableSetup MineableSetup;
    private ShowXP ShowXP;
    private Economy Economy;
    private boolean EnabledEconomy;

    public void onEnable() {
        AreaManager = new AreaManager(this);
        MineableManager = new MineableManager(this);
        SellableManager = new SellableManager(this);
        ProfileManager = new ProfileManager(this);
        MinedBlocks = new MinedBlocks(this);
        Messages = new Messages(this);
        MessageBuilder = new MessageBuilder(this);
        AreaSetup = new AreaSetup(this);
        SellableSetup = new SellableSetup(this);
        MineableSetup = new MineableSetup(this);

        ShowXP = new ShowXP(this);

        this.saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new MiningEvents(this), this);
        getServer().getPluginManager().registerEvents(new MovingEvents(this), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
        getServer().getPluginManager().registerEvents(new ChatEvents(this), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(this), this);
        getCommand("prison").setExecutor(new PrisonCommand(this));
        getCommand("prison").setTabCompleter(new PrisonCommandCompleter());
        getCommand("sellall").setExecutor(new SellAll(this));
        getCommand("showxp").setExecutor(ShowXP);
        getCommand("mines").setExecutor(new Mines(this));

        EnabledEconomy = setupEconomy();
        setupPlaceholders();

        AreaManager.LoadAreas();
        MineableManager.LoadMineables();
        SellableManager.LoadSellable();
        getServer().getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(ProfileManager::LoadProfile);
    }

    private void setupPlaceholders() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") == null)
            return;
        new PrisonPlaceholders(this).register();
    }

    private boolean setupEconomy() {
        if (Bukkit.getPluginManager().getPlugin("Vault") == null)
            return false;

        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        Economy = rsp.getProvider();
        return Economy != null;
    }

    public void onDisable() {
        AreaManager.SaveAreas();
        MineableManager.SaveMineables();
        SellableManager.SaveSellables();
        MinedBlocks.ResetAll();
        getServer().getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(ProfileManager::SaveProfile);
    }

    public void ReloadPlugin() {
        reloadConfig();
        AreaManager.Config = getConfig();
        MineableManager.Config = getConfig();
        SellableManager.Config = getConfig();
        AreaManager.LoadAreas();
        MineableManager.LoadMineables();
        SellableManager.LoadSellable();
        Messages.Reload();
        getServer().getOnlinePlayers().stream().map(Entity::getUniqueId).forEach(ProfileManager::LoadProfile);
    }

    public boolean checkForUpade() {
        try {
            URLConnection connection = (new URL("https://api.spigotmc.org/legacy/update.php?resource=95811")).openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String version = reader.readLine();
            int NewestVersion = Integer.parseInt(version.replace(".", ""));
            int CurrentVersion = Integer.parseInt(getDescription().getVersion().replace(".", ""));
            if(CurrentVersion < NewestVersion)
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public AreaManager getAreaManager() {
        return AreaManager;
    }

    public MineableManager getMineableManager() {
        return MineableManager;
    }

    public SellableManager getSellableManager() {
        return SellableManager;
    }

    public ProfileManager getProfileManager() {
        return ProfileManager;
    }

    public MinedBlocks getMinedBlocks() {
        return MinedBlocks;
    }

    public Messages getMessages() {
        return Messages;
    }

    public MessageBuilder getMessageBuilder() {
        return MessageBuilder;
    }

    public AreaSetup getAreaSetup() {
        return AreaSetup;
    }

    public SellableSetup getSellableSetup() {
        return SellableSetup;
    }

    public MineableSetup getMineableSetup() {
        return MineableSetup;
    }

    public ShowXP getShowXP() {
        return ShowXP;
    }

    public Economy getEconomy() {
        return Economy;
    }

    public boolean isEnabledEconomy() {
        return EnabledEconomy;
    }
}
