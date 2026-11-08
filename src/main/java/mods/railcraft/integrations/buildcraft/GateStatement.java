package mods.railcraft.integrations.buildcraft;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.resources.Identifier;

/**
 * BuildCraft gate triggers and actions provided by Railcraft.
 */
public enum GateStatement {

  TRIGGER_WORK("trigger_work", "Has Work"),
  TRIGGER_CART("trigger_cart", "Has Cart"),
  TRIGGER_FUEL("trigger_fuel", "Low Fuel"),
  TRIGGER_TEMP_COLD("trigger_temp_cold", "Temperature: Cold"),
  TRIGGER_TEMP_WARM("trigger_temp_warm", "Temperature: Warm"),
  TRIGGER_TEMP_HOT("trigger_temp_hot", "Temperature: Hot"),
  TRIGGER_ENGINE_BLUE("trigger_engine_blue", "Engine Blue"),
  TRIGGER_ENGINE_GREEN("trigger_engine_green", "Engine Green"),
  TRIGGER_ENGINE_YELLOW("trigger_engine_yellow", "Engine Yellow"),
  TRIGGER_ENGINE_ORANGE("trigger_engine_orange", "Engine Orange"),
  TRIGGER_ENGINE_RED("trigger_engine_red", "Engine Red"),
  TRIGGER_MAINTENANCE("trigger_maintenance", "Needs Maintenance"),
  TRIGGER_ASPECT_GREEN("trigger_aspect_green", "Signal Aspect: Green"),
  TRIGGER_ASPECT_YELLOW_BLINK("trigger_aspect_yellow_blink", "Signal Aspect: Blinking Yellow"),
  TRIGGER_ASPECT_YELLOW("trigger_aspect_yellow", "Signal Aspect: Yellow"),
  TRIGGER_ASPECT_RED_BLINK("trigger_aspect_red_blink", "Signal Aspect: Blinking Red"),
  TRIGGER_ASPECT_RED("trigger_aspect_red", "Signal Aspect: Red"),
  TRIGGER_ASPECT_OFF("trigger_aspect_off", "Signal Aspect: Off"),
  ACTION_PAUSE("action_pause", "Pause"),
  ACTION_SEND_CART("action_send_cart", "Send Cart");

  private static final String TEXTURE_DIRECTORY = "textures/integration/buildcraft/";

  private final String name;
  private final String englishName;

  GateStatement(String name, String englishName) {
    this.name = name;
    this.englishName = englishName;
  }

  public String getName() {
    return this.name;
  }

  /**
   * Id used by BuildCraft to register the statement and save it in gates.
   */
  public Identifier getId() {
    return RailcraftConstants.id(this.name);
  }

  public String getTranslationKey() {
    return Translations.makeKey("gate", this.name);
  }

  public String getEnglishName() {
    return this.englishName;
  }

  /**
   * Full path of the icon texture, BuildCraft binds it directly (not from an atlas).
   */
  public Identifier getIcon() {
    return RailcraftConstants.id(TEXTURE_DIRECTORY + this.name + ".png");
  }
}
