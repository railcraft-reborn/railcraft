package mods.railcraft.data.worldgen.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.Railcraft;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries.Keys;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBiomeModifier {

  private static final DeferredRegister<Codec<? extends BiomeModifier>> deferredRegister =
      DeferredRegister.create(Keys.BIOME_MODIFIER_SERIALIZERS, Railcraft.ID);

  public static RegistryObject<Codec<RailcraftOreBiomeModifier>> BIOME_MODIFIER =
      deferredRegister.register("railcraft_biome_modifier", () ->
          RecordCodecBuilder.create(builder -> builder.group(
              Biome.LIST_CODEC.fieldOf("biomes").forGetter(RailcraftOreBiomeModifier::biomes),
              PlacedFeature.CODEC.fieldOf("feature").forGetter(RailcraftOreBiomeModifier::feature)
          ).apply(builder, RailcraftOreBiomeModifier::new)));

  public static RegistryObject<Codec<RailcraftSaltpeterBiomeModifier>> SALTPETER_BIOME_MODIFIER =
      deferredRegister.register("railcraft_saltpeter_biome_modifier", () ->
          RecordCodecBuilder.create(builder -> builder.group(
              Biome.LIST_CODEC.fieldOf("biomes").forGetter(RailcraftSaltpeterBiomeModifier::biomes),
              Decoration.CODEC.fieldOf("step").forGetter(RailcraftSaltpeterBiomeModifier::step)
          ).apply(builder, RailcraftSaltpeterBiomeModifier::new)));

  public static RegistryObject<Codec<RailcraftFirestoneBiomeModifier>> FIRESTONE_BIOME_MODIFIER =
      deferredRegister.register("railcraft_firestone_biome_modifier", () ->
          RecordCodecBuilder.create(builder -> builder.group(
              Biome.LIST_CODEC.fieldOf("biomes").forGetter(RailcraftFirestoneBiomeModifier::biomes),
              Decoration.CODEC.fieldOf("step").forGetter(RailcraftFirestoneBiomeModifier::step)
          ).apply(builder, RailcraftFirestoneBiomeModifier::new)));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }
}
