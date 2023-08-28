package mods.railcraft.data.worldgen;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import mods.railcraft.Railcraft;
import mods.railcraft.data.worldgen.features.RailcraftOreFeatures;
import mods.railcraft.data.worldgen.placements.RailcraftOrePlacements;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftDatapackProvider extends DatapackBuiltinEntriesProvider {

  private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
      .add(Registries.CONFIGURED_FEATURE, RailcraftOreFeatures::bootstrap)
      .add(Registries.PLACED_FEATURE, RailcraftOrePlacements::bootstrap)
      .add(ForgeRegistries.Keys.BIOME_MODIFIERS, RailcraftBiomeModifiers::bootstrap);

  public RailcraftDatapackProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider) {
    super(output, lookupProvider, BUILDER, Set.of(Railcraft.ID));
  }
}
