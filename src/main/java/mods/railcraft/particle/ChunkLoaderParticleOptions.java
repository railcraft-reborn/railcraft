package mods.railcraft.particle;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

public record ChunkLoaderParticleOptions(Vec3 dest) implements ParticleOptions {

  public static final Codec<ChunkLoaderParticleOptions> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
          Vec3.CODEC.fieldOf("dest").forGetter(ChunkLoaderParticleOptions::dest)
      ).apply(instance, ChunkLoaderParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final Deserializer<ChunkLoaderParticleOptions> DESERIALIZER =
      new Deserializer<>() {
        @Override
        public ChunkLoaderParticleOptions fromCommand(ParticleType<ChunkLoaderParticleOptions> type,
            StringReader reader) throws CommandSyntaxException {
          reader.expect(' ');
          var x = reader.readDouble();
          reader.expect(' ');
          var y = reader.readDouble();
          reader.expect(' ');
          var z = reader.readDouble();
          return new ChunkLoaderParticleOptions(new Vec3(x, y, z));
        }

        @Override
        public ChunkLoaderParticleOptions fromNetwork(ParticleType<ChunkLoaderParticleOptions> type,
            FriendlyByteBuf buf) {
          return new ChunkLoaderParticleOptions(buf.readVec3());
        }
      };

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeVec3(this.dest);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f",
        BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
        this.dest.x(),
        this.dest.y(),
        this.dest.z());
  }

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.CHUNK_LOADER.get();
  }
}
