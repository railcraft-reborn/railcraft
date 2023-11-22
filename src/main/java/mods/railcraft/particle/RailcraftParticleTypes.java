package mods.railcraft.particle;

import java.util.function.Function;
import com.mojang.serialization.Codec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftParticleTypes {

  private static final DeferredRegister<ParticleType<?>> deferredRegister =
      DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, RailcraftConstants.ID);

  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> STEAM =
      deferredRegister.register("steam", () -> new SimpleParticleType(false));

  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> SPARK =
      deferredRegister.register("spark", () -> new SimpleParticleType(false));

  public static final DeferredHolder<ParticleType<?>, ParticleType<FireSparkParticleOptions>> FIRE_SPARK =
      deferredRegister.register("fire_spark",
          () -> create(FireSparkParticleOptions.DESERIALIZER,
              __ -> FireSparkParticleOptions.CODEC));

  public static final DeferredHolder<ParticleType<?>, SimpleParticleType> PUMPKIN =
      deferredRegister.register("pumpkin", () -> new SimpleParticleType(false));

  public static final DeferredHolder<ParticleType<?>, ParticleType<TuningAuraParticleOptions>> TUNING_AURA =
      deferredRegister.register("tuning_aura",
          () -> create(TuningAuraParticleOptions.DESERIALIZER,
              __ -> TuningAuraParticleOptions.CODEC));

  public static final DeferredHolder<ParticleType<?>, ParticleType<ForceSpawnParticleOptions>> FORCE_SPAWN =
      deferredRegister.register("force_spawn",
          () -> create(ForceSpawnParticleOptions.DESERIALIZER,
              __ -> ForceSpawnParticleOptions.CODEC));

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  private static <T extends ParticleOptions> ParticleType<T> create(
      @SuppressWarnings("deprecation") ParticleOptions.Deserializer<T> deserializer,
      Function<ParticleType<T>, Codec<T>> factory) {
    return new ParticleType<T>(false, deserializer) {
      public Codec<T> codec() {
        return factory.apply(this);
      }
    };
  }
}
