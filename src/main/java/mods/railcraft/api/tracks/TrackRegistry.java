/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import com.google.common.collect.ImmutableSet;
import mods.railcraft.api.core.IRailcraftModule;
import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.api.core.RailcraftCore;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

/**
 * The TrackRegistry is part of a system that allows 3rd party addon to simply, quickly, and easily
 * define new {@link TrackType track types} and {@link TrackKit track kits} with unique behaviors.
 *
 * To define a new TrackType, you need to create a new TrackType object.
 *
 * To define a new TrackKit, you need to create a new TrackKit object and provide a
 * {@link ITrackKitInstance} implementation.
 *
 * The TrackKit contains basic constant information about the TrackKit, while the
 * {@link ITrackKitInstance} controls how the TrackKit interacts with the world.
 *
 * Due to some stupidity in the way that Minecraft handles model registration, TrackTypes and
 * TrackKits must be registered during the PRE-INIT phase of a
 * {@link mods.railcraft.api.core.RailcraftModule railcraft module}. So if you want to add new ones,
 * you'll need to define your own RailcraftModule. Thankfully this isn't too hard.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 * @see TrackType
 * @see TrackKit
 * @see ITrackKitInstance
 * @see TrackKitInstance
 */
// TODO fix registries
public final class TrackRegistry<T extends IStringSerializable & IForgeRegistryEntry<T>>
    implements Iterable<T> {

  /**
   * The registry for {@link TrackType track types}.
   */
  public static final TrackRegistry<TrackType> TRACK_TYPE =
      new TrackRegistry<>(TrackType.NBT_TAG, "iron", TrackType.class);
  /**
   * The registry for {@link TrackKit track kits}.
   */
  public static final TrackRegistry<TrackKit> TRACK_KIT =
      new TrackRegistry<>(TrackKit.NBT_TAG, "missing", TrackKit.class);
  private static final TrackKit missingKit;
  private static ImmutableSet<Tuple<TrackType, TrackKit>> combinations = ImmutableSet.of();
  private static int pass;

  static {
    missingKit = new TrackKit.Builder(TrackKitMissing::new)
        .setVisible(false)
        .setRequiresTicks(true)
        .build();
    missingKit.setRegistryName(RailcraftConstantsAPI.locationOf("missing"));
    TRACK_KIT.register(missingKit);
  }

  private final String nbtTag;
  private final String fallback;
  private final IForgeRegistry<T> registry;

  private TrackRegistry(String nbtTag, String fallback, Class<T> type) {
    this.nbtTag = nbtTag;
    this.fallback = fallback;
    RegistryBuilder<T> builder = new RegistryBuilder<>();
    builder.setName(RailcraftConstantsAPI.locationOf(nbtTag));
    builder.setType(type);
    builder.setIDRange(0, 255);
    builder.setDefaultKey(RailcraftConstantsAPI.locationOf(fallback));
    registry = builder.create();
  }

  /**
   * Equivalent of the fallback entry of {@link #TRACK_KIT track kit registry}.
   *
   * @return The missing track kit
   */
  public static TrackKit getMissingTrackKit() {
    return missingKit;
  }

  /**
   * Gets all possible combinations available for an outfitted track. It is immutable.
   *
   * @return An immutable set containing the combinations
   */
  public static ImmutableSet<Tuple<TrackType, TrackKit>> getCombinations() {
    return combinations;
  }

  /**
   * Do not call this! An error will be thrown if you call.
   *
   * @throws TrackRegistryException If outside mods call this method
   */
  public void finalizeRegistry() {
    if (!RailcraftConstantsAPI.MOD_ID
        .equals(checkNotNull(ModLoadingContext.get().getActiveContainer()).getModId())) {
      throw new TrackRegistryException("Finalize called by non-railcraft mods!");
    }
    pass++; // Prevent building when not all track types or track kits are registered.
    if (pass == 2) {
      ImmutableSet.Builder<Tuple<TrackType, TrackKit>> builder = ImmutableSet.builder();
      for (TrackKit trackKit : TrackRegistry.TRACK_KIT) {
        if (!trackKit.isVisible()) {
          continue;
        }
        for (TrackType trackType : TrackRegistry.TRACK_TYPE) {
          if (trackKit.isAllowedTrackType(trackType)) {
            builder.add(new Tuple<>(trackType, trackKit));
          }
        }
      }
      combinations = builder.build();
    }
  }

  /**
   * Registers a new variant. This must be called in
   * {@link IRailcraftModule.ModuleEventHandler#preInit() Pre-Init Stage}.
   *
   * @param variant The new variant to register
   */
  public void register(T variant) {
    if ((!RailcraftConstantsAPI.MOD_ID
        .equals(checkNotNull(ModLoadingContext.get().getActiveContainer()).getModId())
        || RailcraftCore.getInitStage() != RailcraftCore.InitStage.PRE_INIT) && // default ones can
                                                                                // register earlier
        (!checkNotNull(variant.getRegistryName())
            .equals(RailcraftConstantsAPI.locationOf(fallback)))) {
      throw new TrackRegistryException(
          "Track objects must be registered during PRE-INIT from a Railcraft Module class");
    }
    registry.register(variant);
  }

  /**
   * Returns an object in the track registry, looking up by a {@link String} tag. Falls back to the
   * default object if not available.
   *
   * @param tag The string to look up
   * @return The {@link IForgeRegistryEntry object}
   */
  public T get(String tag) {
    String[] tags = tag.split("_", 2);
    if (tags.length == 2) {
      return checkNotNull(registry.getValue(new ResourceLocation(tags[0], tags[1])));
    }
    return checkNotNull(registry.getValue(new ResourceLocation(tag)));
  }

  /**
   * Returns an object in the track registry, looking up by its {@link ResourceLocation identifier}.
   * Falls back to the default object if not available.
   *
   * @param name The identifier
   * @return The {@link IForgeRegistryEntry object}
   */
  public T get(ResourceLocation name) {
    return checkNotNull(registry.getValue(name));
  }

  /**
   * Returns an object in the track registry, looking up by the {@link String string} tag contained
   * in the {@link CompoundNBT nbt data}. Falls back to the default object if not available.
   *
   * @param nbt The nbt data
   * @return The {@link IForgeRegistryEntry object}
   */
  public T get(CompoundNBT nbt) {
    return get(nbt.getString(nbtTag));
  }

  /**
   * Returns an object in the track registry, looking up by info contained in the {@link CompoundNBT
   * nbt} data of the item stack. Falls back to the default object if not available.
   *
   * <p>
   * Use {@link TrackToolsAPI#getTrackType(ItemStack)} for track types instead! This method does not
   * handle flex tracks.
   * </p>
   *
   * @param stack The item stack
   * @return The {@link IForgeRegistryEntry object}
   */
  public T get(ItemStack stack) {
    CompoundNBT nbt = stack.getTagElement(RailcraftConstantsAPI.MOD_ID);
    if (nbt != null) {
      return get(nbt);
    }
    return getFallback();
  }

  /**
   * Gets the track registry entry associated with the integer identifier provided. Mainly used for
   * serialization in packets.
   *
   * @param id The integer identifier
   * @return The {@link IForgeRegistryEntry registry entry}
   */
  public T get(int id) {
    return ((ForgeRegistry<T>) registry).getValue(id);
  }

  /**
   * Gets the default entries for the track registry.
   *
   * @return The default entry
   */
  public T getFallback() {
    return checkNotNull(registry.getValue(RailcraftConstantsAPI.locationOf(fallback)));
  }

  /**
   * Gets the integer identifier associated with a specific track registry entry. Mainly used for
   * serialization in packets.
   *
   * @param variant The {@link IForgeRegistryEntry registry entry}
   * @return The int id
   */
  public int getId(T variant) {
    return ((ForgeRegistry<T>) registry).getID(variant);
  }

  /**
   * Return the forge registry that contain the track kit/type.
   *
   * @return The forge registry
   */
  public IForgeRegistry<T> getRegistry() {
    return registry;
  }

  /**
   * Return all registered variants in an iterator.
   *
   * @return An iterator of variants
   */
  @Override
  public Iterator<T> iterator() {
    return registry.iterator();
  }

  /**
   * Utility method for creating a stream.
   *
   * @return A stream
   */
  public Stream<T> stream() {
    return StreamSupport.stream(spliterator(), false);
  }

  /**
   * Exceptions related to track registry.
   */
  public static class TrackRegistryException extends RuntimeException {

    private static final long serialVersionUID = 615478639241767729L;

    /**
     * The constructor.
     *
     * @param msg Message
     */
    public TrackRegistryException(String msg) {
      super(msg);
    }
  }
}
