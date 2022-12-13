package mods.railcraft.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
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
import net.minecraftforge.event.level.BlockEvent;

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

  public static <T> Optional<T> getBlockEntity(BlockGetter getter, BlockPos pos,
      Class<T> blockEntityType) {
    return Optional.ofNullable(getter.getBlockEntity(pos))
        .filter(blockEntityType::isInstance)
        .map(blockEntityType::cast);
  }

  public static boolean setBlockState(Level level, BlockPos pos, BlockState blockState,
      @Nullable Player actor) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerLevel) level, pos);
    BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos);
    boolean result = level.setBlockAndUpdate(pos, blockState);
    if (ForgeEventFactory.onBlockPlace(actor, snapshot, Direction.UP)) {
      snapshot.restore(true, false);
      return false;
    }
    return result;
  }

  public static boolean setBlockStateWorldGen(Level level, BlockPos pos, BlockState blockState) {
    return level.setBlock(pos, blockState, Block.UPDATE_ALL);
  }

  public static boolean setBlockState(Level level, BlockPos pos, BlockState blockState,
      int update) {
    return level.setBlock(pos, blockState, update);
  }

  public static boolean setAir(Level level, BlockPos pos) {
    return level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
  }

  public static boolean destroyBlock(Level level, BlockPos pos) {
    return level.destroyBlock(pos, level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(Level level, BlockPos pos, @Nullable Player actor) {
    return destroyBlock(level, pos, actor,
        level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
  }

  public static boolean destroyBlock(Level level, BlockPos pos, @Nullable Player actor,
      boolean dropBlock) {
    if (actor == null)
      actor = RailcraftFakePlayer.get((ServerLevel) level, pos);

    if (MinecraftForge.EVENT_BUS.post(
        new BlockEvent.BreakEvent(level, pos, level.getBlockState(pos), actor)))
      return false;

    return level.destroyBlock(pos, dropBlock);
  }

  public static boolean playerRemoveBlock(Level level, BlockPos pos,
      @Nullable Player player) {
    return playerRemoveBlock(level, pos, player,
        level.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS));
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

  public static void sendBlockUpdated(Level level, BlockPos pos) {
    sendBlockUpdated(level, pos, level.getBlockState(pos));
  }

  public static void sendBlockUpdated(Level level, BlockPos pos, BlockState state) {
    sendBlockUpdated(level, pos, state, state);
  }

  public static void sendBlockUpdated(Level level, BlockPos pos, BlockState oldState,
      BlockState newState) {
    level.sendBlockUpdated(pos, oldState, newState, Block.UPDATE_LIMIT);
  }

  public static @Nullable BlockPos findBlock(Level level, BlockPos pos, int distance,
      Predicate<BlockState> matcher) {
    int x = pos.getX();
    int y = pos.getY();
    int z = pos.getZ();
    for (int yy = y - distance; yy < y + distance; yy++) {
      for (int xx = x - distance; xx < x + distance; xx++) {
        for (int zz = z - distance; zz < z + distance; zz++) {
          var test = new BlockPos(xx, yy, zz);
          if (matcher.test(level.getBlockState(test)))
            return test;
        }
      }
    }
    return null;
  }

  public static void spewItem(ItemStack stack, Level level, double x, double y, double z) {
    if (!stack.isEmpty()) {
      float xOffset = level.random.nextFloat() * 0.8F + 0.1F;
      float yOffset = level.random.nextFloat() * 0.8F + 0.1F;
      float zOffset = level.random.nextFloat() * 0.8F + 0.1F;
      while (!stack.isEmpty()) {
        int numToDrop = level.random.nextInt(21) + 10;
        if (numToDrop > stack.getCount())
          numToDrop = stack.getCount();
        ItemStack newStack = stack.copy();
        setSize(newStack, numToDrop);
        decSize(stack, numToDrop);
        var itemEntity = new ItemEntity(level, x + xOffset, y + yOffset, z + zOffset, newStack);
        level.addFreshEntity(itemEntity);
      }
    }
  }

  private static ItemStack setSize(ItemStack stack, int size) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.setCount(size);
    return stack;
  }

  private static ItemStack decSize(ItemStack stack, int size) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;
    stack.shrink(size);
    return stack;
  }
}
