package mods.railcraft.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.world.BlockEvent;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class LevelUtil {

  @Nullable
  public static BlockEntity getBlockEntityWeak(Level level, BlockPos pos) {
    return level.isLoaded(pos) ? level.getBlockEntity(pos) : null;
  }

  public static Optional<BlockEntity> getBlockEntity(Level level, BlockPos pos) {
    return Optional.ofNullable(level.getBlockEntity(pos));
  }

  public static <T> Optional<T> getBlockEntity(BlockGetter reader, BlockPos pos,
      Class<T> blockEntityType) {
    return getBlockEntity(reader, pos, blockEntityType, false);
  }

  public static <T> Optional<T> getBlockEntity(BlockGetter level, BlockPos pos,
      Class<T> blockEntityType, boolean weak) {
    if (!weak || !(level instanceof Level) || ((Level) level).isLoaded(pos)) {
      BlockEntity blockEntity = level.getBlockEntity(pos);
      if (blockEntityType.isInstance(blockEntity)) {
        return Optional.of(blockEntityType.cast(blockEntity));
      }
    }
    return Optional.empty();
  }

  public static boolean setBlockState(Level world, BlockPos pos, BlockState blockState,
      @Nullable Player actor) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerLevel) world, pos);
    BlockSnapshot snapshot = BlockSnapshot.create(world.dimension(), world, pos);
    boolean result = world.setBlockAndUpdate(pos, blockState);
    if (ForgeEventFactory.onBlockPlace(actor, snapshot, Direction.UP)) {
      snapshot.restore(true, false);
      return false;
    }
    return result;
  }

  public static boolean setBlockStateWorldGen(Level world, BlockPos pos, BlockState blockState) {
    return world.setBlock(pos, blockState, Block.UPDATE_ALL);
  }

  public static boolean setBlockState(Level world, BlockPos pos, BlockState blockState,
      int update) {
    return world.setBlock(pos, blockState, update);
  }

  public static boolean setAir(Level world, BlockPos pos) {
    return world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
  }

  public static boolean destroyBlock(Level world, BlockPos pos) {
    return world.destroyBlock(pos, world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(Level world, BlockPos pos, @Nullable Player actor) {
    return destroyBlock(world, pos, actor,
        world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(Level world, BlockPos pos, @Nullable Player actor,
      boolean dropBlock) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerLevel) world, pos);

    if (MinecraftForge.EVENT_BUS.post(
        new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), actor)))
      return false;

    return world.destroyBlock(pos, dropBlock);
  }

  public static boolean playerRemoveBlock(Level world, BlockPos pos,
      @Nullable Player player) {
    return playerRemoveBlock(world, pos, player,
        world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean playerRemoveBlock(Level level, BlockPos pos, @Nullable Player player,
      boolean dropBlock) {
    if (player == null) {
      player = RailcraftFakePlayer.get((ServerLevel) level, pos);
    }
    var blockState = level.getBlockState(pos);
    var blockEntity = level.getBlockEntity(pos);

    if (MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(level, pos, blockState, player)))
      return false;

    if (!blockState.onDestroyedByPlayer(level, pos, player, dropBlock, level.getFluidState(pos))) {
      return false;
    }

    if (dropBlock) {
      blockState.getBlock().playerDestroy(level, player, pos, blockState, blockEntity,
          player.getMainHandItem());
    }
    return true;
  }

  public static void neighborAction(BlockPos pos, Direction[] sides, Consumer<BlockPos> action) {
    for (var side : sides) {
      action.accept(pos.relative(side));
    }
  }

  public static void sendBlockUpdated(Level world, BlockPos pos) {
    sendBlockUpdated(world, pos, world.getBlockState(pos));
  }

  public static void sendBlockUpdated(Level world, BlockPos pos, BlockState state) {
    sendBlockUpdated(world, pos, state, state);
  }

  public static void sendBlockUpdated(Level world, BlockPos pos, BlockState oldState,
      BlockState newState) {
    world.sendBlockUpdated(pos, oldState, newState, Block.UPDATE_LIMIT);
  }

  public static @Nullable BlockPos findBlock(Level world, BlockPos pos, int distance,
      Predicate<BlockState> matcher) {
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    for (int yy = y - distance; yy < y + distance; yy++) {
      for (int xx = x - distance; xx < x + distance; xx++) {
        for (int zz = z - distance; zz < z + distance; zz++) {
          var test = new BlockPos(xx, yy, zz);
          if (matcher.test(world.getBlockState(test)))
            return test;
        }
      }
    }
    return null;
  }
}
