package mods.railcraft.util.routing;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;

public class RoutingStatementParser {

  public static final String REGEX_SYMBOL = "\\?";

  public static ParsedStatement parse(String keyword, boolean supportsRegex, String line)
      throws RoutingLogicException {
    String keywordMatch = keyword + REGEX_SYMBOL + "?=";
    if (!line.matches(keywordMatch + ".*")) {
      throw new RoutingLogicException(Translations.RoutingTable.UNRECOGNIZED_KEYWORD, line);
    }
    var isRegex = line.matches(keyword + REGEX_SYMBOL + "=.*");
    if (!supportsRegex && isRegex) {
      throw new RoutingLogicException(Translations.RoutingTable.ERROR_UNSUPPORTED_REGEX, line);
    }
    var value = line.replaceFirst(keywordMatch, "");
    Pattern pattern = null;
    if (isRegex) {
      try {
        pattern = Pattern.compile(line);
      } catch (PatternSyntaxException ex) {
        throw new RoutingLogicException(Translations.RoutingTable.ERROR_INVALID_REGEX, line);
      }
    }
    return new ParsedStatement(value, pattern);
  }

  public record ParsedStatement(String value, @Nullable Pattern pattern) {

    public boolean isRegex() {
      return this.pattern != null;
    }
  }
}
