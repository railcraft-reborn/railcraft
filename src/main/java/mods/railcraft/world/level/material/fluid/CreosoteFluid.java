package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.Item;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraftforge.fluids.FluidAttributes;

public class CreosoteFluid extends FlowingFluid {

  public Fluid getFlowing() {
    return Fluids.EMPTY;
  }

  public Fluid getSource() {
    return RailcraftFluids.CREOSOTE.get();
  }

  @Override
  public Item getBucket() {
    return RailcraftItems.CREOSOTE_BUCKET.get();
  }

  @Override
  protected boolean canBeReplacedWith(FluidState fluidState, BlockGetter reader,
      BlockPos blockPos, Fluid fluid, Direction direction) {
    return false;
  }

  @Override
  public Vec3 getFlow(BlockGetter reader, BlockPos blockPos,
      FluidState fluidState) {
    return Vec3.ZERO;
  }

  @Override
  public int getTickDelay(LevelReader worldReader) {
    return 5;
  }

  @Override
  protected float getExplosionResistance() {
    return 50.0f;
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
    return 8;
  }

  @Override
  public VoxelShape getShape(FluidState fluidState, BlockGetter blockReader,
      BlockPos blockPos) {
    return Shapes.empty();
  }

  @Override
  protected FluidAttributes createAttributes() {
    return FluidAttributes
        .builder(
            new ResourceLocation(Railcraft.ID, "block/steam_still"),
            new ResourceLocation(Railcraft.ID, "block/steam_still"))
        .color(0xFFCCA303)
        .temperature(320)
        .density(800)
        .viscosity(1500)
        .build(this);
  }

  @Override
  protected boolean canConvertToSource() {
    return false;
  }

  @Override
  protected void beforeDestroyingBlock(LevelAccessor world, BlockPos blockPos,
      BlockState blockState) {}

  @Override
  protected int getSlopeFindDistance(LevelReader world) {
    return 4;
  }

  @Override
  protected int getDropOff(LevelReader world) {
    return 1;
  }

}
