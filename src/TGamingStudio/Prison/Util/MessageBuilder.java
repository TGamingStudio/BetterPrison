package TGamingStudio.Prison.Util;

import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;
public class MessageBuilder {
    Prison Prison;

    public MessageBuilder(Prison instance) {
        Prison = instance;
    }

    public static String Color(String Input) {
        return ChatColor.translateAlternateColorCodes('&', Input);
    }

    public String Build(String Input, String... args) {
        for (int i = 0; i < args.length; i++)
            Input = Input.replace("%" + i + "%", args[i]);
        return Color(Prison.getMessages().PREFIX + Input);
    }

    public String BuildPrefixless(String Input, String... args) {
        for (int i = 0; i < args.length; i++)
            Input = Input.replace("%" + i + "%", args[i]);
        return Color(Input);
    }
}
