package mods.railcraft.network;

import java.util.Arrays;
import java.util.Optional;
import com.mojang.authlib.GameProfile;
import mods.railcraft.Railcraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
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

  public static final EntityDataSerializer<FluidStack> FLUID_STACK =
      new EntityDataSerializer<FluidStack>() {
        @Override
        public final void write(FriendlyByteBuf buf, FluidStack value) {
          buf.writeFluidStack(value);
        }

        @Override
        public final FluidStack read(FriendlyByteBuf buf) {
          return buf.readFluidStack();
        }

        @Override
        public FluidStack copy(FluidStack value) {
          return value.copy();
        }
      };

  public static final EntityDataSerializer<byte[]> BYTE_ARRAY =
      new EntityDataSerializer<byte[]>() {
        @Override
        public void write(FriendlyByteBuf packetBuffer, byte[] bytes) {
          packetBuffer.writeByteArray(bytes);
        }

        @Override
        public byte[] read(FriendlyByteBuf packetBuffer) {
          return packetBuffer.readByteArray();
        }

        @Override
        public byte[] copy(byte[] value) {
          return Arrays.copyOf(value, value.length);
        }
      };

  public static final EntityDataSerializer<Optional<GameProfile>> OPTIONAL_GAME_PROFILE =
      new EntityDataSerializer<Optional<GameProfile>>() {
        @Override
        public void write(FriendlyByteBuf packetBuffer, Optional<GameProfile> optional) {
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
        public Optional<GameProfile> read(FriendlyByteBuf packetBuffer) {
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
      EntityDataSerializer<?> serializer, String name) {
    registry.register(new DataSerializerEntry(serializer)
        .setRegistryName(new ResourceLocation(Railcraft.ID, name)));
  }

  public static <T extends Enum<T>> void setEnum(SynchedEntityData dataManager,
      EntityDataAccessor<Byte> parameter, Enum<T> value) {
    dataManager.set(parameter, (byte) value.ordinal());
  }

  public static <T extends Enum<T>> T getEnum(SynchedEntityData dataManager,
      EntityDataAccessor<Byte> parameter, T[] values) {
    return values[dataManager.get(parameter)];
  }
}
