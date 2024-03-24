package mods.railcraft.network;

import java.util.Optional;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.season.Season;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class RailcraftDataSerializers {

  private static final DeferredRegister<EntityDataSerializer<?>> deferredRegister =
      DeferredRegister
          .create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, RailcraftConstants.ID);

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
    deferredRegister.register("optional_game_profile", () -> OPTIONAL_GAME_PROFILE);
    deferredRegister.register("locomotive_mode", () -> LOCOMOTIVE_MODE);
    deferredRegister.register("locomotive_speed", () -> LOCOMOTIVE_SPEED);
    deferredRegister.register("locomotive_lock", () -> LOCOMOTIVE_LOCK);
    deferredRegister.register("maintenance_mode", () -> MAINTENANCE_MODE);
    deferredRegister.register("minecart_season", () -> MINECART_SEASON);
    deferredRegister.register(modEventBus);
  }
}
