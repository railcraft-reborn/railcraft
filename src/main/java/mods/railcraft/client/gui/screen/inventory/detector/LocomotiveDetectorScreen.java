package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.world.inventory.detector.LocomotiveDetectorMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class LocomotiveDetectorScreen extends RailcraftMenuScreen<LocomotiveDetectorMenu> {

  private static final Identifier BACKGROUND_TEXTURE =
      RailcraftConstants.id("textures/gui/container/double_slot.png");

  public LocomotiveDetectorScreen(LocomotiveDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, 170);
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public Identifier getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, Component.translatable(Translations.Screen.LOCOMOTIVE_DETECTOR_PRIMARY),
        60, 31, 0x404040, false);
    graphics.text(this.font, Component.translatable(Translations.Screen.LOCOMOTIVE_DETECTOR_SECONDARY),
        60, 57, 0x404040, false);
  }
}
