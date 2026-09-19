package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CreativeLocomotiveScreen
    extends LocomotiveScreen<LocomotiveMenu<CreativeLocomotive>> {

  private static final Identifier TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/creative_locomotive.png");

  public CreativeLocomotiveScreen(LocomotiveMenu<CreativeLocomotive> menu,
      Inventory inventory, Component title) {
    super(menu, inventory, title, "creative");
  }

  @Override
  public Identifier getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
