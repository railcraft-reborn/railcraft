package mods.railcraft.world.level.block.entity.module;

import java.util.Objects;
import javax.annotation.Nonnull;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public class CokeOvenModule extends CookingModule<CokeOvenRecipe> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 1;
  public static final int SLOT_LIQUID_INPUT = 2;
  public static final int SLOT_PROCESSING_FLUID = 3;
  public static final int SLOT_OUTPUT_FLUID = 4;
  public static final int TANK_CAPACITY = 64 * FluidTools.BUCKET_VOLUME;
  private final ContainerMapper outputContainer =
      new ContainerMapper(this, SLOT_OUTPUT, 1).ignoreItemChecks();
  private int multiplier = 1;

  private final StandardTank tank;
  private int fluidProcessingTimer;
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private final ContainerMapper fluidContainer =
      ContainerMapper.make(this, SLOT_LIQUID_INPUT, SLOT_OUTPUT_FLUID);

  private final LazyOptional<IItemHandler> itemHandler =
      LazyOptional.of(() -> new InvWrapper(this) {
        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
          if (slot != SLOT_OUTPUT && slot != SLOT_OUTPUT_FLUID)
            return ItemStack.EMPTY;
          return super.extractItem(slot, amount, simulate);
        }
      });

  public CokeOvenModule(ModuleProvider provider) {
    super(provider, 5, SLOT_INPUT);
    this.tank = new StandardTank(TANK_CAPACITY);
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
    Objects.requireNonNull(this.recipe);
    var output = this.recipe.getResultItem();
    var fluidOutput = this.recipe.getCreosote();
    if (this.outputContainer.canFit(output)
        && (fluidOutput.isEmpty() || this.tank.fill(fluidOutput,
            IFluidHandler.FluidAction.SIMULATE) >= fluidOutput.getAmount())) {
      this.removeItem(SLOT_INPUT, 1);

      this.outputContainer.addStack(output);
      this.tank.fill(fluidOutput, IFluidHandler.FluidAction.EXECUTE);
      return true;
    }
    return false;
  }

  @Override
  public void serverTick() {
    super.serverTick();

    var topSlot = this.getItem(SLOT_LIQUID_INPUT);
    if (!topSlot.isEmpty() && !FluidItemHelper.isContainer(topSlot)) {
      this.setItem(SLOT_LIQUID_INPUT, ItemStack.EMPTY);
      this.dropItem(topSlot);
    }

    var bottomSlot = this.getItem(SLOT_OUTPUT_FLUID);
    if (!bottomSlot.isEmpty() && !FluidItemHelper.isContainer(bottomSlot)) {
      this.setItem(SLOT_OUTPUT_FLUID, ItemStack.EMPTY);
      this.dropItem(bottomSlot);
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
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (!super.canPlaceItem(slot, stack)) {
      return false;
    }
    switch (slot) {
      case SLOT_INPUT:
        return true;
      case SLOT_LIQUID_INPUT:
        return FluidItemHelper.isRoomInContainer(stack, RailcraftFluids.CREOSOTE.get());
      default:
        return false;
    }
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return this.itemHandler;
  }

  @Override
  public void writeToBuf(FriendlyByteBuf out) {
    super.writeToBuf(out);
    out.writeFluidStack(this.tank.getFluid());
  }

  @Override
  public void readFromBuf(FriendlyByteBuf in) {
    super.readFromBuf(in);
    this.tank.setFluid(in.readFluidStack());
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("tank", this.tank.writeToNBT(new CompoundTag()));
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.tank.readFromNBT(tag.getCompound("tank"));
  }
}
