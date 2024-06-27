package mods.railcraft.client.gui.screen.inventory.detector;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.screen.inventory.RailcraftMenuScreen;
import mods.railcraft.world.inventory.detector.SheepDetectorMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SheepDetectorScreen extends RailcraftMenuScreen<SheepDetectorMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      RailcraftConstants.rl("textures/gui/container/single_slot.png");

  public SheepDetectorScreen(SheepDetectorMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }
}
