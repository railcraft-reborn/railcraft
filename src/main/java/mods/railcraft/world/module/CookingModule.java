package mods.railcraft.world.module;

import java.util.Optional;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

public abstract class CookingModule<R extends AbstractCookingRecipe, T extends ModuleProvider>
    extends CrafterModule<T> {

  private final int inputSlot;
  @Nullable
  protected Optional<R> recipe;
  private ItemStack lastInput = ItemStack.EMPTY;

  protected CookingModule(T provider, int size, int inputSlot) {
    super(provider, size);
    this.inputSlot = inputSlot;
    this.recipe = Optional.empty();
  }

  protected abstract RecipeType<R> getRecipeType();

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void setupCrafting() {
    var input = this.getItem(this.inputSlot);
    if (!ItemStack.matches(input, this.lastInput)) {
      this.lastInput = input.copy();
      this.recipe = getRecipe(input);
      if (this.recipe.isEmpty() && !input.isEmpty()) {
        this.setItem(this.inputSlot, ItemStack.EMPTY);
        this.provider.dropItem(input);
      }
    }
  }

  @Override
  protected boolean lacksRequirements() {
    return this.recipe.isEmpty();
  }

  @Override
  protected final int calculateDuration() {
    return this.recipe
        .map(AbstractCookingRecipe::getCookingTime)
        .orElseThrow(NullPointerException::new);
  }

  protected Optional<R> getRecipe(ItemStack itemStack) {
    return provider.level().getRecipeManager()
        .getRecipeFor(this.getRecipeType(),
            new SimpleContainer(itemStack), this.provider.level());
  }
}
