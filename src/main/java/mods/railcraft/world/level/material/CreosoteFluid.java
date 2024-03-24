package mods.railcraft.world.level.material;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;
import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.blaze3d.systems.RenderSystem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer.FogMode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidType;

public abstract class CreosoteFluid extends FlowingFluid {

  @Override
  public Fluid getFlowing() {
    return RailcraftFluids.FLOWING_CREOSOTE.get();
  }

  @Override
  public Fluid getSource() {
    return RailcraftFluids.CREOSOTE.get();
  }

  @Override
  public Item getBucket() {
    return RailcraftItems.CREOSOTE_BUCKET.get();
  }

  @Override
  protected int getSlopeFindDistance(LevelReader levelReader) {
    return 4;
  }

  @Override
  protected BlockState createLegacyBlock(FluidState fluidState) {
    return RailcraftBlocks.CREOSOTE.get()
        .defaultBlockState()
        .setValue(LiquidBlock.LEVEL, getLegacyLevel(fluidState));
  }

  @Override
  public boolean isSame(Fluid fluid) {
    return fluid.isSame(RailcraftFluids.FLOWING_CREOSOTE.get())
        || fluid.isSame(RailcraftFluids.CREOSOTE.get());
  }

  @Override
  protected int getDropOff(LevelReader levelReader) {
    return 1;
  }

  @Override
  public int getTickDelay(LevelReader levelReader) {
    return 5;
  }

  @Override
  public boolean canBeReplacedWith(FluidState fluidState, BlockGetter blockGetter,
      BlockPos blockPos, Fluid fluid, Direction direction) {
    return direction == Direction.DOWN && !this.isSame(fluid);
  }

  @Override
  protected float getExplosionResistance() {
    return 50.0F;
  }

  @Override
  public Vec3 getFlow(BlockGetter blockGetter, BlockPos blockPos, FluidState fluidState) {
    return Vec3.ZERO;
  }

  @Override
  public FluidType getFluidType() {
    return RailcraftFluidTypes.CREOSOTE.get();
  }

  public static FluidType createFluidType() {
    var properties = FluidType.Properties.create()
        .density(1100)
        .viscosity(3000);
    return new FluidType(properties) {
      @Override
      public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
          private static final ResourceLocation STILL_TEXTURE =
              new ResourceLocation("block/water_still");
          private static final ResourceLocation FLOW_TEXURE =
              new ResourceLocation("block/water_flow");

          @Override
          public int getTintColor() {
            return 0xFF6A6200; // color is now ARGB 6a6200
          }

          @Override
          public ResourceLocation getStillTexture() {
            return STILL_TEXTURE;
          }

          @Override
          public ResourceLocation getFlowingTexture() {
            return FLOW_TEXURE;
          }

          @Override
          @NotNull
          public Vector3f modifyFogColor(Camera camera, float partialTick,
              ClientLevel level, int renderDistance, float darkenWorldAmount,
              Vector3f fluidFogColor) {
            var x = Integer.parseInt("6A", 16) / 255f;
            var y = Integer.parseInt("62", 16) / 255f;
            var z = Integer.parseInt("00", 16) / 255f;
            return new Vector3f(x, y, z);
          }

          @Override
          public void modifyFogRender(Camera camera, FogMode mode, float renderDistance,
              float partialTick, float nearDistance, float farDistance, FogShape shape) {
            RenderSystem.setShaderFogStart(0);
            RenderSystem.setShaderFogEnd(3f);
          }
        });
      }
    };
  }

  @Override
  public boolean canConvertToSource(FluidState state, Level level, BlockPos pos) {
    return false;
  }

  @Override
  protected void beforeDestroyingBlock(LevelAccessor level, BlockPos blockPos,
      BlockState blockState) {
    var blockEntity = blockState.hasBlockEntity() ? level.getBlockEntity(blockPos) : null;
    Block.dropResources(blockState, level, blockPos, blockEntity);
  }

  public static class Flowing extends CreosoteFluid {

    @Override
    protected void createFluidStateDefinition(
        StateDefinition.Builder<Fluid, FluidState> fluidState) {
      super.createFluidStateDefinition(fluidState);
      fluidState.add(LEVEL);
    }

    @Override
    public int getAmount(FluidState fluidState) {
      return fluidState.getValue(LEVEL);
    }

    @Override
    public boolean isSource(FluidState fluidState) {
      return false;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
      return false;
    }
  }

  public static class Source extends CreosoteFluid {

    @Override
    public int getAmount(FluidState fluidState) {
      return 8;
    }

    @Override
    public boolean isSource(FluidState fluidState) {
      return true;
    }

    @Override
    protected boolean canConvertToSource(Level level) {
      return true;
    }
  }
}
