package mods.railcraft.util.routing;

import java.io.Serial;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;

public class RoutingLogicException extends Exception {

  @Serial
  private static final long serialVersionUID = -8668211003307380722L;

  private final List<ClientTooltipComponent> tooltip;

  public RoutingLogicException(String errorKey) {
    this.tooltip = List.of(ClientTooltipComponent.create(
            Component.translatable(errorKey).withStyle(ChatFormatting.RED).getVisualOrderText()));
  }

  public RoutingLogicException(String errorKey, String line) {
    var error = ClientTooltipComponent.create(
        Component.translatable(errorKey).withStyle(ChatFormatting.RED).getVisualOrderText());
    var lineLiteral = ClientTooltipComponent.create(
        Component.literal("\"" + line + "\"").getVisualOrderText());
    this.tooltip = List.of(error, lineLiteral);
  }

  public List<ClientTooltipComponent> getTooltip() {
    return this.tooltip;
  }
}
