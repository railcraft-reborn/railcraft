package mods.railcraft.plugins;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class WorldPlugin {

  public static @Nullable TileEntity getBlockTileWeak(World world, BlockPos pos) {
    return world.isLoaded(pos) ? world.getBlockEntity(pos) : null;
  }

  public static Optional<TileEntity> getTileEntity(IBlockReader world, BlockPos pos) {
    return Optional.ofNullable(world.getBlockEntity(pos));
  }

  public static <T> Optional<T> getTileEntity(@Nullable IBlockReader world, @Nullable BlockPos pos,
      Class<T> tileClass) {
    return getTileEntity(world, pos, tileClass, false);
  }

  public static <T> Optional<T> getTileEntity(@Nullable IBlockReader world, @Nullable BlockPos pos,
      Class<T> tileClass, boolean checkLoaded) {
    if (world == null || pos == null)
      return Optional.empty();
    if (!checkLoaded || !(world instanceof World) || ((World) world).isLoaded(pos)) {
      TileEntity tileEntity = world.getBlockEntity(pos);
      if (tileClass.isInstance(tileEntity))
        return Optional.of(tileClass.cast(tileEntity));
    }
    return Optional.empty();
  }

  public static <T, V> Optional<V> retrieveFromTile(IBlockReader world, BlockPos pos,
      Class<T> tileClass, Function<T, V> function) {
    TileEntity tileEntity = world.getBlockEntity(pos);
    if (tileClass.isInstance(tileEntity))
      return Optional.of(tileClass.cast(tileEntity)).map(function);
    return Optional.empty();
  }

  public static <T> void doForTile(IBlockReader world, BlockPos pos, Class<T> tileClass,
      Consumer<T> consumer) {
    TileEntity tileEntity = world.getBlockEntity(pos);
    if (tileClass.isInstance(tileEntity))
      consumer.accept(tileClass.cast(tileEntity));
  }

  public static Material getBlockMaterial(IBlockReader world, BlockPos pos) {
    return world.getBlockState(pos).getMaterial();
  }

  @SuppressWarnings("deprecation")
  public static boolean isAreaLoaded(World world, BlockPos pos1, BlockPos pos2) {
    return world.hasChunksAt(pos1, pos2);
  }

  public static boolean isAir(IBlockReader world, BlockPos pos) {
    return isAir(world, pos, world.getBlockState(pos));
  }

  public static boolean isAir(IBlockReader world, BlockPos pos, BlockState state) {
    return state.getBlock().isAir(state, world, pos);
  }

  public static boolean isBlockAt(IBlockReader world, BlockPos pos, @Nullable Block block) {
    return block != null && block == world.getBlockState(pos).getBlock();
  }

  public static boolean isBlockAt(IBlockReader world, BlockPos pos,
      Class<? extends Block> blockClass) {
    return blockClass.isInstance(world.getBlockState(pos).getBlock());
  }

  public static boolean isMaterialAt(IBlockReader world, BlockPos pos, Material material) {
    return world.getBlockState(pos).getMaterial() == material;
  }

  public static boolean setBlockState(World world, BlockPos pos, BlockState blockState,
      @Nullable PlayerEntity actor) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerWorld) world, pos);
    BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, pos);
    boolean result = world.setBlockAndUpdate(pos, blockState);
    if (ForgeEventFactory.onBlockPlace(actor, snapshot, Direction.UP)) {
      snapshot.restore(true, false);
      return false;
    }
    return result;
  }

  public static boolean setBlockStateWorldGen(World world, BlockPos pos, BlockState blockState) {
    return world.setBlock(pos, blockState,
        Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.UPDATE_NEIGHBORS);
  }

  public static boolean setBlockState(World world, BlockPos pos, BlockState blockState,
      int update) {
    return world.setBlock(pos, blockState, update);
  }

  public static boolean setBlockToAir(World world, BlockPos pos) {
    return world.setBlock(pos, Blocks.AIR.defaultBlockState(),
        Constants.BlockFlags.DEFAULT_AND_RERENDER);
  }

  public static boolean destroyBlock(World world, BlockPos pos, boolean dropBlock) {
    return world.destroyBlock(pos, dropBlock);
  }

  public static boolean destroyBlock(World world, BlockPos pos) {
    return world.destroyBlock(pos, world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(World world, BlockPos pos, @Nullable PlayerEntity actor) {
    return destroyBlock(world, pos, actor,
        world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(World world, BlockPos pos, @Nullable PlayerEntity actor,
      boolean dropBlock) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerWorld) world, pos);

    // Start of Event Fire
    if (MinecraftForge.EVENT_BUS.post(
        new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), actor)))
      return false;
    // End of Event Fire

    return destroyBlock(world, pos, dropBlock);
  }

  public static boolean playerRemoveBlock(World world, BlockPos pos,
      @Nullable PlayerEntity player) {
    return playerRemoveBlock(world, pos, player,
        world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean playerRemoveBlock(World world, BlockPos pos, @Nullable PlayerEntity player,
      boolean dropBlock) {
    if (player == null)
      player = RailcraftFakePlayer.get((ServerWorld) world, pos);
    BlockState state = world.getBlockState(pos);
    TileEntity te = world.getBlockEntity(pos);

    if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, state, player)))
      return false;

    if (!state.getBlock().removedByPlayer(state, world, pos, player, dropBlock,
        world.getFluidState(pos)))
      return false;

    if (dropBlock) {
      state.getBlock().playerDestroy(world, player, pos, state, te, player.getMainHandItem());
    }
    return true;
  }

  public static void neighborAction(BlockPos pos, Direction[] sides, Consumer<BlockPos> action) {
    for (Direction side : sides) {
      action.accept(pos.relative(side));
    }
  }

  public static void notifyBlockOfStateChange(World world, BlockPos pos, Block block) {
    if (world != null && block != null)
      world.updateNeighborsAt(pos, block);
  }

  public static void notifyBlocksOfNeighborChange(World world, BlockPos pos, Block block) {
    if (world != null && block != null)
      world.updateNeighborsAt(pos, block);
  }

  public static void notifyBlocksOfNeighborChangeOnSide(World world, BlockPos pos, Block block,
      Direction side) {
    world.updateNeighborsAt(pos.relative(side), block);
  }

  public static void markBlockForUpdate(World world, BlockPos pos) {
    markBlockForUpdate(world, pos, world.getBlockState(pos));
  }

  public static void markBlockForUpdate(World world, BlockPos pos, BlockState state) {
    markBlockForUpdate(world, pos, state, state);
  }

  public static void markBlockForUpdate(World world, BlockPos pos, BlockState oldState,
      BlockState newState) {
    world.sendBlockUpdated(pos, oldState, newState, 3);
  }

  public static void addBlockEvent(World world, BlockPos pos, Block block, int key, int value) {
    if (world != null && block != null)
      world.blockEvent(pos, block, key, value);
  }

  public static @Nullable BlockPos findBlock(World world, BlockPos pos, int distance,
      Predicate<BlockState> matcher) {
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    for (int yy = y - distance; yy < y + distance; yy++) {
      for (int xx = x - distance; xx < x + distance; xx++) {
        for (int zz = z - distance; zz < z + distance; zz++) {
          BlockPos test = new BlockPos(xx, yy, zz);
          if (matcher.test(world.getBlockState(test)))
            return test;
        }
      }
    }
    return null;
  }

}
