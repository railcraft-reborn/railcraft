package mods.railcraft.world.level.block;

import java.util.function.Supplier;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.plugins.WorldPlugin;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RailBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.Direction;

/**
 * Created by CovertJaguar on 8/2/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class AbandonedFlexTrackBlock extends TrackBlock {

  public static final BooleanProperty GRASS = BooleanProperty.create("grass");

  public AbandonedFlexTrackBlock(Supplier<? extends TrackType> trackType, Properties properties) {
    super(trackType, properties);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(GRASS, false)
        .setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH));
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(GRASS);
  }

  @Override
  public BlockState getStateForPlacement(BlockItemUseContext context) {
    boolean grass = Direction.Plane.HORIZONTAL.stream()
        .anyMatch(s -> WorldPlugin.isBlockAt(context.getLevel(),
            context.getClickedPos().relative(s), Blocks.TALL_GRASS));
    return super.getStateForPlacement(context).setValue(GRASS, grass);
  }
}
