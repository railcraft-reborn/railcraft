package mods.railcraft.world.module;

import org.jetbrains.annotations.NotNull;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CokeOvenModule extends CookingModule<CokeOvenRecipe, CokeOvenBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int SLOT_LIQUID_INPUT = 2;
  public static final int SLOT_LIQUID_PROCESSING = 3;
  public static final int SLOT_LIQUID_OUTPUT = 4;
  private final ContainerMapper outputContainer;
  private int multiplier = 1;

  private final StandardTank tank;
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private final ContainerMapper fluidContainer;

  private final LazyOptional<IItemHandler> itemHandler;
  private final LazyOptional<IFluidHandler> fluidHandler;

  public CokeOvenModule(CokeOvenBlockEntity provider) {
    super(provider, 5, SLOT_INPUT);
    this.tank = StandardTank.ofBuckets(64)
        .disableFill()
        .changeCallback(this::setChanged);

    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 1).ignoreItemChecks();
    fluidContainer = ContainerMapper.make(this, SLOT_LIQUID_INPUT, SLOT_LIQUID_OUTPUT);

    itemHandler = LazyOptional.of(() -> new InvWrapper(this) {
      @Override
      @NotNull
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot == SLOT_INPUT) {
          return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
      }

      @Override
      @NotNull
      public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot == SLOT_INPUT) {
          return super.insertItem(slot, stack, simulate);
        }
        return stack;
      }
    });

    fluidHandler = LazyOptional.of(() -> tank);
  }

  @Override
  protected RecipeType<CokeOvenRecipe> getRecipeType() {
    return RailcraftRecipeTypes.COKING.get();
  }

  @Override
  protected boolean craftAndPush() {
    boolean crafted = false;
    for (int i = 0; i < this.multiplier; i++) {
      crafted |= this.craftAndPushImp();
    }
    return crafted;
  }

  private boolean craftAndPushImp() {
    var output = this.recipe.getResultItem(this.provider.level().registryAccess());
    var fluidOutput = this.recipe.getCreosote();
    if (this.outputContainer.canFit(output)
        && (fluidOutput.isEmpty() || this.tank.internalFill(fluidOutput,
            IFluidHandler.FluidAction.SIMULATE) >= fluidOutput.getAmount())) {
      this.removeItem(SLOT_INPUT, 1);

      this.outputContainer.insert(output);
      this.tank.internalFill(fluidOutput, IFluidHandler.FluidAction.EXECUTE);
      return true;
    }
    return false;
  }

  @Override
  public void serverTick() {
    super.serverTick();

    var topSlot = this.getItem(SLOT_LIQUID_INPUT);
    if (!topSlot.isEmpty() && !FluidTools.isFluidHandler(topSlot)) {
      this.setItem(SLOT_LIQUID_INPUT, ItemStack.EMPTY);
      this.provider.dropItem(topSlot);
    }

    var bottomSlot = this.getItem(SLOT_LIQUID_OUTPUT);
    if (!bottomSlot.isEmpty() && !FluidTools.isFluidHandler(bottomSlot)) {
      this.setItem(SLOT_LIQUID_OUTPUT, ItemStack.EMPTY);
      this.provider.dropItem(bottomSlot);
    }

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState = FluidTools.processContainer(
          this.fluidContainer, this.tank,
          FluidTools.ProcessType.FILL_ONLY, this.processState);
    }
  }

  public boolean needsFuel() {
    return this.getItem(SLOT_INPUT).getCount() < 8;
  }

  public StandardTank getTank() {
    return this.tank;
  }

  @Override
  public boolean canPlaceItem(int slot, ItemStack itemStack) {
    return switch (slot) {
      case SLOT_INPUT -> this.getRecipeFor(itemStack).isPresent();
      case SLOT_LIQUID_INPUT ->
          FluidTools.isRoomInContainer(itemStack, RailcraftFluids.CREOSOTE.get());
      case SLOT_OUTPUT, SLOT_LIQUID_PROCESSING, SLOT_LIQUID_OUTPUT -> true;
      default -> false;
    } && super.canPlaceItem(slot, itemStack);
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return itemHandler;
  }

  public LazyOptional<IFluidHandler> getFluidHandler() {
    return fluidHandler;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put(CompoundTagKeys.TANK, this.tank.writeToNBT(new CompoundTag()));
    tag.putString(CompoundTagKeys.PROCESS_STATE, this.processState.getSerializedName());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tank.readFromNBT(tag.getCompound(CompoundTagKeys.TANK));
    this.processState = FluidTools.ProcessState.fromTag(tag);
  }
}
