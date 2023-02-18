package mods.railcraft.util.routing.expression.condition;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import mods.railcraft.Translations;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.util.routing.RoutingLogicException;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

public abstract class ParsedCondition implements Condition {

  public final String value;
  protected final boolean isRegex;

  protected ParsedCondition(String keyword, boolean supportsRegex, String line)
      throws RoutingLogicException {
    String keywordMatch = keyword + RoutingLogic.REGEX_SYMBOL + "?=";
    if (!line.matches(keywordMatch + ".*")) {
      throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, line);
    }
    this.isRegex = line.matches(keyword + RoutingLogic.REGEX_SYMBOL + "=.*");
    if (!supportsRegex && isRegex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, line);
    }
    this.value = line.replaceFirst(keywordMatch, "");
    if (isRegex) {
      this.validateRegex(line);
    }
  }

  protected void validateRegex(String line) throws RoutingLogicException {
    try {
      Pattern.compile(value);
    } catch (PatternSyntaxException ex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_INVALID_REGEX, line);
    }
  }

  @Override
  public abstract boolean matches(RouterBlockEntity routerBlockEntity, AbstractMinecart cart);
}
