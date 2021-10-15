package mods.railcraft.client.renderer.blockentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import mods.railcraft.Railcraft;
import mods.railcraft.client.util.CuboidModel;
import mods.railcraft.client.util.CuboidModelRenderer;
import mods.railcraft.client.util.FluidRenderer;
import mods.railcraft.client.util.RenderUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Atlases;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

public class FluidManipulatorRenderer extends TileEntityRenderer<FluidManipulatorBlockEntity> {

  public static final ResourceLocation INTERIOR_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/fluid_manipulator/interior");

  private static final CuboidModel interiorModel =
      new CuboidModel(0.011F, 0.01F, 0.011F, 0.989F, 0.99F, 0.989F);


  public FluidManipulatorRenderer(TileEntityRendererDispatcher renderer) {
    super(renderer);
  }

  @Override
  public void render(FluidManipulatorBlockEntity blockEntity, float partialTicks,
      MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int packedLight,
      int packedOverlay) {
    interiorModel.setAll(interiorModel.new Face()
        .setSprite(Minecraft.getInstance().getTextureAtlas(PlayerContainer.BLOCK_ATLAS)
            .apply(INTERIOR_TEXTURE_LOCATION)));
    interiorModel.setPackedLight(packedLight);
    interiorModel.setPackedOverlay(packedOverlay);

    IVertexBuilder builder = renderTypeBuffer.getBuffer(Atlases.cutoutBlockSheet());
    CuboidModelRenderer.render(interiorModel, matrixStack, builder,
        0xFFFFFFFF, CuboidModelRenderer.FaceDisplay.FRONT, true);

    StandardTank tank = blockEntity.getTankManager().get(0);
    FluidStack fluidStack = tank.getFluid();
    if (fluidStack != null && fluidStack.getAmount() > 0) {
      float cap = tank.getCapacity();
      float level = Math.min(fluidStack.getAmount(), cap) / cap;

      int modelNumber;
      if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
        modelNumber = FluidRenderer.STAGES - 1;
      } else {
        modelNumber =
            Math.min(FluidRenderer.STAGES - 1, (int) (level * (FluidRenderer.STAGES - 1)));
      }

      CuboidModel model = FluidRenderer.getFluidModel(fluidStack, modelNumber);
      if (model != null) {
        matrixStack.pushPose();
        {
          matrixStack.translate(0.0F, 0.125F, 0.0F);
          model.setPackedLight(RenderUtil.calculateGlowLight(packedLight, fluidStack));
          model.setPackedOverlay(packedOverlay);
          CuboidModelRenderer.render(model, matrixStack, builder,
              RenderUtil.getColorARGB(fluidStack, level),
              CuboidModelRenderer.FaceDisplay.FRONT, true);
        }
        matrixStack.popPose();
      }
    }
  }
}
