package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.TrackLayerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TrackLayerScreen extends MaintenanceMinecartScreen<TrackLayerMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/track_layer.png");

  public TrackLayerScreen(TrackLayerMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, menu.getTrackLayer());
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
