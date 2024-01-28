package mods.railcraft.world.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import mods.railcraft.world.entity.vehicle.TrackRelayer;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import mods.railcraft.world.entity.vehicle.TrackUndercutter;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftEntityTypes {

  private static final DeferredRegister<EntityType<?>> deferredRegister =
      DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, RailcraftConstants.ID);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final RegistryObject<EntityType<TankMinecart>> TANK_MINECART =
      deferredRegister.register("tank_minecart",
          () -> create("tank_minecart",
              EntityType.Builder
                  .<TankMinecart>of(TankMinecart::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackRemover>> TRACK_REMOVER =
      deferredRegister.register("track_remover",
          () -> create("track_remover",
              EntityType.Builder
                  .<TrackRemover>of(TrackRemover::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackLayer>> TRACK_LAYER =
      deferredRegister.register("track_layer",
          () -> create("track_layer",
              EntityType.Builder
                  .<TrackLayer>of(TrackLayer::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackRelayer>> TRACK_RELAYER =
      deferredRegister.register("track_relayer",
          () -> create("track_relayer",
              EntityType.Builder
                  .<TrackRelayer>of(TrackRelayer::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackUndercutter>> TRACK_UNDERCUTTER =
      deferredRegister.register("track_undercutter",
          () -> create("track_undercutter",
              EntityType.Builder
                  .<TrackUndercutter>of(TrackUndercutter::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TunnelBore>> TUNNEL_BORE =
      deferredRegister.register("tunnel_bore",
          () -> create("tunnel_bore",
              EntityType.Builder
                  .<TunnelBore>of(TunnelBore::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(TunnelBore.LENGTH, TunnelBore.HEIGHT)));

  public static final RegistryObject<EntityType<CreativeLocomotive>> CREATIVE_LOCOMOTIVE =
      deferredRegister.register("creative_locomotive",
          () -> create("creative_locomotive",
              EntityType.Builder
                  .<CreativeLocomotive>of(CreativeLocomotive::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<ElectricLocomotive>> ELECTRIC_LOCOMOTIVE =
      deferredRegister.register("electric_locomotive",
          () -> create("electric_locomotive",
              EntityType.Builder
                  .<ElectricLocomotive>of(ElectricLocomotive::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<SteamLocomotive>> STEAM_LOCOMOTIVE =
      deferredRegister.register("steam_locomotive",
          () -> create("steam_locomotive",
              EntityType.Builder
                  .<SteamLocomotive>of(SteamLocomotive::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<FirestoneItemEntity>> FIRESTONE =
      deferredRegister.register("firestone",
          () -> create("firestone",
              EntityType.Builder
                  .<FirestoneItemEntity>of(FirestoneItemEntity::new, MobCategory.MISC)
                  .setTrackingRange(64)
                  .setUpdateInterval(20)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(Railcraft.rl(registryName).toString());
  }
}
