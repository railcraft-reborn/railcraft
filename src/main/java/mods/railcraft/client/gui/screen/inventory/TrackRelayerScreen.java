package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.TrackRelayerMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class TrackRelayerScreen extends MaintenanceMinecartScreen<TrackRelayerMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/track_relayer.png");

  public TrackRelayerScreen(TrackRelayerMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, menu.getTrackRelayer());
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
