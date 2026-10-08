package mods.railcraft.world.entity.vehicle;

import org.jetbrains.annotations.Nullable;
import com.mojang.datafixers.util.Either;
import mods.railcraft.Translations;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.world.entity.RailcraftEntityTypes;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class BedMinecart extends RailcraftMinecart {

  private boolean notifyRider;

  public BedMinecart(EntityType<?> type, Level level) {
    super(type, level);
  }

  public BedMinecart(ItemStack itemStack, double x, double y, double z, Level level) {
    super(itemStack, RailcraftEntityTypes.BED_MINECART.get(), x, y, z, level);
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level().isClientSide() && this.notifyRider) {
      this.notifyRider = false;
      if (this.getFirstPassenger() instanceof Player player && player.isLocalPlayer()) {
        player.displayClientMessage(Component.translatable(Translations.Tips.BED_MINECART_KEYS,
            Component.keybind("key.sneak"),
            Component.keybind(Translations.KeyBinding.SLEEP)), true);
      }
    }
  }

  @Override
  protected void addPassenger(Entity passenger) {
    super.addPassenger(passenger);
    if (this.level().isClientSide() && passenger instanceof Player player
        && player.isLocalPlayer()) {
      // vanilla sends a "press shift" message after adding passenger, so.
      this.notifyRider = true;
    }
  }

  /**
   * Makes the rider sleep on this cart, as if it was a bed.
   */
  public void attemptSleep(ServerPlayer player) {
    if (player.getVehicle() != this) {
      return;
    }
    var pos = this.blockPosition();
    var problem = EventHooks.canPlayerStartSleeping(player, pos, this.checkSleep(player, pos))
        .left().orElse(null);
    if (problem != null) {
      if (problem.getMessage() != null) {
        player.displayClientMessage(problem.getMessage(), true);
      }
      return;
    }

    // Sleeping dismounts the player, so we put him back on the cart.
    player.startSleeping(pos);
    if (player.getVehicle() != this && !player.startRiding(this, true)) {
      player.stopSleeping();
      return;
    }
    player.setPose(Pose.SLEEPING);

    player.awardStat(Stats.SLEEP_IN_BED);
    CriteriaTriggers.SLEPT_IN_BED.trigger(player);
    RailcraftCriteriaTriggers.BED_CART_SLEEP.value().trigger(player, this);
    var level = player.serverLevel();
    if (!level.canSleepThroughNights()) {
      player.displayClientMessage(Component.translatable("sleep.not_possible"), true);
    }
    level.updateSleepingPlayerList();
  }

  /**
   * Same checks of {@link ServerPlayer#startSleepInBed(BlockPos)}, without the ones that require a
   * bed block.
   */
  private Either<Player.BedSleepingProblem, Unit> checkSleep(ServerPlayer player, BlockPos pos) {
    @Nullable
    Player.BedSleepingProblem problem = null;
    if (player.isSleeping() || !player.isAlive()) {
      problem = Player.BedSleepingProblem.OTHER_PROBLEM;
    } else if (!this.level().dimensionType().natural()) {
      problem = Player.BedSleepingProblem.NOT_POSSIBLE_HERE;
    } else if (this.level().isDay()) {
      problem = Player.BedSleepingProblem.NOT_POSSIBLE_NOW;
    } else if (!player.isCreative()) {
      var center = Vec3.atBottomCenterOf(pos);
      var monsters = this.level().getEntitiesOfClass(Monster.class,
          AABB.ofSize(center, 16, 10, 16), monster -> monster.isPreventingPlayerRest(player));
      if (!monsters.isEmpty()) {
        problem = Player.BedSleepingProblem.NOT_SAFE;
      }
    }
    return problem == null ? Either.right(Unit.INSTANCE) : Either.left(problem);
  }

  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    if (player.isSecondaryUseActive()) {
      return InteractionResult.PASS;
    } else if (this.isVehicle()) {
      return InteractionResult.PASS;
    } else if (!this.level().isClientSide) {
      return player.startRiding(this) ? InteractionResult.CONSUME : InteractionResult.PASS;
    } else {
      return InteractionResult.SUCCESS;
    }
  }

  @Override
  public BlockState getDefaultDisplayBlockState() {
    return Blocks.GRAY_CARPET.defaultBlockState();
  }

  @Override
  protected Item getDropItem() {
    return RailcraftItems.BED_MINECART.get();
  }

  @Override
  public boolean canBeRidden() {
    return true;
  }

  @Override
  public boolean canPassItemRequests(ItemStack stack) {
    return true;
  }

  @Override
  protected boolean hasMenu() {
    return false;
  }

  @Override
  protected AbstractContainerMenu createMenu(int containerId, Inventory playerInventory) {
    return null;
  }

  @Override
  public int getContainerSize() {
    return 0;
  }
}
