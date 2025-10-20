package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.util.fluids.FluidTools;
import mods.railcraft.world.item.crafting.CokeOvenRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

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

  private final ResourceHandler<ItemResource> itemHandler;

  public CokeOvenModule(CokeOvenBlockEntity provider) {
    super(provider, 5, SLOT_INPUT);
    this.tank = StandardTank.ofBuckets(64)
        .disableInsert()
        .changeCallback(this::setChanged);

    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 1).ignoreItemChecks();
    fluidContainer = ContainerMapper.make(this, SLOT_LIQUID_INPUT, SLOT_LIQUID_OUTPUT);

    itemHandler = new DelegatingResourceHandler<>(VanillaContainerWrapper.of(this)) {
      @Override
      public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index == SLOT_INPUT) {
          return 0;
        }
        return super.extract(index, resource, amount, transaction);
      }

      @Override
      public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index == SLOT_INPUT) {
          return super.insert(index, resource, amount, transaction);
        }
        return 0;
      }
    };
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
    var output =
        this.recipe.assemble(null, this.provider.level().registryAccess());
    var fluidOutput = this.recipe.getCreosote();
    if (!this.outputContainer.canFit(output)) {
      return false;
    }

    try (var tx = Transaction.openRoot()) {
      if (fluidOutput.isEmpty() ||
          this.tank.internalInsert(FluidResource.of(fluidOutput), fluidOutput.getAmount(), tx) >= fluidOutput.getAmount()) {
        this.removeItem(SLOT_INPUT, 1);

        this.outputContainer.insert(output);
        tx.commit();
        return true;
      }
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

  public ResourceHandler<ItemResource> getItemHandler() {
    return itemHandler;
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putChild(CompoundTagKeys.TANK, this.tank);
    valueOutput.store(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC, this.processState);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    valueInput.readChild(CompoundTagKeys.TANK, this.tank);
    this.processState = valueInput.read(CompoundTagKeys.PROCESS_STATE, FluidTools.ProcessState.CODEC)
        .orElse(FluidTools.ProcessState.RESET);
  }
}
