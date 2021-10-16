package mods.railcraft.world.level.block.entity.track;

import java.util.UUID;
import javax.annotation.Nullable;
import mods.railcraft.api.event.CartLockdownEvent;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.api.track.LockingTrack;
import mods.railcraft.util.EntitySearcher;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.entity.cart.CartTools;
import mods.railcraft.world.entity.cart.Train;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.behaivor.HighSpeedTools;
import mods.railcraft.world.level.block.track.outfitted.LockingMode;
import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;

public class LockingTrackBlockEntity extends RailcraftTickableBlockEntity
    implements LockingTrack {

  public static final double START_BOOST = 0.04;
  public static final double BOOST_FACTOR = 0.06;

  private UUID id = UUID.randomUUID();

  private LockingModeController lockingModeController;

  @Nullable
  protected AbstractMinecartEntity currentCart;
  @Nullable
  protected AbstractMinecartEntity prevCart;
  // Temporary variables to hold loaded data while we restore from NBT
  @Nullable
  private UUID prevCartId;
  @Nullable
  private UUID currentCartId;

  @Nullable
  protected Train currentTrain;
  protected boolean trainLeaving;
  protected boolean locked;
  private int trainDelay;

  public LockingTrackBlockEntity() {
    super(RailcraftBlockEntityTypes.LOCKING_TRACK.get());
  }

  public LockingTrackBlockEntity(LockingMode lockingMode) {
    super(RailcraftBlockEntityTypes.LOCKING_TRACK.get());
    this.lockingModeController = lockingMode.create(this);
  }

  public void setLockingMode(LockingMode lockingMode) {
    this.level.setBlockAndUpdate(this.getBlockPos(),
        this.getBlockState().setValue(LockingTrackBlock.LOCKING_MODE, lockingMode));
    this.lockingModeController = lockingMode.create(this);
  }

  @Override
  protected void load() {
    this.prevCart = CartTools.getCartFromUUID(this.level, this.prevCartId);
    this.currentCart = CartTools.getCartFromUUID(this.level, this.currentCartId);
  }

  @Override
  public void tick() {
    if (!this.level.isClientSide()) {
      // flag determines whether we send an update to the client, only update when visible changes
      // occur
      boolean updateClient = false;

      if (this.currentCart != null && !this.currentCart.isAlive()) {
        this.releaseCurrentCart();
        this.currentCart = null;
        updateClient = true;
      }

      boolean lastLocked = this.locked;
      this.calculateLocked();
      if (lastLocked != this.locked) {
        updateClient = true;
      }

      if (this.locked) {
        this.lockCurrentCart();
      } else {
        this.releaseCurrentCart();
      }

      // Store our last found cart in prevCart
      if (this.currentCart != null) {
        this.prevCart = this.currentCart;
      }
      // reset currentCart so we know if onMinecartPass() actually found one
      this.currentCart = null;

      if (updateClient) {
        this.syncToClient();
      }
    }
  }

  // Called by block
  public ActionResultType use(PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!itemStack.isEmpty() && itemStack.getItem() instanceof Crowbar) {
      Crowbar crowbar = (Crowbar) itemStack.getItem();
      if (crowbar.canWhack(player, hand, itemStack, this.getBlockPos())) {
        final LockingMode lockingMode = LockingTrackBlock.getLockingMode(this.getBlockState());
        LockingMode newLockingMode =
            player.isCrouching() ? lockingMode.previous() : lockingMode.next();
        crowbar.onWhack(player, hand, itemStack, this.getBlockPos());
        if (!this.level.isClientSide()) {
          this.setLockingMode(newLockingMode);
          player.displayClientMessage(
              new TranslationTextComponent("locking_track.mode",
                  newLockingMode.getDisplayName().copy().withStyle(TextFormatting.DARK_PURPLE)),
              true);
        }
        return ActionResultType.sidedSuccess(this.level.isClientSide());
      }
    }
    return ActionResultType.PASS;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.releaseCart(); // Release any carts still holding on
  }

  public UUID getId() {
    return this.id;
  }

  private void lockCurrentCart() {
    if (this.currentCart != null) {
      HighSpeedTools.performHighSpeedChecks(this.level, this.getBlockPos(), this.currentCart);
      Train train = Train.get(this.currentCart).orElse(null);
      if (this.currentTrain != train && this.currentTrain != null) {
        this.currentTrain.removeLock(this.id);
      }
      this.currentTrain = train;
      if (this.currentTrain != null) {
        this.currentTrain.addLock(this.id);
      }
      MinecraftForge.EVENT_BUS.post(
          new CartLockdownEvent.Lock(this.currentCart, this.getBlockPos()));
      this.lockingModeController.locked(this.currentCart);
      this.currentCart.setDeltaMovement(0.0D, this.currentCart.getDeltaMovement().y(), 0.0D);
      RailShape railShape = TrackBlock.getRailShapeRaw(this.getBlockState());
      if (TrackShapeHelper.isNorthSouth(railShape)) {
        this.currentCart.setPos(this.currentCart.getX(), this.currentCart.getY(),
            this.getBlockPos().getZ() + 0.5D);
      } else {
        this.currentCart.setPos(this.getBlockPos().getX() + 0.5D, this.currentCart.getY(),
            this.currentCart.getZ());
      }
    }
  }

  // Called by block
  public void minecartPassed(AbstractMinecartEntity cart) {
    this.currentCart = cart;
    this.lockingModeController.passed(this.currentCart);
  }

  private void releaseCurrentCart() {
    if (this.currentTrain != null) {
      this.currentTrain.removeLock(this.id);
    }
    if (this.currentCart != null) {
      MinecraftForge.EVENT_BUS
          .post(new CartLockdownEvent.Release(this.currentCart, this.getBlockPos()));
      this.lockingModeController.released(this.currentCart);
    }
  }

  @Override
  public void releaseCart() {
    this.trainLeaving = true;
  }

  @Override
  public boolean isCartLocked(AbstractMinecartEntity cart) {
    return this.locked && this.prevCart == cart;
  }

  /**
   * Determines if the current train is the same train or cart (depending on track type) as the
   * train or cart in previous ticks. The {@code trainDelay} is needed because there are gaps
   * between carts in a train where onMinecartPass() doesn't get called even though the train is
   * still passing over us.
   *
   * @return whether the current cart or train (depending on LockType) is the same as previous cart
   *         or trains
   */
  private boolean isSameTrainOrCart() {
    final LockingMode lockingMode = LockingTrackBlock.getLockingMode(this.getBlockState());
    if (lockingMode.getLockType().isTrain()) {
      if (this.currentCart != null) {
        if (Train.areInSameTrain(this.currentCart, this.prevCart)) {
          // reset trainDelay
          this.trainDelay = TrackTools.TRAIN_LOCKDOWN_DELAY;
        } else {
          // We've encountered a new train, force the delay to 0 so we return false
          this.trainDelay = 0;
        }
      } else if (this.trainLeaving) {
        if (EntitySearcher.findMinecarts()
            .around(this.getBlockPos())
            .in(this.level)
            .stream()
            .anyMatch(cart -> Train.areInSameTrain(cart, this.prevCart))) {
          this.trainDelay = TrackTools.TRAIN_LOCKDOWN_DELAY;
        }
      }

      if (this.trainDelay > 0) {
        this.trainDelay--;
      } else {
        this.prevCart = null;
      }
      return this.trainDelay > 0;
    } else {
      return this.currentCart != null &&
          (lockingMode.getLockType().isCart() && this.currentCart == this.prevCart);
    }
  }

  /**
   * The heart of the logic for this class is done here. If you understand what's going on here, the
   * rest will make much more sense to you. Basically, we're trying to determine whether this track
   * should be trying to lock the current or next cart that passes over it. First of all we must
   * realize that we only have 2 inputs: 1) whether a train/cart is passing over us and 2) whether
   * our track is receiving a redstone signal. If we try to create a truth table with 2 boolean
   * inputs to calculate "locked", we find that we can't quite express the correct value for
   * "locked". When we analyze the situation, we notice that when a train is passing over the track,
   * we need both the redstone to be off and the last cart to be off the track in order to lock the
   * track. However after the train has already left the track, then we want the track to be
   * "locked" when the redstone is off, regardless of whether a new or old cart starts moving onto
   * the track. In the end, what we're really after is having 2 truth tables and a way to decide
   * which of the 2 tables to use. To do this, we use the boolean {@code trainLeaving} to indicate
   * which table to use. As the name implies, {@code trainLeaving} indicates whether the train or
   * cart is in the process of leaving the track.
   */
  private void calculateLocked() {
    final boolean powered = LockingTrackBlock.isPowered(this.getBlockState());
    final boolean sameTrainOrCart = this.isSameTrainOrCart();
    if (this.trainLeaving) {
      this.locked = !(sameTrainOrCart || powered);
      if (this.locked) {
        // When the train is in the process of leaving, we know that the "trainLeaving" state ends
        // when both the carts and redstone signal are false
        this.trainLeaving = false;
      }
    } else {
      this.locked = !powered;
      if (!this.locked && sameTrainOrCart) {
        // When we get both signals we know a train is leaving, so we set the state as so
        this.trainLeaving = true;
      }
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.put("lockingModeController", this.lockingModeController.serializeNBT());
    data.putBoolean("locked", this.locked);
    data.putBoolean("trainLeaving", this.trainLeaving);
    data.putInt("trainDelay", this.trainDelay);
    if (this.prevCart != null) {
      data.putUUID("prevCartId", this.prevCart.getUUID());
    }
    if (this.currentCart != null) {
      data.putUUID("currentCartId", this.currentCart.getUUID());
    }
    data.putUUID("id", this.id);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.lockingModeController = LockingTrackBlock.getLockingMode(state).create(this);
    this.lockingModeController.deserializeNBT(data.getCompound("lockingModeController"));
    this.locked = data.getBoolean("locked");
    this.trainLeaving = data.getBoolean("trainLeaving");
    this.trainDelay = data.getInt("trainDelay");
    if (data.hasUUID("prevCartId")) {
      this.prevCartId = data.getUUID("prevCartId");
    }
    if (data.hasUUID("currentCartId")) {
      this.currentCartId = data.getUUID("currentCartId");
    }
    this.id = data.getUUID("id");
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeBoolean(this.locked);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.locked = data.readBoolean();
  }
}
