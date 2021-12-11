package mods.railcraft.particle;

import java.util.List;
import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class TuningAuraParticleOptions implements ParticleOptions {

  public static final Codec<TuningAuraParticleOptions> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Codec.DOUBLE
                  .listOf()
                  .comapFlatMap(
                      list -> Util.fixedSize(list, 3).map(fixedList -> new Vec3(
                          fixedList.get(0), fixedList.get(1), fixedList.get(2))),
                      vec -> List.of(vec.x(), vec.y(), vec.z()))
                  .fieldOf("destination")
                  .forGetter(TuningAuraParticleOptions::getDestination),
              Codec.INT
                  .fieldOf("color")
                  .forGetter(TuningAuraParticleOptions::getColor))
          .apply(instance, TuningAuraParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final ParticleOptions.Deserializer<TuningAuraParticleOptions> DESERIALIZER =
      new ParticleOptions.Deserializer<>() {
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

  private final Vec3 destination;
  private final int color;

  public TuningAuraParticleOptions(Vec3 destination, int color) {
    this.destination = destination;
    this.color = color;;
  }

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeDouble(this.destination.x());
    buf.writeDouble(this.destination.y());
    buf.writeDouble(this.destination.z());
    buf.writeVarInt(this.color);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f",
        ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
        this.destination.x(),
        this.destination.y(),
        this.destination.z(),
        this.color);
  }

  public Vec3 getDestination() {
    return this.destination;
  }

  public int getColor() {
    return this.color;
  }

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.TUNING_AURA.get();
  }
}
