package mods.railcraft.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class FluidRenderer {

  private static final Minecraft minecraft = Minecraft.getInstance();

  public static CuboidModel getFluidModel(FluidStack fluid, float maxX, float maxY, float maxZ,
      FluidType type) {
    var model = new CuboidModel();
    model.setAll(model.new Face().setSprite(
        FluidRenderer.getFluidTexture(fluid, type)));

    model.setMinX(0.0F);
    model.setMinY(0.0F);
    model.setMinZ(0.0F);

    model.setMaxX(maxX);
    model.setMaxY(maxY);
    model.setMaxZ(maxZ);

    return model;
  }

  public static TextureAtlasSprite getFluidTexture(FluidStack fluidStack,
      FluidType type) {
    Fluid fluid = fluidStack.getFluid();
    ResourceLocation spriteLocation;
    if (type == FluidType.STILL) {
      spriteLocation = fluid.getAttributes().getStillTexture(fluidStack);
    } else {
      spriteLocation = fluid.getAttributes().getFlowingTexture(fluidStack);
    }
    return minecraft.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(spriteLocation);
  }

  public enum FluidType {
    STILL,
    FLOWING
  }
}
