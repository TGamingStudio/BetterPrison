package TGamingStudio.Prison;

import TGamingStudio.Prison.Area.Area;
import com.mysql.fabric.xmlrpc.base.Value;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class PrisonPlaceholders extends PlaceholderExpansion {

    Prison Prison;

    public PrisonPlaceholders(Prison instance) {
        Prison = instance;
    }

    @Override
    public String getIdentifier() {
        return "prison";
    }

    @Override
    public String getAuthor() {
        return "TGamingStudio";
    }

    @Override
    public String getVersion() {
        return Prison.getDescription().getVersion();
    }


    DecimalFormat DecimalFormat = new DecimalFormat("###,###,###");

    public String ValueFromAreaParams(Area Area, Player player, String params) {
        return switch (params) {
            case "name" -> Area.getName();
            case "xp" -> String.valueOf(Area.getMinXP());
            case "xp_formatted" -> DecimalFormat.format(Area.getMinXP());
            case "xp_remaining" -> String.valueOf(Area.getMinXP() - Prison.getProfileManager().getProfile(player.getUniqueId()).getXp());
            case "xp_remaining_formatted" -> DecimalFormat.format(Area.getMinXP() - Prison.getProfileManager().getProfile(player.getUniqueId()).getXp());
            case "unlocked" -> Prison.getMessages().getAreaLocked(Prison.getAreaManager().isUnlocked(Area, player));
            case "player_count" -> String.valueOf(Prison.getAreaManager().PlayersInArea(Area).size());

            default -> null;
        };
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        String Result = switch (params) {

            case "xp" -> String.valueOf(Prison.getProfileManager().getProfile(player.getUniqueId()).getXp());
            case "blocks_mined" -> String.valueOf(Prison.getProfileManager().getProfile(player.getUniqueId()).getBlocksMined());

            case "xp_formatted" -> DecimalFormat.format(Prison.getProfileManager().getProfile(player.getUniqueId()).getXp());
            case "blocks_mined_formatted" -> DecimalFormat.format(Prison.getProfileManager().getProfile(player.getUniqueId()).getBlocksMined());

            default -> null;
        };

        Pattern Regexp;
        Matcher Matches;

        Regexp = Pattern.compile("(\\w*)_area_(\\w*)");
        Matches = Regexp.matcher(params);
        if (Matches.find()) {
            String Prefix = Matches.group(1);
            String Params = Matches.group(2);
            Area Area = switch (Prefix) {
                case "standing" -> Prison.getAreaManager().StandingArea(player.getLocation());
                case "next" -> Prison.getAreaManager().NextArea(player);
                case "best_unlocked" -> Prison.getAreaManager().HighestUnlockedByRequiredXP(player);
                default -> throw new IllegalStateException("Error while parsing a placeholder (Unexpected Area Prefix)");
            };
            if (Area != null) {
                Result = ValueFromAreaParams(Area, player, Params);
            } else {
                Result = switch (Prefix) {
                    case "standing" -> Prison.getMessages().STANDING_AREA_NONE;
                    case "next" -> Prison.getMessages().MAX_AREA_REACHED;
                    case "best_unlocked" -> Prison.getMessages().NO_AREA_UNLOCKED;
                    default -> throw new IllegalStateException("Error while parsing a placeholder (Unexpected Area Prefix)");
                };
            }
        }

        if (Result != null) return Result;

        Regexp = Pattern.compile("area_(\\w*)_(\\w*)");
        Matches = Regexp.matcher(params);
        if (Matches.find()) {
            String Name = Matches.group(1);
            Area Area = Prison.getAreaManager().getArea(Name);
            if (Area != null) {
                String endParams = params.substring(Name.length() + 6);
                return ValueFromAreaParams(Area, player, endParams);
            } else {
                return Prison.getMessages().INVALID_ARENA_NAME;
            }
        }


        return Result;
    }
}
