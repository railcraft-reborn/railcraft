package mods.railcraft.client.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class FluidRenderer {

  private static final Minecraft minecraft = Minecraft.getInstance();

  private static final FluidRenderMap<Int2ObjectMap<CuboidModel>> cachedCenterFluids =
      new FluidRenderMap<>();

  public static final int STAGES = 100;

  public static CuboidModel getFluidModel(FluidStack fluid, int stage, FluidType type) {
    if (cachedCenterFluids.containsKey(fluid) && cachedCenterFluids.get(fluid).containsKey(stage)) {
      return cachedCenterFluids.get(fluid).get(stage);
    }

    CuboidModel model = new CuboidModel();
    model.setAll(model.new Face().setSprite(
        FluidRenderer.getFluidTexture(fluid, type)));

    model.setMinX(0.01F);
    model.setMinY(0.0F);
    model.setMinZ(0.01F);

    model.setMaxX(0.99F);
    model.setMaxY(stage / (float) STAGES);
    model.setMaxZ(0.99F);

    cachedCenterFluids.computeIfAbsent(fluid, f -> new Int2ObjectOpenHashMap<>())
        .put(stage, model);
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
    return minecraft.getTextureAtlas(PlayerContainer.BLOCK_ATLAS).apply(spriteLocation);
  }

  public enum FluidType {
    STILL,
    FLOWING
  }
}
