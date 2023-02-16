package mods.railcraft.util.routing;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class RoutingLogicException extends Exception {

  private final List<Component> tooltip;

  public RoutingLogicException(String errorKey) {
    tooltip = List.of(Component.translatable(errorKey).withStyle(ChatFormatting.RED));
  }

  public RoutingLogicException(String errorKey, String line) {
    var error = Component.translatable(errorKey).withStyle(ChatFormatting.RED);
    var lineLiteral = Component.literal("\"" + line + "\"");
    tooltip = List.of(error, lineLiteral);
  }

  public List<Component> getToolTip() {
    return this.tooltip;
  }
}
