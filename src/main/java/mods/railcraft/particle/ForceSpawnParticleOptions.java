package mods.railcraft.particle;

import java.util.Locale;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;

public class ForceSpawnParticleOptions implements ParticleOptions {

  public static final Codec<ForceSpawnParticleOptions> CODEC =
      RecordCodecBuilder.create(instance -> instance
          .group(
              Codec.INT
                  .fieldOf("color")
                  .forGetter(ForceSpawnParticleOptions::getColor))
          .apply(instance, ForceSpawnParticleOptions::new));

  @SuppressWarnings("deprecation")
  public static final ParticleOptions.Deserializer<ForceSpawnParticleOptions> DESERIALIZER =
      new ParticleOptions.Deserializer<>() {
        @Override
        public ForceSpawnParticleOptions fromCommand(ParticleType<ForceSpawnParticleOptions> type,
            StringReader reader) throws CommandSyntaxException {
          reader.expect(' ');
          var color = reader.readInt();
          return new ForceSpawnParticleOptions(color);
        }

        @Override
        public ForceSpawnParticleOptions fromNetwork(ParticleType<ForceSpawnParticleOptions> type,
            FriendlyByteBuf buf) {
          return new ForceSpawnParticleOptions(buf.readVarInt());
        }
      };

  private final int color;

  public ForceSpawnParticleOptions(int color) {
    this.color = color;
  }

  @Override
  public void writeToNetwork(FriendlyByteBuf buf) {
    buf.writeVarInt(this.color);
  }

  @Override
  public String writeToString() {
    return String.format(Locale.ROOT, "%s %d",
        ForgeRegistries.PARTICLE_TYPES.getKey(this.getType()),
        this.color);
  }

  public int getColor() {
    return this.color;
  }

  @Override
  public ParticleType<?> getType() {
    return RailcraftParticleTypes.FORCE_SPAWN.get();
  }
}
