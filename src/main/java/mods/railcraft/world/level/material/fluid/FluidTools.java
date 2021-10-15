package mods.railcraft.world.level.material.fluid;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
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
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class FluidTools {

  public static final int BUCKET_FILL_TIME = 8;
  public static final int NETWORK_UPDATE_INTERVAL = 128;
  public static final int BUCKET_VOLUME = 1000;
  public static final int PROCESS_VOLUME = BUCKET_VOLUME * 4;

  private FluidTools() {}

  public static ITextComponent toString(@Nullable FluidStack fluidStack) {
    if (fluidStack == null)
      return new StringTextComponent("null");
    return new StringTextComponent(fluidStack.getAmount() + "x")
        .append(fluidStack.getDisplayName());
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

  public enum ProcessState implements IStringSerializable {

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
    if (container.isEmpty() || !FluidUtil.getFluidHandler(container).isPresent()) {
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

  public static void initWaterBottle(boolean nerf) {
    WaterBottleEventHandler.INSTANCE.amount = nerf ? 333 : 1000;
    MinecraftForge.EVENT_BUS.register(WaterBottleEventHandler.INSTANCE);
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

  public static void drip(World level, BlockPos pos, BlockState state, Random rand,
      float particleRed, float particleGreen, float particleBlue) {
    if (rand.nextInt(10) == 0 && Block.canSupportRigidBlock(level, pos.below())
        && !level.getBlockState(pos.below(2)).getMaterial().blocksMotion()) {
      double px = (double) ((float) pos.getX() + rand.nextFloat());
      double py = (double) pos.getY() - 1.05D;
      double pz = (double) ((float) pos.getZ() + rand.nextFloat());

      // TODO implement this particle
      // Particle fx =
      // new ParticleDrip(world, new Vec3d(px, py, pz), particleRed, particleGreen, particleBlue);
      // FMLClientHandler.instance().getClient().effectRenderer.addEffect(fx);
    }
  }

  public static Collection<IFluidHandler> findNeighbors(World level, BlockPos centrePos,
      Predicate<? super TileEntity> filter, Direction... directions) {
    List<IFluidHandler> targets = new ArrayList<>();
    for (Direction direction : directions) {
      TileEntity blockEntity = level.getBlockEntity(centrePos.relative(direction));
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
