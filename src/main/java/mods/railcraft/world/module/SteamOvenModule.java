package mods.railcraft.world.module;

import java.util.Optional;
import java.util.stream.IntStream;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.neoforge.transfer.DelegatingResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class SteamOvenModule extends CrafterModule<SteamOvenBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 9;
  private static final int STEAM_PER_STEP = 500;
  private static final int TOTAL_COOK_TIME = 256;
  private static final int ITEMS_SMELTED = 9;
  protected final StandardTank steamTank;
  private final ContainerMapper inputContainer, outputContainer;
  private final ResourceHandler<ItemResource> itemHandler;

  public SteamOvenModule(SteamOvenBlockEntity provider) {
    super(provider, 18);
    this.steamTank = StandardTank.ofBuckets(8)
        .filter(RailcraftTags.Fluids.STEAM);
    this.inputContainer = ContainerMapper.make(this, SLOT_INPUT, 9);
    this.outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 9).ignoreItemChecks();
    this.itemHandler = new DelegatingResourceHandler<>(VanillaContainerWrapper.of(this)) {
      @Override
      public int extract(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index < 9) {
          return 0;
        }
        return super.extract(index, resource, amount, transaction);
      }

      @Override
      public int insert(int index, ItemResource resource, int amount, TransactionContext transaction) {
        if (index >= 9) {
          return 0;
        }
        return super.insert(index, resource, amount, transaction);
      }
    };
  }

  @Override
  protected boolean lacksRequirements() {
    return IntStream.range(0, inputContainer.getContainerSize())
        .mapToObj(inputContainer::getItem)
        .noneMatch(stack -> getRecipe(stack).isPresent());
  }

  private Optional<RecipeHolder<SmeltingRecipe>> getRecipe(ItemStack itemStack) {
    if (provider.getLevel() instanceof ServerLevel serverLevel) {
      return serverLevel.recipeAccess()
          .getRecipeFor(RecipeType.SMELTING,
              new SingleRecipeInput(itemStack), serverLevel);
    }
    return Optional.empty();
  }

  @Override
  protected int calculateDuration() {
    return TOTAL_COOK_TIME;
  }

  @Override
  protected boolean doProcessStep() {
    try (var tx = Transaction.openRoot()) {
      var steamResource = this.steamTank.getResource(0);
      if (steamResource.isEmpty()) {
        return false;
      }

      var steamExtracted = this.steamTank.extract(steamResource, STEAM_PER_STEP, tx);
      if (steamExtracted < STEAM_PER_STEP) {
        return false;
      }
      tx.commit();
      return true;
    }
  }

  @Override
  protected boolean craftAndPush() {
    int count = 0;
    boolean changed = true;
    boolean smelted = false;
    while (count < ITEMS_SMELTED && changed) {
      changed = false;
      for (int slot = 0; slot < 9 && count < ITEMS_SMELTED; slot++) {
        var stack = inputContainer.getItem(slot);
        if (stack.isEmpty()) {
          continue;
        }
        var output = getRecipe(stack)
            .map(x -> x.value().assemble(new SingleRecipeInput(stack)))
            .orElse(ItemStack.EMPTY);
        if (!output.isEmpty() &&
            outputContainer.canFit(output) &&
            outputContainer.insert(output).isEmpty()) {
          inputContainer.removeItem(slot, 1);
          changed = true;
          count++;
        }
      }
      smelted |= changed;
    }
    if (smelted) {
      this.provider.getLevel().playSound(null, provider.blockPos(),
          RailcraftSoundEvents.STEAM_BURST.get(), SoundSource.BLOCKS, 1f,
          provider.getLevel().getRandom().nextFloat() * 0.1f + 1f);
    }
    return smelted;
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack stack) {
    if (index < 9 && super.canPlaceItem(index, stack)) {
      return this.getRecipe(stack).isPresent();
    }
    return super.canPlaceItem(index, stack);
  }

  public StandardTank getSteamTank() {
    return this.steamTank;
  }

  public ResourceHandler<ItemResource> getItemHandler() {
    return this.itemHandler;
  }
}
