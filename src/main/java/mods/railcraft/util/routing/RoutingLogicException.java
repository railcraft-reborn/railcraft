package mods.railcraft.util.routing;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class RoutingLogicException extends Exception {

  private final Component tooltip;

  RoutingLogicException(String errorKey) {
    tooltip = Component.translatable(errorKey).withStyle(ChatFormatting.RED);
  }

  public RoutingLogicException(String errorKey, String line) {
    var error = Component.translatable(errorKey).withStyle(ChatFormatting.RED);
    var lineLiteral = Component.literal("\"" + line + "\"");
    tooltip = CommonComponents.joinLines(error, lineLiteral);
  }

  public Component getToolTip() {
    return this.tooltip;
  }
}
