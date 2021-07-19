/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import javax.annotation.Nullable;

import java.util.Optional;
import java.util.function.ToIntFunction;

/**
 * Created by CovertJaguar on 12/25/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IRecipeBuilder<SELF extends IRecipeBuilder<SELF>> {

  /**
   * Adds a resource location that describes the recipe. It is only used for logging, so it doesn't
   * need to be exact.
   *
   * This is required.
   */
  default SELF name(@Nullable ResourceLocation name) {
    return this.self();
  }

  /**
   * Adds a resource location that describes the recipe. It is only used for logging, so it doesn't
   * need to be exact.
   *
   * This is required.
   */
  default SELF name(String name) {
    name(new ResourceLocation(name));
    return this.self();
  }

  /**
   * Adds a resource location that describes the recipe. It is only used for logging, so it doesn't
   * need to be exact.
   *
   * This is required.
   */
  default SELF name(String namespace, String descriptor) {
    name(new ResourceLocation(namespace, descriptor));
    return this.self();
  }

  /**
   * This function will attempt to generate a ResourceLocation from the given object.
   *
   * This is required.
   */
  default SELF name(@Nullable Object name) {
    return this.self();
  }

  default ResourceLocation getName() {
    return new ResourceLocation("invalid", "dummy");
  }

  default boolean notRegistered() {
    return true;
  }

  interface IFeature {
    default <F> Optional<F> getFeature(Class<F> feature) {
      return Optional.empty();
    }
  }

  @SuppressWarnings("unchecked")
  default SELF self() {
    return (SELF) this;
  }

  interface ITimeFeature<SELF extends IRecipeBuilder<SELF>> extends IFeature {

    /**
     * Sets the cooking time/heat value/process time for this recipe, in ticks.
     */
    default SELF time(int ticks) {
      return time(stack -> ticks);
    }

    /**
     * Sets the cooking time/heat value/process time for this recipe, in ticks.
     */
    @SuppressWarnings("unchecked")
    default SELF time(ToIntFunction<ItemStack> tickFunction) {
      getFeature(ITimeFeature.class).ifPresent(impl -> impl.time(tickFunction));
      return (SELF) this;
    }

    @SuppressWarnings("unchecked")
    default ToIntFunction<ItemStack> getTimeFunction() {
      return getFeature(ITimeFeature.class).map(ITimeFeature::getTimeFunction).get();
    }
  }

  interface ISingleInputFeature extends IFeature {
    default Ingredient getInput() {
      return getFeature(ISingleInputFeature.class).map(ISingleInputFeature::getInput).get();
    }
  }

  interface ISingleItemStackOutputFeature<SELF extends IRecipeBuilder<SELF>> extends IFeature {

    @SuppressWarnings("unchecked")
    default SELF output(@Nullable ItemStack output) {
      getFeature(ISingleItemStackOutputFeature.class).ifPresent(impl -> impl.output(output));
      return (SELF) this;
    }

    default SELF output(@Nullable Item output) {
      return output(output, 1);
    }

    default SELF output(@Nullable Item output, int amount) {
      return output(new ItemStack(output, amount));
    }

    default SELF output(@Nullable Block output) {
      return output(output, 1);
    }

    default SELF output(@Nullable Block output, int amount) {
      return output(new ItemStack(output, amount));
    }

    default ItemStack getOutput() {
      return getFeature(ISingleItemStackOutputFeature.class)
          .map(ISingleItemStackOutputFeature::getOutput).orElse(ItemStack.EMPTY);
    }
  }
}
