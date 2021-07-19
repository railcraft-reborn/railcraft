package mods.railcraft.world.level.block;

import java.util.Collections;
import java.util.List;
import mods.railcraft.world.level.block.entity.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class ForceTrackBlock extends TrackBlock {

  public static final EnumProperty<RailShape> SHAPE =
      EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);

  public ForceTrackBlock(Properties properties) {
    super(TrackTypes.HIGH_SPEED, properties);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected BlockState updateDir(World worldIn, BlockPos pos, BlockState state,
      boolean initialPlacement) {
    return state; // Get the hell off my tile entity!
  }

  @Override
  public boolean canMakeSlopes(BlockState state, IBlockReader world, BlockPos pos) {
    return false;
  }

  @Override
  public boolean isFlexibleRail(BlockState state, IBlockReader world, BlockPos pos) {
    return false;
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return Collections.emptyList();
  }

  @Override
  public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn,
      BlockPos neighborPos, boolean something) {
    if (blockIn != this) {
      TileEntity tile = worldIn.getBlockEntity(pos);
      if (tile instanceof ForceTrackBlockEntity) {
        ((ForceTrackBlockEntity) tile).notifyEmitterForTrackChange();
      }
    }
  }

  @Override
  public float getRailMaxSpeed(BlockState state, World world, BlockPos pos,
      AbstractMinecartEntity cart) {
    return 0.6F;
  }

  @Override
  public boolean canBeReplacedByLeaves(BlockState state, IWorldReader world, BlockPos pos) {
    return true;
  }

  @Override
  public boolean canBeReplaced(BlockState state, BlockItemUseContext context) {
    return true;
  }

  @Override
  public void onRemove(BlockState state, World worldIn, BlockPos pos,
      BlockState newState, boolean something) {
    TileEntity tile = worldIn.getBlockEntity(pos);
    if (tile instanceof ForceTrackBlockEntity) {
      ((ForceTrackBlockEntity) tile).notifyEmitterForBreak();
    }
    super.onRemove(state, worldIn, pos, newState, something);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public TileEntity createTileEntity(BlockState state, IBlockReader world) {
    return new ForceTrackBlockEntity();
  }
}
