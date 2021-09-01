package TGamingStudio.Prison.Util;

import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;

import java.text.DecimalFormat;
public class MessageBuilder {
    Prison Prison;

    public MessageBuilder(Prison instance) {
        Prison = instance;
    }

    public static String Color(String Input) {
        return ChatColor.translateAlternateColorCodes('&', Input);
    }

    DecimalFormat DecimalFormat = new DecimalFormat("###,###,###");

    private String replaceVariable(String Input, String... args) {
        for (int i = 0; i < args.length; i++) {
            Input = Input.replace("%" + i + "%", args[i]);
            try {
                Input = Input.replace("%f" + i + "%", DecimalFormat.format(Double.valueOf(args[i])));
            } catch (Exception E) {
                Input = Input.replace("%f" + i + "%", args[i]);
            }
        }
        return Input;
    }

    public String Build(String Input, String... args) {
        return Color(Prison.getMessages().PREFIX + replaceVariable(Input, args));
    }

    public String BuildPrefixless(String Input, String... args) {
        return Color(replaceVariable(Input, args));
    }
}
