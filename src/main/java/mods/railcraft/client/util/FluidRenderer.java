package mods.railcraft.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
    var fluidModel = Minecraft.getInstance()
        .getModelManager()
        .getFluidStateModelSet()
        .get(fluidStack.getFluid().defaultFluidState());
    var material = switch (type) {
      case STILL -> fluidModel.stillMaterial();
      case FLOWING -> fluidModel.flowingMaterial();
    };
    return material.sprite();
  }

  public enum FluidType {
    STILL,
    FLOWING
  }
}
