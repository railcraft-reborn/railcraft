/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * TrackKits are Items that can be applied to existing tracks to transform them into a more advanced
 * track with special properties. This class defines the behaviors of those advanced tracks.
 *
 * Each track equipped with a TrackKit in the world has a ITrackInstance that corresponds with it.
 *
 * Take note of the difference (similar to block classes and tile entities classes).
 *
 * TrackKits must be registered with the TrackRegistry in the Pre-Init phase of a Railcraft Module.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 * @see TrackRegistry
 * @see ITrackKitInstance
 * @see mods.railcraft.api.core.RailcraftModule
 */
public final class TrackKit extends ForgeRegistryEntry<TrackKit> {

  public static final String NBT_TAG = "kit";
  public static Block blockTrackOutfitted;
  private final Supplier<? extends Item> item;
  private final Supplier<? extends ITrackKitInstance> instanceFactory;
  private final boolean allowedOnSlopes;
  private final boolean requiresTicks;
  private final int maxSupportDistance;

  public TrackKit(Supplier<? extends Item> item,
      Supplier<? extends ITrackKitInstance> instanceFactory,
      boolean allowedOnSlopes, boolean requiresTicks,
      int maxSupportDistance) {
    this.item = item;
    this.instanceFactory = instanceFactory;
    this.allowedOnSlopes = allowedOnSlopes;
    this.requiresTicks = requiresTicks;
    this.maxSupportDistance = maxSupportDistance;
  }

  public ItemStack createItemStack() {
    return this.item.get().getDefaultInstance();
  }

  public ITrackKitInstance createInstance() {
    return this.instanceFactory.get();
  }

  public boolean isAllowedOnSlopes() {
    return this.allowedOnSlopes;
  }

  public int getMaxSupportDistance() {
    return this.maxSupportDistance;
  }

  public boolean requiresTicking() {
    return this.requiresTicks;
  }

  public static class Builder {

    private final Supplier<? extends Item> item;
    private final Supplier<? extends ITrackKitInstance> instanceFactory;
    private boolean allowedOnSlopes = true;
    private boolean requiresTicks;
    private int maxSupportDistance;

    public Builder(Supplier<? extends ITrackKitInstance> instanceFactory) {
      this(() -> Items.AIR, instanceFactory);
    }

    /**
     * Defines a new track kit spec.
     *
     * @param registryName A unique internal string identifier (ex. "railcraft:one_way")
     * @param instanceFactory The ITrackInstance class that corresponds to this TrackSpec
     */
    public Builder(Supplier<? extends Item> item,
        Supplier<? extends ITrackKitInstance> instanceFactory) {
      this.item = item;
      this.instanceFactory = instanceFactory;
    }

    public TrackKit build() {
      return new TrackKit(
          this.item,
          this.instanceFactory,
          this.allowedOnSlopes,
          this.requiresTicks,
          this.maxSupportDistance);
    }

    public Builder setAllowedOnSlopes(boolean allowedOnSlopes) {
      this.allowedOnSlopes = allowedOnSlopes;
      return this;
    }

    public Builder setMaxSupportDistance(int maxSupportDistance) {
      this.maxSupportDistance = maxSupportDistance;
      return this;
    }

    public Builder setRequiresTicks(boolean requiresTicks) {
      this.requiresTicks = requiresTicks;
      return this;
    }
  }
}
