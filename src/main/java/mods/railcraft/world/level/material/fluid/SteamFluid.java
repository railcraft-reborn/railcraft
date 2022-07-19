package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.IFluidTypeRenderProperties;
import net.minecraftforge.fluids.FluidType;

import java.util.function.Consumer;

public class SteamFluid extends Fluid {

  @Override
  public Item getBucket() {
    return Items.AIR;
  }

  @Override
  protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter reader,
      BlockPos blockPos, Fluid fluid, Direction direction) {
    return false;
  }

  @Override
  protected Vec3 getFlow(BlockGetter reader, BlockPos blockPos,
      FluidState fluidState) {
    return Vec3.ZERO;
  }

  @Override
  public int getTickDelay(LevelReader worldReader) {
    return 0;
  }

  @Override
  protected float getExplosionResistance() {
    return 0;
  }

  @Override
  public float getHeight(FluidState fluidState, BlockGetter blockReader, BlockPos blockPos) {
    return 0;
  }

  @Override
  public float getOwnHeight(FluidState fluidState) {
    return 0;
  }

  @Override
  protected BlockState createLegacyBlock(FluidState fluidState) {
    return Blocks.AIR.defaultBlockState();
  }

  @Override
  public boolean isSource(FluidState fluidState) {
    return true;
  }

  @Override
  public int getAmount(FluidState fluidState) {
    return 0;
  }

  @Override
  public VoxelShape getShape(FluidState fluidState, BlockGetter blockReader,
      BlockPos blockPos) {
    return Shapes.empty();
  }

  @Override
  public FluidType getFluidType() {
    return RailcraftFluids.STEAM_TYPE.get();
  }

  public static FluidType createFluidType() {
    var properties = FluidType.Properties.create()
      .temperature(400) // in kelvin, 150c
      .density(-1000)
      .viscosity(500);
    return new FluidType(properties) {
      @Override
      public void initializeClient(Consumer<IFluidTypeRenderProperties> consumer) {
        consumer.accept(new IFluidTypeRenderProperties() {
          private static final ResourceLocation STILL_TEXTURE =
            new ResourceLocation(Railcraft.ID, "block/steam_still");
          @Override
          public int getColorTint() {
            return 0xFFF5F5F5;
          }

          @Override
          public ResourceLocation getStillTexture() {
            return STILL_TEXTURE;
          }

          @Override
          public ResourceLocation getFlowingTexture() {
            return STILL_TEXTURE;
          }
        });
      }
    };
  }
}
