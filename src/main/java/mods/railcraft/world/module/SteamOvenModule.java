package mods.railcraft.world.module;

import java.util.Optional;
import java.util.stream.IntStream;
import org.jetbrains.annotations.NotNull;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.ContainerMapper;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public class SteamOvenModule extends CrafterModule<SteamOvenBlockEntity> {

  public static final int SLOT_INPUT = 0;
  public static final int SLOT_OUTPUT = 9;
  private static final int STEAM_PER_STEP = 500;
  private static final int TOTAL_COOK_TIME = 256;
  private static final int ITEMS_SMELTED = 9;
  protected final StandardTank steamTank;
  private final ContainerMapper inputContainer, outputContainer;
  private final IItemHandler itemHandler;

  public SteamOvenModule(SteamOvenBlockEntity provider) {
    super(provider, 18);
    this.steamTank = StandardTank.ofBuckets(8)
        .filter(RailcraftTags.Fluids.STEAM);
    this.inputContainer = ContainerMapper.make(this, SLOT_INPUT, 9);
    this.outputContainer = ContainerMapper.make(this, SLOT_OUTPUT, 9).ignoreItemChecks();
    this.itemHandler = new InvWrapper(this) {
      @Override
      @NotNull
      public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 9) {
          return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
      }

      @Override
      @NotNull
      public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot >= 9) {
          return stack;
        }
        return super.insertItem(slot, stack, simulate);
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
    return provider.getLevel().getRecipeManager()
        .getRecipeFor(RecipeType.SMELTING,
            new SimpleContainer(itemStack), provider.getLevel());
  }

  @Override
  protected int calculateDuration() {
    return TOTAL_COOK_TIME;
  }

  @Override
  protected boolean doProcessStep() {
    if (!this.needFuel()) {
      this.steamTank.drain(STEAM_PER_STEP, IFluidHandler.FluidAction.EXECUTE);
      return true;
    }
    return false;
  }

  private boolean needFuel() {
    var steam = this.steamTank.drain(STEAM_PER_STEP, IFluidHandler.FluidAction.SIMULATE);
    return steam.getAmount() < STEAM_PER_STEP;
  }

  @Override
  protected boolean craftAndPush() {
    int count = 0;
    boolean changed = true;
    boolean smelted = false;
    var registryAccess = provider.getLevel().registryAccess();
    while (count < ITEMS_SMELTED && changed) {
      changed = false;
      for (int slot = 0; slot < 9 && count < ITEMS_SMELTED; slot++) {
        var stack = inputContainer.getItem(slot);
        if (stack.isEmpty()) {
          continue;
        }
        var output = getRecipe(stack)
            .map(x -> x.value().getResultItem(registryAccess))
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

  public IItemHandler getItemHandler() {
    return this.itemHandler;
  }
}
