package mods.railcraft.util.fluids;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import com.google.common.base.Predicates;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandlerUtil;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public final class FluidTools {

  public static final int BUCKET_FILL_TIME = 8;
  public static final int NETWORK_UPDATE_INTERVAL = 128;
  public static final int PROCESS_VOLUME = FluidType.BUCKET_VOLUME * 4;

  private FluidTools() {}

  public static Component toString(FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return Component.literal("Empty");
    }
    return Component.literal(fluidStack.getAmount() + "x")
        .append(fluidStack.getHoverName());
  }

  /**
   * Handles interaction with an item that can (or might) store fluids.
   *
   * @param player The Player
   * @param hand The Hand
   * @param fluidHandler A Fluidhandler
   * @return TRUE if we should return success, FALSE if super must be called.
   */
  public static boolean interactWithFluidHandler(Player player, InteractionHand hand,
      ResourceHandler<FluidResource> fluidHandler) {
    return player.level().isClientSide()
        ? isFluidHandler(player.getItemInHand(hand))
        : FluidUtil.interactWithFluidHandler(player, hand, null, fluidHandler);
  }

  public static boolean isFluidHandler(ItemStack stack) {
    if (stack.isEmpty()) {
      return false;
    }
    return ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM) != null;
  }

  public static boolean isEmptyContainer(ItemStack stack) {
    var cap = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
    if (cap == null) {
      return false;
    }
    for (int i = 0; i < cap.size(); i++) {
      if (cap.getAmountAsLong(i) > 0) {
        return false;
      }
    }
    return true;
  }

  public static boolean isRoomInContainer(ItemStack stack, Fluid fluid) {
    var cap = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
    Objects.requireNonNull(cap);
    try (var tx = Transaction.openRoot()) {
      return cap.insert(FluidResource.of(fluid), Integer.MAX_VALUE, tx) > 0;
    }
  }

  public static boolean containsFluid(ItemStack stack, Fluid fluid) {
    var cap = ItemAccess.forStack(stack).getCapability(Capabilities.Fluid.ITEM);
    Objects.requireNonNull(cap);
    for (int i = 0; i < cap.size(); i++) {
      if (!cap.getResource(i).is(fluid)) {
        return false;
      }
    }
    return true;
  }

  public enum ProcessType {
    FILL_ONLY, DRAIN_ONLY, FILL_THEN_DRAIN, DRAIN_THEN_FILL
  }

  public enum ProcessState implements StringRepresentable {

    FILLING("filling"),
    DRAINING("draining"),
    RESET("reset");

    public static final StringRepresentable.EnumCodec<ProcessState> CODEC =
        StringRepresentable.fromEnum(ProcessState::values);

    private final String name;

    ProcessState(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }

  private static void sendToProcessing(Container container) {
    ContainerMapper.make(container, 0, 1)
        .moveOneItemTo(ContainerMapper.make(container, 1, 1).ignoreItemChecks());
  }

  private static void sendToOutput(Container container) {
    ContainerMapper.make(container, 1, 1)
        .moveOneItemTo(ContainerMapper.make(container, 2, 1).ignoreItemChecks());
  }

  private static ProcessState tryFill(Container container, StandardTank tank, ItemAccess itemAccess) {
    var cap = itemAccess.getCapability(Capabilities.Fluid.ITEM);
    var moved = ResourceHandlerUtil.move(tank, cap, Predicates.alwaysTrue(),
        FluidType.BUCKET_VOLUME, null);
    if (moved == 0) {
      sendToOutput(container);
      return ProcessState.RESET;
    }
    var result = itemAccess.getResource().toStack(itemAccess.getAmount());
    container.setItem(1, result);
    return ProcessState.FILLING;
  }

  private static ProcessState tryDrain(Container container, StandardTank tank,
      ItemAccess itemAccess) {
    var cap = itemAccess.getCapability(Capabilities.Fluid.ITEM);
    var moved = ResourceHandlerUtil.move(cap, tank, Predicates.alwaysTrue(),
        FluidType.BUCKET_VOLUME, null);
    if (moved != FluidType.BUCKET_VOLUME) {
      sendToOutput(container);
      return ProcessState.RESET;
    }
    container.setItem(1, itemAccess.getResource().toStack());
    return ProcessState.DRAINING;
  }

  /**
   * Expects a three slot inventory, with input as slot 0, processing as slot 1, and output as slot
   * 2. Will handle moving an item through all stages from input to output for either filling or
   * draining.
   */
  public static ProcessState processContainer(Container container, StandardTank tank,
      ProcessType type, ProcessState state) {
    var itemStack = container.getItem(1);
    if (itemStack.isEmpty() || !isFluidHandler(itemStack)) {
      sendToProcessing(container);
      return ProcessState.RESET;
    }
    var itemAccess = ItemAccess.forHandlerIndex(VanillaContainerWrapper.of(container), 1);
    if (state == ProcessState.RESET) {
      if (type == ProcessType.FILL_ONLY) {
        return tryFill(container, tank, itemAccess);
      } else if (type == ProcessType.DRAIN_ONLY) {
        return tryDrain(container, tank, itemAccess);
      } else if (type == ProcessType.FILL_THEN_DRAIN) {
        var cap = itemAccess.getCapability(Capabilities.Fluid.ITEM);
        if (ResourceHandlerUtil.move(tank, cap, Predicates.alwaysTrue(),
            FluidType.BUCKET_VOLUME, null) == FluidType.BUCKET_VOLUME) {
          return tryFill(container, tank, itemAccess);
        } else {
          return tryDrain(container, tank, itemAccess);
        }
      } else if (type == ProcessType.DRAIN_THEN_FILL) {
        if (!FluidUtil.getFirstStackContained(itemStack).isEmpty() && !tank.isFull()) {
          return tryDrain(container, tank, itemAccess);
        } else {
          return tryFill(container, tank, itemAccess);
        }
      }
    }
    if (state == ProcessState.FILLING) {
      return tryFill(container, tank, itemAccess);
    }
    if (state == ProcessState.DRAINING) {
      return tryDrain(container, tank, itemAccess);
    }
    return state;
  }

  public static boolean isFullFluidBlock(Level level, BlockPos pos) {
    return isFullFluidBlock(level.getBlockState(pos), level, pos);
  }

  public static boolean isFullFluidBlock(BlockState state, Level level, BlockPos pos) {
    if (state.getBlock() instanceof LiquidBlock) {
      return state.getValue(LiquidBlock.LEVEL) == 0;
    }
    return false;
  }

  public static Fluid getFluid(BlockState state) {
    return state.getFluidState().getType();
  }

  public static Collection<ResourceHandler<FluidResource>> findNeighbors(Level level, BlockPos centrePos,
      Predicate<BlockEntity> filter, Direction... directions) {
    List<ResourceHandler<FluidResource>> targets = new ArrayList<>();
    for (var direction : directions) {
      var blockEntity = level.getBlockEntity(centrePos.relative(direction));
      if (blockEntity == null) {
        continue;
      }
      if (!TankManager.TANK_FILTER.apply(blockEntity, direction.getOpposite())) {
        continue;
      }
      if (!filter.test(blockEntity)) {
        continue;
      }
      var cap = level.getCapability(Capabilities.Fluid.BLOCK,
          blockEntity.getBlockPos(), direction.getOpposite());
      if (cap != null) {
        targets.add(cap);
      }
    }
    return targets;
  }
}
