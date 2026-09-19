package mods.railcraft.world.module;

import java.util.Objects;
import java.util.Optional;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.jspecify.annotations.Nullable;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

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
    if (this.provider.level() instanceof ServerLevel serverLevel) {
      return serverLevel.recipeAccess()
          .getRecipeFor(this.getRecipeType(), new SingleRecipeInput(itemStack), serverLevel);
    }
    return Optional.empty();
  }

  @Override
  protected boolean lacksRequirements() {
    return this.recipe == null;
  }

  @Override
  protected final int calculateDuration() {
    Objects.requireNonNull(this.recipe);
    return this.recipe.cookingTime();
  }
}
