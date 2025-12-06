package mods.railcraft.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class FluidRenderer {

  public static CuboidModel getFluidModel(FluidStack fluid, float maxX, float maxY, float maxZ,
      FluidType type) {
    var model = new CuboidModel();
    model.setAll(model.new Face().setSprite(FluidRenderer.getFluidTexture(fluid, type)));

    model.setMinX(0);
    model.setMinY(0);
    model.setMinZ(0);

    model.setMaxX(maxX);
    model.setMaxY(maxY);
    model.setMaxZ(maxZ);

    return model;
  }

  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack, FluidType type) {
    var fluid = fluidStack.getFluid();
    var spriteLocation = switch (type) {
      case STILL -> IClientFluidTypeExtensions.of(fluid).getStillTexture(fluidStack);
      case FLOWING -> IClientFluidTypeExtensions.of(fluid).getFlowingTexture(fluidStack);
    };
    return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
  }

  public enum FluidType {
    STILL,
    FLOWING
  }
}
