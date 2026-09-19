package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.inventory.CartDispenserMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CartDispenserScreen extends RailcraftMenuScreen<CartDispenserMenu> {

  private static final Identifier WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.id("textures/gui/container/cart_slots.png");

  public CartDispenserScreen(CartDispenserMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title, 140);
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  public Identifier getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
