/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;
import mods.railcraft.api.core.ILocalizedObject;
import mods.railcraft.api.core.IVariantEnum;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
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
public final class TrackKit extends ForgeRegistryEntry<TrackKit>
    implements IVariantEnum, ILocalizedObject {

  public enum Renderer {
    COMPOSITE,
    UNIFIED
  }

  public static final String NBT_TAG = "kit";
  public static Block blockTrackOutfitted;
  public static Item itemKit;
  private final Supplier<? extends ITrackKitInstance> instanceFactory;
  private final Predicate<TrackType> trackTypeFilter;
  private final boolean allowedOnSlopes;
  private final boolean requiresTicks;
  private final boolean visible;
  private final int renderStates;
  private final int maxSupportDistance;
  private final Renderer renderer;

  public TrackKit(Supplier<? extends ITrackKitInstance> instanceFactory,
      Predicate<TrackType> trackTypeFilter,
      Renderer renderer,
      boolean allowedOnSlopes, boolean requiresTicks,
      boolean visible, int renderStates, int maxSupportDistance) {
    this.instanceFactory = instanceFactory;
    this.trackTypeFilter = trackTypeFilter;
    this.renderer = renderer;
    this.allowedOnSlopes = allowedOnSlopes;
    this.requiresTicks = requiresTicks;
    this.visible = visible;
    this.renderStates = renderStates;
    this.maxSupportDistance = maxSupportDistance;
  }

  public static class Builder {

    private final Supplier<? extends ITrackKitInstance> instanceFactory;
    private Predicate<TrackType> trackTypeFilter = (t) -> true;
    private boolean allowedOnSlopes = true;
    private boolean requiresTicks;
    private boolean visible = true;
    private Renderer renderer = Renderer.COMPOSITE;
    private int renderStates = 1;
    private int maxSupportDistance;

    /**
     * Defines a new track kit spec.
     *
     * @param registryName A unique internal string identifier (ex. "railcraft:one_way")
     * @param instanceClass The ITrackInstance class that corresponds to this TrackSpec
     */
    public Builder(Supplier<? extends ITrackKitInstance> instanceClass) {
      this.instanceFactory = instanceClass;
    }

    public TrackKit build() {
      return new TrackKit(
          instanceFactory,
          trackTypeFilter,
          renderer,
          allowedOnSlopes,
          requiresTicks,
          visible,
          renderStates,
          maxSupportDistance);
    }

    public Builder setRenderer(Renderer renderer) {
      this.renderer = renderer;
      return this;
    }

    public Builder setRenderStates(int renderStates) {
      this.renderStates = renderStates;
      return this;
    }

    public Builder setAllowedOnSlopes(boolean allowedOnSlopes) {
      this.allowedOnSlopes = allowedOnSlopes;
      return this;
    }

    public Builder setMaxSupportDistance(int maxSupportDistance) {
      this.maxSupportDistance = maxSupportDistance;
      return this;
    }

    public Builder setTrackTypeFilter(Predicate<TrackType> filter) {
      this.trackTypeFilter = filter;
      return this;
    }

    public Builder setRequiresTicks(boolean requiresTicks) {
      this.requiresTicks = requiresTicks;
      return this;
    }

    public Builder setVisible(boolean visible) {
      this.visible = visible;
      return this;
    }
  }

  @Override
  public String getSerializedName() {
    return Objects.requireNonNull(getRegistryName()).toString().replaceAll("[.:]", "_");
  }

  @Override
  public String getResourcePathSuffix() {
    return getSerializedName();
  }

  @Override
  public String getLocalizationTag() {
    return "track_kit.railcraft." + getSerializedName() + ".name";
  }

  /**
   * This function will only work after the Init Phase.
   *
   * @return an ItemStack that can be used to place the track.
   */
  public ItemStack getTrackKitItem() {
    return getTrackKitItem(1);
  }

  /**
   * This function will only work after the Init Phase.
   *
   * @return an ItemStack that can be used to place the track.
   */
  public ItemStack getTrackKitItem(int qty) {
    if (itemKit != null) {
      ItemStack stack = new ItemStack(itemKit, qty);
      CompoundNBT nbt = stack.getOrCreateTagElement(RailcraftConstantsAPI.MOD_ID);
      nbt.putString(NBT_TAG, getSerializedName());
      return stack;
    }
    return ItemStack.EMPTY;
  }

  /**
   * This function will only work after the Init Phase.
   *
   * @return an ItemStack that can be used to place the track.
   */
  public ItemStack getOutfittedTrack(TrackType trackType) {
    return getOutfittedTrack(trackType, 1);
  }

  /**
   * This function will only work after the Init Phase.
   *
   * @return an ItemStack that can be used to place the track.
   */
  public ItemStack getOutfittedTrack(TrackType trackType, int qty) {
    if (blockTrackOutfitted != null) {
      ItemStack stack = new ItemStack(blockTrackOutfitted, qty);
      CompoundNBT nbt = stack.getOrCreateTagElement(RailcraftConstantsAPI.MOD_ID);
      nbt.putString(TrackType.NBT_TAG, trackType.getSerializedName());
      nbt.putString(NBT_TAG, getSerializedName());
      return stack;
    }
    return ItemStack.EMPTY;
  }

  public ITrackKitInstance createInstance() {
    ITrackKitInstance trackInstance = instanceFactory.get();
    if (trackInstance == null)
      throw new NullPointerException("No track constructor found");
    return trackInstance;
  }

  public Renderer getRenderer() {
    return renderer;
  }

  public int getRenderStates() {
    return renderStates;
  }

  public boolean isAllowedOnSlopes() {
    return allowedOnSlopes;
  }

  public int getMaxSupportDistance() {
    return maxSupportDistance;
  }

  public boolean requiresTicking() {
    return requiresTicks;
  }

  public boolean isAllowedTrackType(TrackType trackType) {
    return trackTypeFilter.test(trackType);
  }

  public boolean isVisible() {
    return visible;
  }

  @Override
  public int ordinal() {
    return TrackRegistry.TRACK_KIT.getId(this);
  }

  @Override
  public String toString() {
    return "TrackKit{" + getSerializedName() + "}";
  }
}
