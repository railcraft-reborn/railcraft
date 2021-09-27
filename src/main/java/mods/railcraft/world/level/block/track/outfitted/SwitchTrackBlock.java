package mods.railcraft.world.level.block.track.outfitted;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.entity.track.SwitchTrackBlockEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class SwitchTrackBlock extends OutfittedTrackBlock {

  public static final BooleanProperty MIRRORED = BooleanProperty.create("mirrored");
  public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");

  public SwitchTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(MIRRORED, false)
        .setValue(SWITCHED, false));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(MIRRORED, SWITCHED);
  }

  @Override
  public boolean hasTileEntity(BlockState state) {
    return true;
  }

  @Override
  public abstract SwitchTrackBlockEntity createTileEntity(BlockState state, IBlockReader reader);

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return Collections.emptyList();
  }

  @Override
  public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    this.determineRailDirection(state, worldIn, pos);
    this.determineMirror(state, worldIn, pos, false);
  }

  protected void determineRailDirection(BlockState state, World world, BlockPos pos) {
    RailShape dir = TrackTools.getTrackDirectionRaw(state);
    if (AbstractRailBlock.isRail(world, pos.east())
        && AbstractRailBlock.isRail(world, pos.west())) {
      if (dir != RailShape.EAST_WEST)
        TrackTools.setTrackDirection(world, pos, RailShape.EAST_WEST);
      // } else if (TrackTools.isRailBlockAt(world, pos.north()) &&
      // TrackTools.isRailBlockAt(world, pos.south())) {
      // if (dir != RailShape.NORTH_SOUTH)
      // TrackTools.setTrackDirection(world, pos, RailShape.NORTH_SOUTH);
    } else if (dir != RailShape.NORTH_SOUTH)
      TrackTools.setTrackDirection(world, pos, RailShape.NORTH_SOUTH);
  }

  protected void determineMirror(BlockState state, World world, BlockPos pos,
      boolean updateClient) {
    RailShape dir = TrackTools.getTrackDirection(world, pos);
    boolean wasMirrored = isMirrored(state);
    boolean mirrored = wasMirrored;
    if (dir == RailShape.NORTH_SOUTH) {
      BlockPos offset = pos;
      if (AbstractRailBlock.isRail(world, offset.west())) {
        offset = offset.west();
        mirrored = true; // West
      } else {
        offset = offset.east();
        mirrored = false; // East
      }
      if (AbstractRailBlock.isRail(world, offset)) {
        RailShape otherDir = TrackTools.getTrackDirection(world, offset);
        if (otherDir == RailShape.NORTH_SOUTH)
          TrackTools.setTrackDirection(world, offset, RailShape.EAST_WEST);
      }
    } else if (dir == RailShape.EAST_WEST)
      mirrored = AbstractRailBlock.isRail(world, pos.north());

    if (updateClient && wasMirrored != mirrored)
      world.setBlockAndUpdate(pos, state.setValue(SwitchTrackBlock.MIRRORED, mirrored));
  }

  @Override
  public void neighborChanged(BlockState state, World world, BlockPos pos, Block neighborBlock,
      BlockPos neighborPos, boolean moving) {
    if (!world.isClientSide()) {
      this.determineRailDirection(state, world, pos);
      this.determineMirror(state, world, pos, true);
    }
    super.neighborChanged(state, world, pos, neighborBlock, neighborPos, moving);
  }

  public static boolean isMirrored(BlockState blockState) {
    return blockState.getValue(SwitchTrackBlock.MIRRORED);
  }

  public static boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SwitchTrackBlock.SWITCHED);
  }
}
