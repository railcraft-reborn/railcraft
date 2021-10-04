package mods.railcraft.world.level.block.post;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PostBlock extends Block {

  public static final EnumProperty<Column> COLUMN = EnumProperty.create("column", Column.class);
  public static final EnumProperty<Connection> NORTH =
      EnumProperty.create("north", Connection.class);
  public static final EnumProperty<Connection> SOUTH =
      EnumProperty.create("south", Connection.class);
  public static final EnumProperty<Connection> EAST = EnumProperty.create("east", Connection.class);
  public static final EnumProperty<Connection> WEST = EnumProperty.create("west", Connection.class);

  public PostBlock(Properties properties) {
    super(properties);
    properties.strength(3, 15);
    this.registerDefaultState(this.stateDefinition.any()
        .setValue(COLUMN, Column.NORMAL)
        .setValue(NORTH, Connection.NONE)
        .setValue(SOUTH, Connection.NONE)
        .setValue(EAST, Connection.NONE)
        .setValue(WEST, Connection.NONE));
    super.createBlockStateDefinition(null);
  }

  @Override
  protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
    builder.add(COLUMN, NORTH, SOUTH, EAST, WEST);
  }

  public Column getColumn(BlockState state, IBlockReader level, BlockPos pos) {
    if (Block.canSupportRigidBlock(level, pos.below()))
      return Column.NORMAL;
    BlockPos abovePos = pos.above();
    BlockState aboveState = level.getBlockState(abovePos);
    if (aboveState.getBlock() instanceof PostBlock)
      return Column.NORMAL;
    return Column.SHORT;
  }

  public boolean isPlatform(BlockState state) {
    return false;
  }

  // @Override
  // public @Nullable AxisAlignedBB getCollisionBoundingBox(IBlockState blockState,
  // IBlockAccess worldIn, BlockPos pos) {
  // if (isPlatform(blockState))
  // return FULL_BLOCK_AABB;
  // BlockPos down = pos.down();
  // IBlockState downState = WorldPlugin.getBlockState(worldIn, down);
  // if (!downState.getBlock().isAir(downState, worldIn, down)
  // && !(downState.getBlock() instanceof BlockPostBase))
  // return COLLISION_BOX_FENCE;
  // return COLLISION_BOX;
  // }

  @Override
  public boolean isPathfindable(BlockState state, IBlockReader level, BlockPos pos,
      PathType pathType) {
    return false;
  }

  @Override
  public boolean canCreatureSpawn(BlockState state, IBlockReader level, BlockPos pos,
      EntitySpawnPlacementRegistry.PlacementType placementType, EntityType<?> entityType) {
    return false;
  }
}
