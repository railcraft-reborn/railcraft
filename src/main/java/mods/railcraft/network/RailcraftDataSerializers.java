package mods.railcraft.network;

import com.mojang.authlib.GameProfile;
import mods.railcraft.Railcraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by CovertJaguar on 6/12/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class RailcraftDataSerializers {
  public static final EntityDataSerializer<FluidStack> FLUID_STACK =
      new EntityDataSerializer<>() {
        @Override
        public void write(FriendlyByteBuf buf, FluidStack value) {
          buf.writeFluidStack(value);
        }

        @Override
        public FluidStack read(FriendlyByteBuf buf) {
          return buf.readFluidStack();
        }

        @Override
        public FluidStack copy(FluidStack value) {
          return value.copy();
        }
      };

  public static final EntityDataSerializer<byte[]> BYTE_ARRAY =
      new EntityDataSerializer<>() {
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
      new EntityDataSerializer<>() {
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

  private static final DeferredRegister<EntityDataSerializer<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Railcraft.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register("fluid_stack", () -> FLUID_STACK);
    deferredRegister.register("byte_array", () -> BYTE_ARRAY);
    deferredRegister.register("optional_game_profile", () -> OPTIONAL_GAME_PROFILE);
    deferredRegister.register(modEventBus);
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
