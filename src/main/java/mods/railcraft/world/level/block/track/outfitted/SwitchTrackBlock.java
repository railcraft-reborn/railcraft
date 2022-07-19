package mods.railcraft.world.level.block.track.outfitted;

import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackTools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.storage.loot.LootContext;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public abstract class SwitchTrackBlock extends ReversibleOutfittedTrackBlock {

  public static final BooleanProperty SWITCHED = BooleanProperty.create("switched");
  public static final EnumProperty<RailShape> SHAPE =
      EnumProperty.create("shape", RailShape.class, RailShape.NORTH_SOUTH, RailShape.EAST_WEST);

  public SwitchTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
  }

  @Override
  protected BlockState buildDefaultState(BlockState blockState) {
    return super.buildDefaultState(blockState)
        .setValue(SWITCHED, false);
  }

  @Override
  public Property<RailShape> getShapeProperty() {
    return SHAPE;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(SWITCHED);
  }

  @Override
  public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    return Collections.emptyList();
  }

  @Override
  public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldBlockState,
      boolean moved) {
    // this.updateAdjacentBlocks(state, level, pos);
  }

  protected void updateAdjacentBlocks(BlockState blockState, Level level, BlockPos pos) {
    final RailShape railShape = TrackTools.getRailShapeRaw(blockState);
    if (railShape == RailShape.NORTH_SOUTH) {
      BlockPos offset =
          BaseRailBlock.isRail(level, pos.west()) ? pos.west() : pos.east();
      if (BaseRailBlock.isRail(level, offset)) {
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
