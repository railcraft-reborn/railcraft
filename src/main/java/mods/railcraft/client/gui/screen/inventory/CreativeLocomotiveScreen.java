package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CreativeLocomotiveScreen
    extends LocomotiveScreen<LocomotiveMenu<CreativeLocomotive>> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/creative_locomotive.png");

  public CreativeLocomotiveScreen(LocomotiveMenu<CreativeLocomotive> menu,
      Inventory inventory, Component title) {
    super(menu, inventory, title, "creative");
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
