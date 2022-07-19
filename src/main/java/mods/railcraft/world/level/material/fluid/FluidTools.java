package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public final class FluidTools {

  public static final int BUCKET_FILL_TIME = 8;
  public static final int NETWORK_UPDATE_INTERVAL = 128;
  public static final int BUCKET_VOLUME = 1000;
  public static final int PROCESS_VOLUME = BUCKET_VOLUME * 4;

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
    return player.getLevel().isClientSide()
        ? FluidItemHelper.isContainer(player.getItemInHand(hand))
        : FluidUtil.interactWithFluidHandler(player, hand, fluidHandler);
  }

  public enum ProcessType {
    FILL_ONLY, DRAIN_ONLY, FILL_THEN_DRAIN, DRAIN_THEN_FILL
  }

  public enum ProcessState implements StringRepresentable {

    FILLING("filling"),
    DRAINING("draining"),
    RESET("reset");

    private static final Map<String, ProcessState> byName = Arrays.stream(values())
        .collect(Collectors.toMap(ProcessState::getSerializedName, Function.identity()));

    private final String name;

    private ProcessState(String name) {
      this.name = name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public static Optional<ProcessState> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
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
    if (itemStack.isEmpty() || !FluidUtil.getFluidHandler(itemStack).isPresent()) {
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
        // TODO https://github.com/MinecraftForge/MinecraftForge/pull/8318
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

  public static void initWaterBottle(boolean nerf) {
    WaterBottleEventHandler.INSTANCE.amount = nerf ? 333 : 1000;
    MinecraftForge.EVENT_BUS.register(WaterBottleEventHandler.INSTANCE);
  }

  public static boolean isFullFluidBlock(Level world, BlockPos pos) {
    return isFullFluidBlock(world.getBlockState(pos), world, pos);
  }

  public static boolean isFullFluidBlock(BlockState state, Level world, BlockPos pos) {
    if (state.getBlock() instanceof LiquidBlock) {
      return state.getValue(LiquidBlock.LEVEL) == 0;
    }
    if (state.getBlock() instanceof IFluidBlock) {
      return Math.abs(((IFluidBlock) state.getBlock()).getFilledPercentage(world, pos)) == 1.0;
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
          .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction.getOpposite())
          .ifPresent(targets::add);
    }
    return targets;
  }

  static final class WaterBottleEventHandler {

    static final WaterBottleEventHandler INSTANCE = new WaterBottleEventHandler();
    int amount;

    private WaterBottleEventHandler() {}

    @SubscribeEvent
    public void onAttachCapability(AttachCapabilitiesEvent<ItemStack> event) {
      if (event.getObject().getItem() == Items.POTION
          && PotionUtils.getPotion(event.getObject()) == Potions.WATER) {
        event.addCapability(new ResourceLocation(Railcraft.ID, "water_bottle_container"),
            new WaterBottleCapabilityDispatcher(event.getObject()));
      }
    }
  }

  private static final class WaterBottleCapabilityDispatcher extends FluidBucketWrapper {

    WaterBottleCapabilityDispatcher(ItemStack container) {
      super(container);
    }

    @Override
    public int fill(FluidStack resource, FluidAction doDrain) {
      return 0;
    }

    @Override
    protected void setFluid(FluidStack fluid) {
      if (fluid.isEmpty()) {
        this.container = new ItemStack(Items.GLASS_BOTTLE);
      }
    }

    @Override
    public FluidStack drain(FluidStack resource, FluidAction doDrain) {
      if (this.container.getCount() != 1 || resource.isEmpty()
          || resource.getAmount() < WaterBottleEventHandler.INSTANCE.amount) {
        return FluidStack.EMPTY;
      }

      FluidStack fluidStack = getFluid();
      if (!fluidStack.isEmpty() && fluidStack.isFluidEqual(resource)) {
        if (doDrain.execute()) {
          this.setFluid(FluidStack.EMPTY);
        }
        return fluidStack;
      }

      return FluidStack.EMPTY;
    }

    @Override
    public FluidStack drain(int maxDrain, FluidAction doDrain) {
      if (this.container.getCount() != 1 || maxDrain < WaterBottleEventHandler.INSTANCE.amount) {
        return FluidStack.EMPTY;
      }

      FluidStack fluidStack = getFluid();
      if (!fluidStack.isEmpty()) {
        if (doDrain.execute()) {
          this.setFluid(FluidStack.EMPTY);
        }
        return fluidStack;
      }

      return FluidStack.EMPTY;
    }

    @Override
    public FluidStack getFluid() {
      return new FluidStack(Fluids.WATER, WaterBottleEventHandler.INSTANCE.amount);
    }
  }
}
