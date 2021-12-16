/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import java.util.Optional;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

/**
 * Created by CovertJaguar on 6/7/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public final class InvToolsAPI {

  private InvToolsAPI() {}

  public static Optional<CompoundTag> getRailcraftData(ItemStack stack, boolean create) {
    if (stack.isEmpty())
      return Optional.empty();
    return Optional.ofNullable(create ? stack.getOrCreateTagElement(RailcraftConstantsAPI.MOD_ID)
        : stack.getTagElement(RailcraftConstantsAPI.MOD_ID));
  }

  public static void clearRailcraftDataSubtag(ItemStack stack, String tag) {
    getRailcraftData(stack, false)
        .filter(nbt -> nbt.contains(tag))
        .ifPresent(nbt -> nbt.remove(tag));
  }

  public static void setRailcraftDataSubtag(ItemStack stack, String tag, Tag data) {
    if (stack.isEmpty()) {
      return;
    }
    getRailcraftData(stack, true).ifPresent(nbt -> nbt.put(tag, data));

  }

  public static Optional<CompoundTag> getRailcraftDataSubtag(ItemStack stack, String tag) {
    return getRailcraftDataSubtag(stack, tag, false);
  }

  public static Optional<CompoundTag> getRailcraftDataSubtag(ItemStack stack, String tag,
      boolean create) {
    return getRailcraftData(stack, create)
        .filter(nbt -> create || nbt.contains(tag))
        .map(nbt -> {
          CompoundTag subNBT = nbt.getCompound(tag);
          nbt.put(tag, subNBT);
          return subNBT;
        });
  }
}
