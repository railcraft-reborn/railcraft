package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public class GasFluid extends Fluid {

  @Override
  public Item getBucket() {
    return Items.AIR;
  }

  @Override
  protected boolean canBeReplacedWith(FluidState p_215665_1_, IBlockReader p_215665_2_,
      BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
    return false;
  }

  @Override
  protected Vector3d getFlow(IBlockReader p_215663_1_, BlockPos p_215663_2_,
      FluidState p_215663_3_) {
    return Vector3d.ZERO;
  }

  @Override
  public int getTickDelay(IWorldReader p_205569_1_) {
    return 0;
  }

  @Override
  protected float getExplosionResistance() {
    return 0;
  }

  @Override
  public float getHeight(FluidState p_215662_1_, IBlockReader p_215662_2_, BlockPos p_215662_3_) {
    return 0;
  }

  @Override
  public float getOwnHeight(FluidState p_223407_1_) {
    return 0;
  }

  @Override
  protected BlockState createLegacyBlock(FluidState p_204527_1_) {
    return Blocks.AIR.defaultBlockState();
  }

  @Override
  public boolean isSource(FluidState p_207193_1_) {
    return true;
  }

  @Override
  public int getAmount(FluidState p_207192_1_) {
    return 0;
  }

  @Override
  public VoxelShape getShape(FluidState p_215664_1_, IBlockReader p_215664_2_,
      BlockPos blockPos) {
    return VoxelShapes.empty();
  }

  @Override
  protected FluidAttributes createAttributes() {
    return FluidAttributes
        .builder(new ResourceLocation(Railcraft.ID, "block/steam_still.png"),
            new ResourceLocation(Railcraft.ID, "block/steam_still.png"))
        .color(0x808080)
        .gaseous()
        .build(this);
  }
}
