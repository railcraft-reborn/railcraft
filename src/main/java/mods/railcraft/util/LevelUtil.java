package mods.railcraft.util;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.core.RailcraftFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
      @Nullable Entity entity) {
    if (entity == null)
      entity = RailcraftFakePlayer.get((ServerLevel) level, pos);
    BlockSnapshot snapshot = BlockSnapshot.create(level.dimension(), level, pos);
    boolean result = level.setBlockAndUpdate(pos, blockState);
    if (ForgeEventFactory.onBlockPlace(entity, snapshot, Direction.UP)) {
      snapshot.restore(true, false);
      return false;
    }
    return result;
  }

  public static boolean setBlockStateWorldGen(Level level, BlockPos pos, BlockState blockState) {
    return level.setBlock(pos, blockState, Block.UPDATE_ALL);
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

  public static void spewItem(ItemStack stack, Level level, double x, double y, double z) {
    if (stack.isEmpty()) {
      return;
    }
    float xOffset = level.random.nextFloat() * 0.8F + 0.1F;
    float yOffset = level.random.nextFloat() * 0.8F + 0.1F;
    float zOffset = level.random.nextFloat() * 0.8F + 0.1F;
    while (!stack.isEmpty()) {
      int numToDrop = Math.min(level.random.nextInt(21) + 10, stack.getCount());
      var newStack = stack.split(numToDrop);
      var itemEntity = new ItemEntity(level, x + xOffset, y + yOffset, z + zOffset, newStack);
      level.addFreshEntity(itemEntity);
    }
  }
}
