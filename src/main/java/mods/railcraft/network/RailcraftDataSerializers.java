package mods.railcraft.network;

import java.util.Arrays;
import java.util.Optional;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Railcraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * Created by CovertJaguar on 6/12/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class RailcraftDataSerializers {

  public static final IDataSerializer<FluidStack> FLUID_STACK =
      new IDataSerializer<FluidStack>() {
        @Override
        public final void write(PacketBuffer buf, FluidStack value) {
          buf.writeFluidStack(value);
        }

        @Override
        public final FluidStack read(PacketBuffer buf) {
          return buf.readFluidStack();
        }

        @Override
        public FluidStack copy(FluidStack value) {
          return value.copy();
        }
      };

  public static final IDataSerializer<byte[]> BYTE_ARRAY =
      new IDataSerializer<byte[]>() {
        @Override
        public void write(PacketBuffer packetBuffer, byte[] bytes) {
          packetBuffer.writeByteArray(bytes);
        }

        @Override
        public byte[] read(PacketBuffer packetBuffer) {
          return packetBuffer.readByteArray();
        }

        @Override
        public byte[] copy(byte[] value) {
          return Arrays.copyOf(value, value.length);
        }
      };

  public static final IDataSerializer<Optional<GameProfile>> OPTIONAL_GAME_PROFILE =
      new IDataSerializer<Optional<GameProfile>>() {
        @Override
        public void write(PacketBuffer packetBuffer, Optional<GameProfile> optional) {
          if (optional.isPresent()) {
            packetBuffer.writeBoolean(false);
            GameProfile gameProfile = optional.get();
            packetBuffer.writeUUID(gameProfile.getId());
            packetBuffer.writeUtf(gameProfile.getName(), 16);
          } else {
            packetBuffer.writeBoolean(true);
          }
        }

        @Override
        public Optional<GameProfile> read(PacketBuffer packetBuffer) {
          return packetBuffer.readBoolean() ? Optional.empty()
              : Optional.of(new GameProfile(packetBuffer.readUUID(), packetBuffer.readUtf(16)));
        }

        @Override
        public Optional<GameProfile> copy(Optional<GameProfile> optional) {
          return optional;
        }
      };

  // We can't use deferred register because we need access to the type parameters of the
  // IDataSerializers
  public static void register(RegistryEvent.Register<DataSerializerEntry> event) {
    register(event.getRegistry(), FLUID_STACK, "fluid_stack");
    register(event.getRegistry(), BYTE_ARRAY, "byte_array");
    register(event.getRegistry(), OPTIONAL_GAME_PROFILE, "optional_game_profile");
  }

  private static void register(IForgeRegistry<DataSerializerEntry> registry,
      IDataSerializer<?> serializer, String name) {
    registry.register(new DataSerializerEntry(serializer)
        .setRegistryName(new ResourceLocation(Railcraft.ID, name)));
  }

  public static <T extends Enum<T>> void setEnum(EntityDataManager dataManager,
      DataParameter<Byte> parameter, Enum<T> value) {
    dataManager.set(parameter, (byte) value.ordinal());
  }

  public static <T extends Enum<T>> T getEnum(EntityDataManager dataManager,
      DataParameter<Byte> parameter, T[] values) {
    return values[dataManager.get(parameter)];
  }
}
