package mods.railcraft.data;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.data.worldgen.RailcraftBiomeModifiers;
import mods.railcraft.data.worldgen.RailcraftStructureSets;
import mods.railcraft.data.worldgen.RailcraftStructures;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import mods.railcraft.data.worldgen.placements.RailcraftOrePlacements;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.ForgeRegistries;

public class RailcraftDatapackProvider extends DatapackBuiltinEntriesProvider {

  private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, RailcraftOreFeatures::bootstrap)
      .add(Registries.PLACED_FEATURE, RailcraftOrePlacements::bootstrap)
      .add(ForgeRegistries.Keys.BIOME_MODIFIERS, RailcraftBiomeModifiers::bootstrap)
      .add(Registries.DAMAGE_TYPE, RailcraftDamageType::bootstrap)
      .add(Registries.STRUCTURE, RailcraftStructures::bootstrap)
      .add(Registries.STRUCTURE_SET, RailcraftStructureSets::bootstrap);

  public RailcraftDatapackProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(output, lookupProvider, BUILDER, Set.of(RailcraftConstants.ID));
  }
}
