package mods.railcraft.particle;

import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftParticleTypes {

  private static final DeferredRegister<ParticleType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, RailcraftConstants.ID);

  public static final RegistryObject<SimpleParticleType> STEAM =
      deferredRegister.register("steam", () -> new SimpleParticleType(false));

  public static final RegistryObject<SimpleParticleType> SPARK =
      deferredRegister.register("spark", () -> new SimpleParticleType(false));

  public static final RegistryObject<ParticleType<FireSparkParticleOptions>> FIRE_SPARK =
      deferredRegister.register("fire_spark",
          () -> create(FireSparkParticleOptions.DESERIALIZER, FireSparkParticleOptions.CODEC));

  public static final RegistryObject<SimpleParticleType> PUMPKIN =
      deferredRegister.register("pumpkin", () -> new SimpleParticleType(false));

  public static final RegistryObject<ParticleType<TuningAuraParticleOptions>> TUNING_AURA =
      deferredRegister.register("tuning_aura",
          () -> create(TuningAuraParticleOptions.DESERIALIZER, TuningAuraParticleOptions.CODEC));

  public static final RegistryObject<ParticleType<ForceSpawnParticleOptions>> FORCE_SPAWN =
      deferredRegister.register("force_spawn",
          () -> create(ForceSpawnParticleOptions.DESERIALIZER, ForceSpawnParticleOptions.CODEC));

  public static final RegistryObject<ParticleType<ChunkLoaderParticleOptions>> CHUNK_LOADER =
      deferredRegister.register("chunk_loader",
          () -> create(ChunkLoaderParticleOptions.DESERIALIZER, ChunkLoaderParticleOptions.CODEC));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static <T extends ParticleOptions> ParticleType<T> create(
      @SuppressWarnings("deprecation") ParticleOptions.Deserializer<T> deserializer,
      Codec<T> codec) {
    return new ParticleType<>(false, deserializer) {
      public Codec<T> codec() {
        return codec;
      }
    };
  }
}
