package mods.railcraft.network;

import java.util.Optional;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Season;
import mods.railcraft.util.RailcraftCodecs;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.server.players.NameAndId;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RailcraftDataSerializers {

  private static final DeferredRegister<EntityDataSerializer<?>> deferredRegister =
      DeferredRegister
          .create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RailcraftConstants.ID);

  public static final EntityDataSerializer<Optional<NameAndId>> OPTIONAL_NAME_AND_ID =
      EntityDataSerializer.forValueType(ByteBufCodecs.optional(RailcraftCodecs.NAME_AND_ID));

  public static final EntityDataSerializer<Locomotive.Mode> LOCOMOTIVE_MODE =
      EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(Locomotive.Mode.class));

  public static final EntityDataSerializer<Locomotive.Speed> LOCOMOTIVE_SPEED =
      EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(Locomotive.Speed.class));

  public static final EntityDataSerializer<Locomotive.Lock> LOCOMOTIVE_LOCK =
      EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(Locomotive.Lock.class));

  public static final EntityDataSerializer<MaintenanceMinecart.Mode> MAINTENANCE_MODE =
      EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(MaintenanceMinecart.Mode.class));

  public static final EntityDataSerializer<Season> MINECART_SEASON =
      EntityDataSerializer.forValueType(NeoForgeStreamCodecs.enumCodec(Season.class));

  public static final EntityDataSerializer<CompoundTag> COMPOUND_TAG = new EntityDataSerializer<>() {
    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, CompoundTag> codec() {
      return ByteBufCodecs.TRUSTED_COMPOUND_TAG;
    }

    public CompoundTag copy(CompoundTag compoundtag) {
      return compoundtag.copy();
    }
  };

  public static void register(IEventBus modEventBus) {
    deferredRegister.register("optional_name_and_id", () -> OPTIONAL_NAME_AND_ID);
    deferredRegister.register("locomotive_mode", () -> LOCOMOTIVE_MODE);
    deferredRegister.register("locomotive_speed", () -> LOCOMOTIVE_SPEED);
    deferredRegister.register("locomotive_lock", () -> LOCOMOTIVE_LOCK);
    deferredRegister.register("maintenance_mode", () -> MAINTENANCE_MODE);
    deferredRegister.register("minecart_season", () -> MINECART_SEASON);
    deferredRegister.register("compound_tag", () -> COMPOUND_TAG);
    deferredRegister.register(modEventBus);
  }
}
