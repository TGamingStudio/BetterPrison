package TGamingStudio.Prison.Util;

import TGamingStudio.Prison.Prison;
import net.md_5.bungee.api.ChatColor;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
public class MessageBuilder {
    Prison Prison;

    public MessageBuilder(Prison instance) {
        Prison = instance;
    }

    public static String Color(String Input) {
        if (Input == null) return null;
        return ChatColor.translateAlternateColorCodes('&', Input);
    }

    DecimalFormat DecimalFormat = new DecimalFormat("###,###,###");

    private String replaceVariable(String Input, String... args) {
        if (Input == null) return null;
        if (Arrays.stream(args).anyMatch(Objects::isNull)) return null;
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
