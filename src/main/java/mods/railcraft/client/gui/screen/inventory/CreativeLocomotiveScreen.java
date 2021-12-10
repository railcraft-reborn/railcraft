package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.cart.locomotive.CreativeLocomotiveEntity;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;

public class CreativeLocomotiveScreen
    extends LocomotiveScreen<LocomotiveMenu<CreativeLocomotiveEntity>> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/creative_locomotive.png");

  public CreativeLocomotiveScreen(LocomotiveMenu<CreativeLocomotiveEntity> menu,
      Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, "creative");
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return TEXTURE_LOCATION;
  }
}
