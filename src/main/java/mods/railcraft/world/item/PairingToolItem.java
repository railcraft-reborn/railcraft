package mods.railcraft.world.item;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import mods.railcraft.api.item.ActivationBlockingItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;

@ActivationBlockingItem
public abstract class PairingToolItem<T, P> extends Item {

  private static final Logger logger = LogUtils.getLogger();

  public PairingToolItem(Properties properties) {
    super(properties);
  }

  protected abstract Class<T> targetType();

  protected abstract Class<P> peerType();

  @Override
  public final InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var level = context.getLevel();
    if (!(level instanceof ServerLevel serverLevel)) {
      return InteractionResult.SUCCESS;
    }

    var player = context.getPlayer();
    var pos = context.getClickedPos();
    var targetPos = getTargetPos(itemStack).orElse(null);

    if (targetPos != null) {
      // Clicked on target twice
      if (targetPos.pos().equals(pos)) {
        this.displayMessageForState(player, State.ABANDONED);
        return InteractionResult.CONSUME;
      }
      // Target dimension is different to current dimension so silently start new pairing
      if (targetPos.dimension().compareTo(level.dimension()) != 0) {
        var dimension = level.getServer().getLevel(targetPos.dimension());
        if (dimension != null) {
          this.abandon(getBlockEntity(dimension, targetPos.pos(), this.targetType()));
        }
        clearTargetPos(itemStack);
        targetPos = null;
      }
    }

    // No target has been set yet
    if (targetPos == null) {
      var target = getBlockEntity(level, pos, this.targetType());
      if (target == null) {
        this.displayMessageForState(player, State.INVALID_TARGET);
        return InteractionResult.FAIL;
      }
      var result = this.begin(target);
      player.displayClientMessage(result.message(), true);
      if (result.success()) {
        setTargetPos(itemStack, GlobalPos.of(level.dimension(), pos));
        return InteractionResult.CONSUME;
      } else {
        return InteractionResult.FAIL;
      }
    }

    var targetEntity = getBlockEntity(level, targetPos.pos(), this.targetType());

    if (player.isShiftKeyDown()) {
      this.abandon(targetEntity);
      this.displayMessageForState(player, State.ABANDONED);
      clearTargetPos(itemStack);
      return InteractionResult.CONSUME;
    }

    // Target no longer exists
    if (targetEntity == null) {
      this.displayMessageForState(player, State.LOST_TARGET);
      clearTargetPos(itemStack);
      return InteractionResult.FAIL;
    }

    var peerEntity = getBlockEntity(level, pos, this.peerType());
    if (peerEntity == null) {
      this.displayMessageForState(player, State.INVALID_PEER);
      return InteractionResult.FAIL;
    }

    if (targetEntity == peerEntity) {
      throw new IllegalStateException("Target entity is the same as the peer entity.");
    }

    var result = this.complete(targetEntity, peerEntity);
    player.displayClientMessage(result.message(), true);
    clearTargetPos(itemStack);
    return result.success() ? InteractionResult.CONSUME : InteractionResult.FAIL;
  }

  private void displayMessageForState(Player player, State state) {
    player.displayClientMessage(this.getMessageForState(state), true);
  }

  /**
   * {@return the message for the specified {@link State}}
   *
   * @param state - the state to retrieve the message for
   */
  protected abstract Component getMessageForState(State state);

  /**
   * Abandons pairing with the specified {@link BlockEntity}.
   *
   * @param target - the {@link BlockEntity} to cancel pairing for
   */
  protected abstract void abandon(@Nullable T target);

  /**
   * Begins pairing with the specified {@link BlockEntity}.
   *
   * @param target - the {@link BlockEntity} to begin pairing with
   * @return the result of the operation
   */
  protected abstract Result begin(T target);

  /**
   * Completes pairing.
   *
   * @param target - the target {@link BlockEntity}
   * @param peer - the peer {@link BlockEntity}
   * @return the result of the operation
   */
  protected abstract Result complete(T target, P peer);

  protected enum State {

    ABANDONED, LOST_TARGET, INVALID_TARGET, INVALID_PEER;
  }

  protected record Result(boolean success, Component message) {

    public static Result success(Component message) {
      return new Result(true, message);
    }

    public static Result failure(Component message) {
      return new Result(false, message);
    }
  }

  private static <T> T getBlockEntity(BlockGetter getter, BlockPos pos, Class<T> type) {
    var entity = getter.getBlockEntity(pos);
    return type.isInstance(entity) ? type.cast(entity) : null;
  }

  public static Optional<GlobalPos> getTargetPos(ItemStack itemStack) {
    var tag = itemStack.getTag();
    return tag != null && tag.contains("peerPos", Tag.TAG_COMPOUND)
        ? GlobalPos.CODEC
            .parse(NbtOps.INSTANCE, tag.getCompound("peerPos"))
            .resultOrPartial(logger::error)
        : Optional.empty();
  }

  public static void setTargetPos(ItemStack itemStack, GlobalPos peerPos) {
    GlobalPos.CODEC
        .encodeStart(NbtOps.INSTANCE, peerPos)
        .resultOrPartial(logger::error)
        .ifPresent(tag -> itemStack.getOrCreateTag().put("peerPos", tag));
  }

  public static void clearTargetPos(ItemStack itemStack) {
    var tag = itemStack.getTag();
    if (tag != null) {
      tag.remove("peerPos");
    }
  }

  public static <T> boolean checkAbandonPairing(GlobalPos signalPos, Player player,
      ServerLevel level, Runnable stopLinking) {
    if (signalPos.dimension().compareTo(level.dimension()) != 0) {
      return true;
    }
    if (player.isShiftKeyDown()) {
      stopLinking.run();
      return true;
    }
    return false;
  }
}
