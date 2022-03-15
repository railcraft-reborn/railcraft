package mods.railcraft.world.level.material.fluid;

import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraftforge.fluids.FluidAttributes;

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
        .setValue(LiquidBlock.LEVEL, Integer.valueOf(getLegacyLevel(fluidState)));
  }

  @Override
  public boolean isSame(Fluid fluid) {
    return fluid == RailcraftFluids.FLOWING_CREOSOTE.get()
        || fluid == RailcraftFluids.CREOSOTE.get();
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
  public Vec3 getFlow(BlockGetter blockGetter, BlockPos blockPos,
      FluidState fluidState) {
    return Vec3.ZERO;
  }

  @Override
  protected FluidAttributes createAttributes() {
    return FluidAttributes
        .builder(
            new ResourceLocation("block/water_still"), // vanilla textures, greyscale
            new ResourceLocation("block/water_flow"))
        .color(0xFF6A6200) // color is now ARGB 6a6200
        .density(1100)
        .viscosity(3000)
        .build(this);
  }

  @Override
  protected boolean canConvertToSource() {
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
  }
}
