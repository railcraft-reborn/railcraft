package mods.railcraft.plugins;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
public class LevelUtil {

  @Nullable
  public static TileEntity getBlockEntityWeak(World level, BlockPos pos) {
    return level.isLoaded(pos) ? level.getBlockEntity(pos) : null;
  }

  public static Optional<TileEntity> getBlockEntity(World level, BlockPos pos) {
    return Optional.ofNullable(level.getBlockEntity(pos));
  }

  public static <T> Optional<T> getBlockEntity(IBlockReader reader, BlockPos pos,
      Class<T> blockEntityType) {
    return getBlockEntity(reader, pos, blockEntityType, false);
  }

  public static <T> Optional<T> getBlockEntity(IBlockReader level, BlockPos pos,
      Class<T> blockEntityType, boolean weak) {
    if (!weak || !(level instanceof World) || ((World) level).isLoaded(pos)) {
      TileEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntityType.isInstance(blockEntity)) {
        return Optional.of(blockEntityType.cast(blockEntity));
      }
    }
    return Optional.empty();
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

  public static boolean setAir(World world, BlockPos pos) {
    return world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
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

    if (MinecraftForge.EVENT_BUS.post(
        new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), actor)))
      return false;

    return world.destroyBlock(pos, dropBlock);
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

  public static void sendBlockUpdated(World world, BlockPos pos) {
    sendBlockUpdated(world, pos, world.getBlockState(pos));
  }

  public static void sendBlockUpdated(World world, BlockPos pos, BlockState state) {
    sendBlockUpdated(world, pos, state, state);
  }

  public static void sendBlockUpdated(World world, BlockPos pos, BlockState oldState,
      BlockState newState) {
    world.sendBlockUpdated(pos, oldState, newState, Constants.BlockFlags.DEFAULT);
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
