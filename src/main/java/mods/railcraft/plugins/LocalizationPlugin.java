package mods.railcraft.plugins;

import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Map;
import mods.railcraft.api.core.ILocalizedObject;
import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class LocalizationPlugin {

  public static final String ENGLISH = "en_us";

  public static String convertTag(String tag) {
    return tag.replace("_", ".");
  }

  public static String translate(String tag) {
    return I18n.get(tag).replace("\\n", "\n").replace("\\%", "@");
  }

  public static String translateFast(String tag) {
    return I18n.get(tag);
  }

  public static String translate(String tag, ILocalizedObject... args) {
    String text = translate(tag);

    Object[] objects =
        Arrays.stream(args).map(a -> translateFast(a.getLocalizationTag())).toArray();

    try {
      return String.format(text, objects);
    } catch (IllegalFormatException ex) {
      return "Format error: " + text;
    }
  }

  public static String translate(String tag, Object... args) {
    String text = translate(tag);

    try {
      return String.format(text, args);
    } catch (IllegalFormatException ex) {
      return "Format error: " + text;
    }
  }

  public static String format(String tag, Object... args) {
    String text = translate(tag);
    for (int ii = 0; ii < args.length; ii++) {
      if (args[ii] instanceof Double) {
        args[ii] = HumanReadableNumberFormatter.format((Double) args[ii]);
      }
    }

    try {
      return String.format(text, args);
    } catch (IllegalFormatException ex) {
      return "Format error: " + text;
    }
  }

  public static String translateArgs(String tag, Map<String, ILocalizedObject> args) {
    String text = translate(tag);
    for (Map.Entry<String, ILocalizedObject> arg : args.entrySet()) {
      text = text.replace("{" + arg.getKey() + "}",
          translateFast(arg.getValue().getLocalizationTag()));
    }
    return text;
  }

  public static boolean hasTag(String tag) {
    return I18n.exists(tag);
  }

  public static String getEntityLocalizationTag(Entity entity) {
    return entity.getType().getDescriptionId();
  }
}
