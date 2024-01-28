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

public record TuningAuraParticleOptions(Vec3 destination, int color) implements ParticleOptions {

  public static final Codec<TuningAuraParticleOptions> CODEC = RecordCodecBuilder.create(
      instance -> instance.group(
              Vec3.CODEC.fieldOf("destination").forGetter(TuningAuraParticleOptions::destination),
              Codec.INT.fieldOf("color").forGetter(TuningAuraParticleOptions::color))
          .apply(instance, TuningAuraParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final Deserializer<TuningAuraParticleOptions> DESERIALIZER =
      new Deserializer<>() {
        @Override
        public TuningAuraParticleOptions fromCommand(ParticleType<TuningAuraParticleOptions> type,
            StringReader reader) throws CommandSyntaxException {
          reader.expect(' ');
          var x = reader.readDouble();
          reader.expect(' ');
          var y = reader.readDouble();
          reader.expect(' ');
          var z = reader.readDouble();
          reader.expect(' ');
          var color = reader.readInt();
          return new TuningAuraParticleOptions(new Vec3(x, y, z), color);
        }

        @Override
        public TuningAuraParticleOptions fromNetwork(ParticleType<TuningAuraParticleOptions> type,
            FriendlyByteBuf buf) {
          return new TuningAuraParticleOptions(
              new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble()), buf.readVarInt());
        }
      };

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeDouble(this.destination.x());
    buf.writeDouble(this.destination.y());
    buf.writeDouble(this.destination.z());
    buf.writeVarInt(this.color);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %d",
        ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
        this.destination.x(),
        this.destination.y(),
        this.destination.z(),
        this.color);
  }

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.TUNING_AURA.get();
  }
}
