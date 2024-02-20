package mods.railcraft.util.fluids;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.material.StandardTank;
import mods.railcraft.world.level.material.TankManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.IFluidHandler;

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
        .append(fluidStack.getDisplayName());
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
      IFluidHandler fluidHandler) {
    return player.level().isClientSide()
        ? isFluidHandler(player.getItemInHand(hand))
        : FluidUtil.interactWithFluidHandler(player, hand, fluidHandler);
  }

  public static boolean isFluidHandler(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack).isPresent();
  }

  public static boolean isEmptyContainer(ItemStack stack) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (!item.getFluidInTank(i).isEmpty()) {
              return false;
            }
          }
          return true;
        })
        .isPresent();
  }

  public static boolean isRoomInContainer(ItemStack stack, Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> item.fill(new FluidStack(fluid, Integer.MAX_VALUE),
            IFluidHandler.FluidAction.SIMULATE) > 0)
        .isPresent();
  }

  public static boolean containsFluid(ItemStack stack, Fluid fluid) {
    return FluidUtil.getFluidHandler(stack)
        .filter(item -> {
          for (int i = 0; i < item.getTanks(); i++) {
            if (!item.getFluidInTank(i).getFluid().isSame(fluid)) {
              return false;
            }
          }
          return true;
        })
        .isPresent();
  }

  public enum ProcessType {
    FILL_ONLY, DRAIN_ONLY, FILL_THEN_DRAIN, DRAIN_THEN_FILL
  }

  public enum ProcessState implements StringRepresentable {

    FILLING("filling"),
    DRAINING("draining"),
    RESET("reset");

    private static final StringRepresentable.EnumCodec<ProcessState> CODEC =
        StringRepresentable.fromEnum(ProcessState::values);

    private final String name;

    ProcessState(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static ProcessState fromName(String name) {
      return CODEC.byName(name, ProcessState.RESET);
    }

    public static ProcessState fromTag(CompoundTag tag) {
      return fromName(tag.getString(CompoundTagKeys.PROCESS_STATE));
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

  private static ProcessState tryFill(Container container, StandardTank tank, ItemStack itemStack) {
    var result =
        FluidUtil.tryFillContainer(itemStack, tank, FluidType.BUCKET_VOLUME, null, true);
    if (!result.isSuccess()) {
      sendToOutput(container);
      return ProcessState.RESET;
    }
    container.setItem(1, result.getResult());
    return ProcessState.FILLING;
  }

  private static ProcessState tryDrain(Container container, StandardTank tank,
      ItemStack itemStack) {
    var result =
        FluidUtil.tryEmptyContainer(itemStack, tank, FluidType.BUCKET_VOLUME, null, true);
    if (!result.isSuccess()) {
      sendToOutput(container);
      return ProcessState.RESET;
    }
    container.setItem(1, result.getResult());
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
    if (state == ProcessState.RESET) {
      if (type == ProcessType.FILL_ONLY) {
        return tryFill(container, tank, itemStack);
      } else if (type == ProcessType.DRAIN_ONLY) {
        return tryDrain(container, tank, itemStack);
      } else if (type == ProcessType.FILL_THEN_DRAIN) {
        if (FluidUtil.tryFillContainer(itemStack, tank,
            FluidType.BUCKET_VOLUME, null, false).isSuccess()) {
          return tryFill(container, tank, itemStack);
        } else {
          return tryDrain(container, tank, itemStack);
        }
      } else if (type == ProcessType.DRAIN_THEN_FILL) {
        if (FluidUtil.getFluidContained(itemStack).isPresent() && !tank.isFull()) {
          return tryDrain(container, tank, itemStack);
        } else {
          return tryFill(container, tank, itemStack);
        }
      }
    }
    if (state == ProcessState.FILLING) {
      return tryFill(container, tank, itemStack);
    }
    if (state == ProcessState.DRAINING) {
      return tryDrain(container, tank, itemStack);
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
    if (state.getBlock() instanceof IFluidBlock fluidBlock) {
      return Math.abs(fluidBlock.getFilledPercentage(level, pos)) == 1.0;
    }
    return false;
  }

  public static Fluid getFluid(BlockState state) {
    return state.getFluidState().getType();
  }

  public static Collection<IFluidHandler> findNeighbors(Level level, BlockPos centrePos,
      Predicate<BlockEntity> filter, Direction... directions) {
    List<IFluidHandler> targets = new ArrayList<>();
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
      blockEntity
          .getCapability(ForgeCapabilities.FLUID_HANDLER, direction.getOpposite())
          .ifPresent(targets::add);
    }
    return targets;
  }
}
