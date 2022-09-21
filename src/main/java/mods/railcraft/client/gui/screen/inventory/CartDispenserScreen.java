package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.inventory.CartDispenserMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class CartDispenserScreen extends ManipulatorScreen<CartDispenserMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/cart_slots.png");

  public CartDispenserScreen(CartDispenserMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
  }

  @Override
  public void init() {
    super.init();
    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;
  }

  @Override
  protected void sendAttributes() {

  }


  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
