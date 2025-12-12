package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.world.inventory.CargoMinecartMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CargoMinecartScreen extends RailcraftMenuScreen<CargoMinecartMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/cargo_minecart.png");

  public CargoMinecartScreen(CargoMinecartMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, Component.translatable(Translations.Screen.FILTER),
        this.titleLabelX, 22, IngameWindowScreen.TEXT_COLOR, false);
  }
}
