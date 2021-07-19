/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.items;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

/**
 * Created by CovertJaguar on 6/7/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class InvToolsAPI {

  private InvToolsAPI() {}

  public static boolean isEmpty(@Nullable ItemStack stack) {
    return stack == null || stack.isEmpty();
  }

  public static ItemStack emptyStack() {
    return ItemStack.EMPTY;
  }

  public static Optional<CompoundNBT> getRailcraftData(ItemStack stack, boolean create) {
    if (isEmpty(stack))
      return Optional.empty();
    return Optional.ofNullable(create ? stack.getOrCreateTagElement(RailcraftConstantsAPI.MOD_ID)
        : stack.getTagElement(RailcraftConstantsAPI.MOD_ID));
  }

  public static void clearRailcraftDataSubtag(ItemStack stack, String tag) {
    getRailcraftData(stack, false)
        .filter(nbt -> nbt.contains(tag))
        .ifPresent(nbt -> nbt.remove(tag));
  }

  public static void setRailcraftDataSubtag(ItemStack stack, String tag, INBT data) {
    if (isEmpty(stack)) {
      return;
    }
    getRailcraftData(stack, true).ifPresent(nbt -> nbt.put(tag, data));

  }

  public static Optional<CompoundNBT> getRailcraftDataSubtag(ItemStack stack, String tag) {
    return getRailcraftDataSubtag(stack, tag, false);
  }

  public static Optional<CompoundNBT> getRailcraftDataSubtag(ItemStack stack, String tag,
      boolean create) {
    return getRailcraftData(stack, create)
        .filter(nbt -> create || nbt.contains(tag))
        .map(nbt -> {
          CompoundNBT subNBT = nbt.getCompound(tag);
          nbt.put(tag, subNBT);
          return subNBT;
        });
  }
}
