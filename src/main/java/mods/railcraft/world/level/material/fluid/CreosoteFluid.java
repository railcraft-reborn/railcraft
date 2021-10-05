package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
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
  protected boolean canBeReplacedWith(FluidState fluidState, IBlockReader reader,
      BlockPos blockPos, Fluid fluid, Direction direction) {
    return false;
  }

  @Override
  public Vector3d getFlow(IBlockReader reader, BlockPos blockPos,
      FluidState fluidState) {
    return Vector3d.ZERO;
  }

  @Override
  public int getTickDelay(IWorldReader worldReader) {
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
  public VoxelShape getShape(FluidState fluidState, IBlockReader blockReader,
      BlockPos blockPos) {
    return VoxelShapes.empty();
  }

  @Override
  protected FluidAttributes createAttributes() {
    return FluidAttributes
        .builder(
            new ResourceLocation(Railcraft.ID, "block/steam_still"),
            new ResourceLocation(Railcraft.ID, "block/steam_still"))
        .color(0xFFF5F5F5) // color is now ARGB
        // .temperature(423) // TODO creosote temp, steal from imer engi.
        .build(this);
  }

  @Override
  protected boolean canConvertToSource() {
    return false;
  }

  @Override
  protected void beforeDestroyingBlock(IWorld world, BlockPos blockPos, BlockState blockState) {}

  @Override
  protected int getSlopeFindDistance(IWorldReader world) {
    return 4;
  }

  @Override
  protected int getDropOff(IWorldReader world) {
    return 1;
  }

}
