package mods.railcraft.network;

import java.util.Arrays;
import java.util.Optional;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Season;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftDataSerializers {

  private static final DeferredRegister<EntityDataSerializer<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RailcraftConstants.ID);

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
      EntityDataSerializer
          .optional(FriendlyByteBuf::writeGameProfile, FriendlyByteBuf::readGameProfile);

  public static final EntityDataSerializer<Locomotive.Mode> LOCOMOTIVE_MODE =
      EntityDataSerializer.simpleEnum(Locomotive.Mode.class);

  public static final EntityDataSerializer<Locomotive.Speed> LOCOMOTIVE_SPEED =
      EntityDataSerializer.simpleEnum(Locomotive.Speed.class);

  public static final EntityDataSerializer<Locomotive.Lock> LOCOMOTIVE_LOCK =
      EntityDataSerializer.simpleEnum(Locomotive.Lock.class);

  public static final EntityDataSerializer<MaintenanceMinecart.Mode> MAINTENANCE_MODE =
      EntityDataSerializer.simpleEnum(MaintenanceMinecart.Mode.class);

  public static final EntityDataSerializer<Season> MINECART_SEASON =
      EntityDataSerializer.simpleEnum(Season.class);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register("fluid_stack", () -> FLUID_STACK);
    deferredRegister.register("byte_array", () -> BYTE_ARRAY);
    deferredRegister.register("optional_game_profile", () -> OPTIONAL_GAME_PROFILE);
    deferredRegister.register("locomotive_mode", () -> LOCOMOTIVE_MODE);
    deferredRegister.register("locomotive_speed", () -> LOCOMOTIVE_SPEED);
    deferredRegister.register("locomotive_lock", () -> LOCOMOTIVE_LOCK);
    deferredRegister.register("maintenance_mode", () -> MAINTENANCE_MODE);
    deferredRegister.register("minecart_season", () -> MINECART_SEASON);
    deferredRegister.register(modEventBus);
  }
}
