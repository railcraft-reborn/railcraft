package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.CreativeLocomotiveEntity;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class CreativeLocomotiveScreen
    extends LocomotiveScreen<LocomotiveMenu<CreativeLocomotiveEntity>> {

  private static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/creative_locomotive.png");

  public CreativeLocomotiveScreen(LocomotiveMenu<CreativeLocomotiveEntity> menu,
      PlayerInventory playerInventory, ITextComponent title) {
    super(menu, playerInventory, title, "creative");
  }

  @Override
  public ResourceLocation getTextureLocation() {
    return TEXTURE_LOCATION;
  }
}
