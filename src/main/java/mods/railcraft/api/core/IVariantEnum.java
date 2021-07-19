/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.core;

import javax.annotation.Nullable;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IStringSerializable;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public interface IVariantEnum extends IStringSerializable {

  int ordinal();

  default String getResourcePathSuffix() {
    return this.getSerializedName().replace(".", "_");
  }

  default @Nullable ITag<Item> getTag() {
    return null;
  }

  default Ingredient getAlternate(IIngredientSource container) {
    ITag<Item> tag = getTag();
    return tag == null ? Ingredient.EMPTY : Ingredient.of(tag);
  }

  default boolean isEnabled() {
    return true;
  }

  default boolean isDeprecated() {
    return false;
  }
}
