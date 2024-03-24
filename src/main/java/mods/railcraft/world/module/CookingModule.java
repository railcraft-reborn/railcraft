package mods.railcraft.world.module;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class CookingModule<R extends AbstractCookingRecipe, T extends ModuleProvider>
    extends CrafterModule<T> {

  private final int inputSlot;
  @Nullable
  protected R recipe;
  private ItemStack lastInput = ItemStack.EMPTY;

  protected CookingModule(T provider, int size, int inputSlot) {
    super(provider, size);
    this.inputSlot = inputSlot;
  }

  protected abstract RecipeType<R> getRecipeType();

  @Override
  @OverridingMethodsMustInvokeSuper
  protected void setupCrafting() {
    var input = this.getItem(this.inputSlot);
    if (!ItemStack.matches(input, this.lastInput)) {
      this.lastInput = input.copy();
      this.recipe = this.getRecipeFor(input).map(RecipeHolder::value).orElse(null);
      if (this.recipe == null && !input.isEmpty()) {
        this.setItem(this.inputSlot, ItemStack.EMPTY);
        this.provider.dropItem(input);
      }
    }
  }

  protected Optional<RecipeHolder<R>> getRecipeFor(ItemStack itemStack) {
    return this.provider.level().getRecipeManager()
        .getRecipeFor(this.getRecipeType(), new SimpleContainer(itemStack), this.provider.level());
  }

  @Override
  protected boolean lacksRequirements() {
    return this.recipe == null;
  }

  @Override
  protected final int calculateDuration() {
    Objects.requireNonNull(this.recipe);
    return this.recipe.getCookingTime();
  }
}
