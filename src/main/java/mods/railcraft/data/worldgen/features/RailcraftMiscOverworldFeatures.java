package mods.railcraft.data.worldgen.features;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.DiskConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftMiscOverworldFeatures {

  private static final DeferredRegister<ConfiguredFeature<?, ?>> deferredRegister =
      DeferredRegister.create(Registries.CONFIGURED_FEATURE, Railcraft.ID);

  public static final RegistryObject<ConfiguredFeature<?, ?>> SALTPETER =
      register("saltpeter",
          () -> new DiskConfiguration(
              RuleBasedBlockStateProvider.simple(RailcraftBlocks.SALTPETER_ORE.get()),
              BlockPredicate
                  .matchesBlocks(List.of(Blocks.DIRT, RailcraftBlocks.SALTPETER_ORE.get())),
              UniformInt.of(2, 3), 1));

  public static final RegistryObject<ConfiguredFeature<?, ?>> FIRESTONE =
      register("firestone",
          () -> new DiskConfiguration(
              RuleBasedBlockStateProvider.simple(RailcraftBlocks.FIRESTONE_ORE.get()),
              BlockPredicate.matchesBlocks(List.of(Blocks.NETHERRACK,
                  RailcraftBlocks.FIRESTONE_ORE.get())),
              ConstantInt.of(1), 1));

  private static RegistryObject<ConfiguredFeature<?, ?>> register(String name,
      Supplier<DiskConfiguration> diskConfiguration) {
    return deferredRegister.register(name,
        () -> new ConfiguredFeature<>(Feature.DISK, diskConfiguration.get()));
  }

  public static Map<ResourceLocation, ConfiguredFeature<?, ?>> collectEntries() {
    return deferredRegister.getEntries().stream()
        .collect(Collectors.toMap(RegistryObject::getId, RegistryObject::get));
  }

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
