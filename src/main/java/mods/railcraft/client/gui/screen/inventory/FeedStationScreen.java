package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.FeedStationMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class FeedStationScreen extends RailcraftMenuScreen<FeedStationMenu> {

  private static final Identifier BACKGROUND_TEXTURE =
      RailcraftConstants.id("textures/gui/container/single_slot.png");

  public FeedStationScreen(FeedStationMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public Identifier getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }
}
