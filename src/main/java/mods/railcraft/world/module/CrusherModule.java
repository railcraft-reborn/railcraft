package mods.railcraft.world.module;

import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.charge.ChargeStorage;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.util.ForwardingEnergyStorage;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.item.crafting.CrusherRecipe;
import mods.railcraft.world.item.crafting.RailcraftRecipeTypes;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class CrusherModule extends CrafterModule<CrusherBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 9;
  private static final int COST_PER_TICK = 80;
  private static final int COST_PER_STEP = COST_PER_TICK * PROGRESS_STEP;
  private final ContainerMapper inputContainer;
  private final ContainerMapper outputContainer;
  private final Charge network;
  private Optional<CrusherRecipe> currentRecipe;
  private int currentSlot;
  private final LazyOptional<IItemHandler> itemHandler;
  private final LazyOptional<IEnergyStorage> energyHandler;

  public CrusherModule(CrusherBlockEntity provider, Charge network) {
    super(provider, 18);
    this.network = network;

    inputContainer = ContainerMapper.make(this, SLOT_INPUT, 9);
    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 9).ignoreItemChecks();
    currentRecipe = Optional.empty();

    itemHandler = LazyOptional.of(() -> new InvWrapper(this) {
      @Override
      @NotNull
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < SLOT_OUTPUT) {
          return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
      }

      @Override
      @NotNull
      public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot < SLOT_OUTPUT) {
          return super.insertItem(slot, stack, simulate);
        }
        return stack;
      }
    });
    energyHandler = LazyOptional.of(() -> new ForwardingEnergyStorage(this::storage));
  }

  public Optional<? extends ChargeStorage> storage() {
    return this.provider.level().isClientSide() ? Optional.empty() : this.access().storage();
  }

  private Charge.Access access() {
    return this.network
        .network((ServerLevel) this.provider.level())
        .access(this.provider.blockPos());
  }

  @Override
  public void serverTick() {
    super.serverTick();
    if (!lacksRequirements()) {
      energyHandler.ifPresent(storage -> storage.extractEnergy(COST_PER_TICK, false));
    }
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    if (index < 9 && super.canPlaceItem(index, stack)) {
      return getRecipe(stack).isPresent();
    }
    return true;
  }

  @Override
  protected int calculateDuration() {
    return currentRecipe
        .map(CrusherRecipe::getProcessTime)
        .orElse(CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME);
  }

  @Override
  protected boolean lacksRequirements() {
    return currentRecipe.isEmpty();
  }

  @Override
  protected boolean doProcessStep() {
    return energyHandler
        .map(storage -> storage.getEnergyStored() > COST_PER_STEP)
        .orElse(false);
  }

  @Override
  protected void setupCrafting() {
    if (isRecipeValid()) {
      return;
    }

    currentRecipe = Optional.empty();
    for (int i = 0; i < inputContainer.getContainerSize(); i++) {
      var itemStack = inputContainer.getItem(i);
      if (!itemStack.isEmpty()) {
        currentSlot = i;
        currentRecipe = getRecipe(itemStack);
        break;
      }
    }
  }

  @Override
  protected boolean craftAndPush() {
    final var recipe = currentRecipe.orElseThrow(NullPointerException::new);
    var tempInv = AdvancedContainer.copyOf(outputContainer);
    var outputs = recipe.pollOutputs(provider.getLevel().getRandom());
    var hasSpace = outputs.stream()
        .map(tempInv::insert)
        .allMatch(ItemStack::isEmpty);

    if (hasSpace) {
      outputs.forEach(outputContainer::insert);
      inputContainer.extract(recipe.getIngredients().get(0));
      provider.getLevel().playSound(null, provider.blockPos(),
          SoundEvents.IRON_GOLEM_DEATH, SoundSource.BLOCKS, 1.0f,
          provider.getLevel().getRandom().nextFloat() * 0.25f + 0.7f);
    }
    return hasSpace;
  }

  private boolean isRecipeValid() {
    return currentRecipe
        .map(r -> r.getIngredients().get(0))
        .map(r -> r.test(inputContainer.getItem(currentSlot)))
        .orElse(false);
  }

  private Optional<CrusherRecipe> getRecipe(ItemStack itemStack) {
    return provider.getLevel().getRecipeManager()
        .getRecipeFor(RailcraftRecipeTypes.CRUSHING.get(),
            new SimpleContainer(itemStack), provider.getLevel());
  }

  @Override
  public void setChanged() {
    super.setChanged();
    currentRecipe = Optional.empty();
  }

  public LazyOptional<IItemHandler> getItemHandler() {
    return itemHandler;
  }

  public LazyOptional<IEnergyStorage> getEnergyHandler() {
    return energyHandler;
  }

  public void invalidateCaps() {
    itemHandler.invalidate();
    energyHandler.invalidate();
  }
}
