package mods.railcraft.world.level.material.fluid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.AdjacentBlockEntityCache;
import mods.railcraft.util.inventory.InvTools;
import mods.railcraft.util.inventory.wrappers.InventoryMapper;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class FluidTools {
  public static final int BUCKET_FILL_TIME = 8;
  public static final int NETWORK_UPDATE_INTERVAL = 128;
  public static final int BUCKET_VOLUME = 1000;
  public static final int PROCESS_VOLUME = BUCKET_VOLUME * 4;

  private FluidTools() {}


  public static @Nullable FluidStack copy(@Nullable FluidStack fluidStack) {
    return fluidStack == null ? null : fluidStack.copy();
  }

  public static boolean matches(@Nullable FluidStack left, @Nullable FluidStack right) {
    // FluidStack#equals calls isFluidEqual
    return Objects.equals(left, right);
    // return left == null ? right == null : left.isFluidEqual(right);
  }

  public static ITextComponent toString(@Nullable FluidStack fluidStack) {
    if (fluidStack == null)
      return new StringTextComponent("null");
    return new StringTextComponent(fluidStack.getAmount() + "x")
        .append(fluidStack.getDisplayName());
  }

  public static LazyOptional<IFluidHandler> getFluidHandler(ICapabilityProvider object) {
    return getFluidHandler(null, object);
  }

  public static LazyOptional<IFluidHandler> getFluidHandler(@Nullable Direction side,
      ICapabilityProvider object) {
    return object.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side);
  }

  public static boolean interactWithFluidHandler(PlayerEntity player, Hand hand,
      IFluidHandler fluidHandler) {
    if (!player.level.isClientSide())
      return FluidUtil.interactWithFluidHandler(player, hand, fluidHandler);
    return FluidItemHelper.isContainer(player.getItemInHand(hand));
  }

  public enum ProcessType {
    FILL_ONLY,
    DRAIN_ONLY,
    FILL_THEN_DRAIN,
    DRAIN_THEN_FILL
  }

  public enum ProcessState {
    FILLING,
    DRAINING,
    RESET
  }

  private static void sendToProcessing(IInventory inv) {
    InventoryMapper.make(inv, 0, 1)
        .moveOneItemTo(InventoryMapper.make(inv, 1, 1).ignoreItemChecks());
  }

  private static void sendToOutput(IInventory inv) {
    InventoryMapper.make(inv, 1, 1)
        .moveOneItemTo(InventoryMapper.make(inv, 2, 1).ignoreItemChecks());
  }

  private static ProcessState tryFill(IInventory inv, StandardTank tank, ItemStack container) {
    FluidActionResult filled =
        FluidUtil.tryFillContainer(container, tank, FluidAttributes.BUCKET_VOLUME, null, true);
    if (!filled.isSuccess()) {
      sendToOutput(inv);
      return ProcessState.RESET;
    }
    inv.setItem(1, InvTools.makeSafe(filled.getResult()));
    return ProcessState.FILLING;
  }

  private static ProcessState tryDrain(IInventory inv, StandardTank tank, ItemStack container) {
    FluidActionResult drained =
        FluidUtil.tryEmptyContainer(container, tank, FluidAttributes.BUCKET_VOLUME, null, true);
    if (!drained.isSuccess()) {
      sendToOutput(inv);
      return ProcessState.RESET;
    }
    inv.setItem(1, InvTools.makeSafe(drained.getResult()));
    return ProcessState.DRAINING;
  }

  /**
   * Expects a three slot inventory, with input as slot 0, processing as slot 1, and output as slot
   * 2. Will handle moving an item through all stages from input to output for either filling or
   * draining.
   */
  public static ProcessState processContainer(IInventory inv, StandardTank tank, ProcessType type,
      ProcessState state) {
    ItemStack container = inv.getItem(1);
    if (InvTools.isEmpty(container) || FluidUtil.getFluidHandler(container) == null) {
      sendToProcessing(inv);
      return ProcessState.RESET;
    }
    if (state == ProcessState.RESET) {
      if (type == ProcessType.FILL_ONLY) {
        return tryFill(inv, tank, container);
      } else if (type == ProcessType.DRAIN_ONLY) {
        return tryDrain(inv, tank, container);
      } else if (type == ProcessType.FILL_THEN_DRAIN) {
        if (FluidUtil.tryFillContainer(container, tank, FluidAttributes.BUCKET_VOLUME, null, false)
            .isSuccess())
          return tryFill(inv, tank, container);
        else
          return tryDrain(inv, tank, container);
      } else if (type == ProcessType.DRAIN_THEN_FILL) {
        if (FluidUtil.tryEmptyContainer(container, tank, FluidAttributes.BUCKET_VOLUME, null, false)
            .isSuccess())
          return tryDrain(inv, tank, container);
        else
          return tryFill(inv, tank, container);
      }
    }
    if (state == ProcessState.FILLING)
      return tryFill(inv, tank, container);
    if (state == ProcessState.DRAINING)
      return tryDrain(inv, tank, container);
    return state;
  }

  /**
   * Process containers in input/output slot like the in the tank cart.
   *
   * @param tank Fluid tank
   * @param inv The inventory that contains input/output slots
   * @param inputSlot The input slot number
   * @param outputSlot The output slot number
   * @return {@code true} if changes have been done to the tank
   * @deprecated The two slot functions are deprecated in favor of the three slot function in order
   *             to support partial containers. All usage should be migrated.
   */
  @Deprecated
  public static boolean processContainers(StandardTank tank, IInventory inv, int inputSlot,
      int outputSlot) {
    return processContainers(tank, inv, inputSlot, outputSlot, tank.getFluidType(), true, true);
  }

  @Deprecated
  public static boolean processContainers(StandardTank tank, IInventory inv, int inputSlot,
      int outputSlot, @Nullable Fluid fluidToFill, boolean processFilled, boolean processEmpty) {
    TankManager tankManger = new TankManager();
    tankManger.add(tank);
    return processContainers(tankManger, inv, inputSlot, outputSlot, fluidToFill, processFilled,
        processEmpty);
  }

  @Deprecated
  public static boolean processContainers(TankManager tank, IInventory inv, int inputSlot,
      int outputSlot, @Nullable Fluid fluidToFill) {
    return processContainers(tank, inv, inputSlot, outputSlot, fluidToFill, true, true);
  }

  @Deprecated
  public static boolean processContainers(IFluidHandler fluidHandler, IInventory inv, int inputSlot,
      int outputSlot, @Nullable Fluid fluidToFill, boolean processFilled, boolean processEmpty) {
    ItemStack input = inv.getItem(inputSlot);

    if (InvTools.isEmpty(input))
      return false;

    if (processFilled && drainContainers(fluidHandler, inv, inputSlot, outputSlot))
      return true;

    if (processEmpty && fluidToFill != null)
      return fillContainers(fluidHandler, inv, inputSlot, outputSlot, fluidToFill);
    return false;
  }

  @Deprecated
  public static boolean fillContainers(IFluidHandler source, IInventory inv, int inputSlot,
      int outputSlot, @Nullable Fluid fluidToFill) {
    ItemStack input = inv.getItem(inputSlot);
    // need an empty container
    if (InvTools.isEmpty(input))
      return false;
    ItemStack output = inv.getItem(outputSlot);
    FluidActionResult container =
        FluidUtil.tryFillContainer(input, source, BUCKET_VOLUME, null, false);
    // check failure
    if (!container.isSuccess())
      return false;
    // check filled fluid type
    if (fluidToFill != null && !InvTools.isEmpty(container.getResult())) {
      if (FluidUtil.getFluidContained(container.getResult())
          .filter(fluidStack -> fluidStack.getFluid() == fluidToFill).isPresent())
        return false;
    }
    // check place for container
    if (!InvTools.canMerge(output, container.getResult()))
      return false;
    // do actual things here
    container = FluidUtil.tryFillContainer(input, source, BUCKET_VOLUME, null, true);
    storeContainer(inv, inputSlot, outputSlot, container.getResult());
    return true;
  }

  @Deprecated
  public static boolean drainContainers(IFluidHandler dest, IInventory inv, int inputSlot,
      int outputSlot) {
    ItemStack input = inv.getItem(inputSlot);
    // need a valid container
    if (InvTools.isEmpty(input))
      return false;
    ItemStack output = inv.getItem(outputSlot);
    FluidActionResult container =
        FluidUtil.tryEmptyContainer(input, dest, BUCKET_VOLUME, null, false);
    // check failure
    if (!container.isSuccess())
      return false;
    // check place for container
    if (!InvTools.canMerge(output, container.getResult()))
      return false;
    // do actual things here
    container = FluidUtil.tryEmptyContainer(input, dest, BUCKET_VOLUME, null, true);
    storeContainer(inv, inputSlot, outputSlot, container.getResult());
    return true;
  }

  /**
   * We can assume that if null is passed for the container that the container was consumed by the
   * process and we should just remove the input container.
   */
  @Deprecated
  private static void storeContainer(IInventory inv, int inputSlot, int outputSlot,
      @Nullable ItemStack container) {
    if (InvTools.isEmpty(container)) {
      inv.removeItem(inputSlot, 1);
      return;
    }
    ItemStack output = inv.getItem(outputSlot);
    if (InvTools.isEmpty(output))
      inv.setItem(outputSlot, container);
    else
      InvTools.inc(output);
    inv.removeItem(inputSlot, 1);
  }

  public static void initWaterBottle(boolean nerf) {
    WaterBottleEventHandler.INSTANCE.amount = nerf ? 333 : 1000;
    MinecraftForge.EVENT_BUS.register(WaterBottleEventHandler.INSTANCE);
  }

  private static @Nullable FluidStack drainForgeFluid(BlockState state, World world, BlockPos pos,
      FluidAction doDrain) {
    if (state.getBlock() instanceof IFluidBlock) {
      IFluidBlock fluidBlock = (IFluidBlock) state.getBlock();
      if (fluidBlock.canDrain(world, pos))
        return fluidBlock.drain(world, pos, doDrain);
    }
    return null;
  }


  public static boolean isFullFluidBlock(World world, BlockPos pos) {
    return isFullFluidBlock(world.getBlockState(pos), world, pos);
  }

  public static boolean isFullFluidBlock(BlockState state, World world, BlockPos pos) {
    if (state.getBlock() instanceof FlowingFluidBlock)
      return state.getValue(FlowingFluidBlock.LEVEL) == 0;
    if (state.getBlock() instanceof IFluidBlock)
      return Math.abs(((IFluidBlock) state.getBlock()).getFilledPercentage(world, pos)) == 1.0;
    return false;
  }

  public static Fluid getFluid(BlockState state) {
    return state.getFluidState().getType();
  }

  public static void drip(World world, BlockPos pos, BlockState state, Random rand,
      float particleRed, float particleGreen, float particleBlue) {
    if (rand.nextInt(10) == 0 && Block.canSupportRigidBlock(world, pos.below())
        && !WorldPlugin.getBlockMaterial(world, pos.below(2)).blocksMotion()) {
      double px = (double) ((float) pos.getX() + rand.nextFloat());
      double py = (double) pos.getY() - 1.05D;
      double pz = (double) ((float) pos.getZ() + rand.nextFloat());

      // Particle fx =
      // new ParticleDrip(world, new Vec3d(px, py, pz), particleRed, particleGreen, particleBlue);
      // FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }
  }

  public static Collection<IFluidHandler> findNeighbors(AdjacentBlockEntityCache cache,
      Predicate<? super TileEntity> filter, Direction... sides) {
    List<IFluidHandler> targets = new ArrayList<>();
    for (Direction side : sides) {
      TileEntity tile = cache.getTileOnSide(side);
      if (tile == null)
        continue;
      if (!TankManager.TANK_FILTER.apply(tile, side.getOpposite()))
        continue;
      if (!filter.test(tile))
        continue;
      FluidTools.getFluidHandler(side.getOpposite(), tile).ifPresent(targets::add);
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
    protected void setFluid(@Nullable FluidStack fluid) {
      if (fluid == null) {
        container = new ItemStack(Items.GLASS_BOTTLE);
      }
    }

    @Override
    public @Nullable FluidStack drain(FluidStack resource, FluidAction doDrain) {
      if (InvTools.sizeOf(container) != 1 || resource == null
          || resource.getAmount() < WaterBottleEventHandler.INSTANCE.amount) {
        return null;
      }

      FluidStack fluidStack = getFluid();
      if (fluidStack != null && fluidStack.isFluidEqual(resource)) {
        if (doDrain.execute()) {
          setFluid((FluidStack) null);
        }
        return fluidStack;
      }

      return null;
    }

    @Override
    public @Nullable FluidStack drain(int maxDrain, FluidAction doDrain) {
      if (container.getCount() != 1 || maxDrain < WaterBottleEventHandler.INSTANCE.amount) {
        return null;
      }

      FluidStack fluidStack = getFluid();
      if (fluidStack != null) {
        if (doDrain.execute()) {
          setFluid((FluidStack) null);
        }
        return fluidStack;
      }

      return null;
    }

    @Override
    public FluidStack getFluid() {
      return new FluidStack(Fluids.WATER, WaterBottleEventHandler.INSTANCE.amount);
    }
  }
}
