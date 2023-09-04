package mods.railcraft.util.routing;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class RoutingLogicException extends Exception {

  private static final long serialVersionUID = -8668211003307380722L;

  private final List<Component> tooltip;

  public RoutingLogicException(String errorKey) {
    this.tooltip = List.of(Component.translatable(errorKey).withStyle(ChatFormatting.RED));
  }

  public RoutingLogicException(String errorKey, String line) {
    var error = Component.translatable(errorKey).withStyle(ChatFormatting.RED);
    var lineLiteral = Component.literal("\"" + line + "\"");
    this.tooltip = List.of(error, lineLiteral);
  }

  public List<Component> getTooltip() {
    return this.tooltip;
  }
}
