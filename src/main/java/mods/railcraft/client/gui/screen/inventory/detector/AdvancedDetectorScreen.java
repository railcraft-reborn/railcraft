package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.world.inventory.detector.AdvancedDetectorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class AdvancedDetectorScreen extends RailcraftMenuScreen<AdvancedDetectorMenu> {

  private static final Identifier BACKGROUND_TEXTURE =
      RailcraftConstants.id("textures/gui/container/advanced_detector.png");

  public AdvancedDetectorScreen(AdvancedDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, 140);
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public Identifier getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }
}
