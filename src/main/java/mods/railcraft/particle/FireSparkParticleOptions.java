package mods.railcraft.particle;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public record FireSparkParticleOptions(Vec3 destination) implements ParticleOptions {

  public static final Codec<FireSparkParticleOptions> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(Vec3.CODEC
              .fieldOf("destination")
              .forGetter(FireSparkParticleOptions::destination))
          .apply(instance, FireSparkParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final Deserializer<FireSparkParticleOptions> DESERIALIZER =
      new Deserializer<>() {
        @Override
        public FireSparkParticleOptions fromCommand(ParticleType<FireSparkParticleOptions> type,
            StringReader reader) throws CommandSyntaxException {
          reader.expect(' ');
          var x = reader.readDouble();
          reader.expect(' ');
          var y = reader.readDouble();
          reader.expect(' ');
          var z = reader.readDouble();
          return new FireSparkParticleOptions(new Vec3(x, y, z));
        }

        @Override
        public FireSparkParticleOptions fromNetwork(ParticleType<FireSparkParticleOptions> type,
            FriendlyByteBuf buf) {
          return new FireSparkParticleOptions(
              new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()));
        }
      };

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeDouble(this.destination.x());
    buf.writeDouble(this.destination.y());
    buf.writeDouble(this.destination.z());
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
        ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
        this.destination.x(),
        this.destination.y(),
        this.destination.z());
  }


  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.FIRE_SPARK.get();
  }
}
