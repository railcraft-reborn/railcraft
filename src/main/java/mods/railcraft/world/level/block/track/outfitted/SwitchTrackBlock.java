package mods.railcraft.world.level.block.track.outfitted;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.entity.track.SwitchTrackBlockEntity;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public abstract class SwitchTrackBlock extends ReversibleOutfittedTrackBlock {

  public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");
  public static final EnumProperty<RailShape> SHAPE =
      EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);

  public SwitchTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(this.getShapeProperty(), RailShape.NORTH_SOUTH)
        .setValue(REVERSED, false)
        .setValue(SWITCHED, false));
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(SWITCHED);
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
  public void onPlace(BlockState state, World level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    // this.updateAdjacentBlocks(state, level, pos);
  }

  protected void updateAdjacentBlocks(BlockState blockState, World level, BlockPos pos) {
    final RailShape railShape = TrackTools.getRailShapeRaw(blockState);
    if (railShape == RailShape.NORTH_SOUTH) {
      BlockPos offset =
          AbstractRailBlock.isRail(level, pos.west()) ? pos.west() : pos.east();
      if (AbstractRailBlock.isRail(level, offset)) {
        RailShape otherDir = TrackTools.getTrackDirection(level, offset);
        if (otherDir == RailShape.NORTH_SOUTH) {
          TrackTools.setRailShape(level, offset, RailShape.EAST_WEST);
        }
      }
    }
  }

  public static boolean isSwitched(BlockState blockState) {
    return blockState.getValue(SWITCHED);
  }
}
