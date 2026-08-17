package mods.railcraft.world.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.energy.EnergyHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class CrusherModule extends CrafterModule<CrusherBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 9;
  private static final int COST_PER_TICK = 80;
  private static final int COST_PER_STEP = COST_PER_TICK * PROGRESS_STEP;
  private final ContainerMapper inputContainer;
  private final ContainerMapper outputContainer;
  private final RandomSource random;
  private final Charge network;
  private Optional<RecipeHolder<CrusherRecipe>> currentRecipe;
  private int currentSlot;
  private final ResourceHandler<ItemResource> itemHandler;
  private final EnergyHandler energyHandler;

  public CrusherModule(CrusherBlockEntity provider, Charge network) {
    super(provider, 18);
    this.random = RandomSource.create();
    this.network = network;

    inputContainer = ContainerMapper.make(this, SLOT_INPUT, 9);
    outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 9).ignoreItemChecks();
    currentRecipe = Optional.empty();

    itemHandler = new DelegatingResourceHandler<>(VanillaContainerWrapper.of(this)) {
      @Override
      public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index < SLOT_OUTPUT) {
          return 0;
        }
        return super.extract(index, resource, amount, transaction);
      }

      @Override
      public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index < SLOT_OUTPUT) {
          return super.insert(index, resource, amount, transaction);
        }
        return 0;
      }
    };
    energyHandler = new ForwardingEnergyStorage(this::storage);
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
      try (var tx = Transaction.openRoot()) {
        energyHandler.extract(COST_PER_TICK, tx);
        tx.commit();
      }
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
        .map(RecipeHolder::value)
        .map(CrusherRecipe::getProcessTime)
        .orElse(CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME);
  }

  @Override
  protected boolean lacksRequirements() {
    return currentRecipe.isEmpty();
  }

  @Override
  protected boolean doProcessStep() {
    return energyHandler.getAmountAsInt() > COST_PER_STEP;
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

  private List<ItemStack> pollOutputs(CrusherRecipe recipe) {
    var result = new ArrayList<ItemStack>();
    for(var item : recipe.getProbabilityOutputs()) {
      if(random.nextDouble() < item.probability()) {
        result.add(item.getOutput());
      }
    }
    return result;
  }

  @Override
  protected boolean craftAndPush() {
    final var recipe = currentRecipe
        .map(RecipeHolder::value)
        .orElseThrow(NullPointerException::new);
    var tempInv = AdvancedContainer.copyOf(outputContainer);
    var outputs = pollOutputs(recipe);
    var hasSpace = outputs.stream()
        .map(tempInv::insert)
        .allMatch(ItemStack::isEmpty);

    if (hasSpace) {
      outputs.forEach(outputContainer::insert);
      inputContainer.extract(recipe.placementInfo().ingredients().getFirst());
      provider.getLevel().playSound(null, provider.blockPos(),
          SoundEvents.IRON_GOLEM_DEATH, SoundSource.BLOCKS, 1,
          provider.getLevel().getRandom().nextFloat() * 0.25F + 0.7F);
    }
    return hasSpace;
  }

  private boolean isRecipeValid() {
    return currentRecipe
        .map(RecipeHolder::value)
        .map(r -> r.placementInfo().ingredients().getFirst())
        .map(r -> r.test(inputContainer.getItem(currentSlot)))
        .orElse(false);
  }

  private Optional<RecipeHolder<CrusherRecipe>> getRecipe(ItemStack itemStack) {
    if (provider.getLevel() instanceof ServerLevel serverLevel) {
      return serverLevel.recipeAccess()
              .getRecipeFor(RailcraftRecipeTypes.CRUSHING.get(),
                      new SingleRecipeInput(itemStack), serverLevel);
    }
    return Optional.empty();
  }

  @Override
  public void setChanged() {
    super.setChanged();
    currentRecipe = Optional.empty();
  }

  public ResourceHandler<ItemResource> getItemHandler() {
    return itemHandler;
  }

  public EnergyHandler getEnergyHandler() {
    return energyHandler;
  }
}
