package mods.railcraft.world.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.TankMinecart;
import mods.railcraft.world.entity.vehicle.TrackLayer;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.entity.vehicle.locomotive.CreativeLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.ElectricLocomotive;
import mods.railcraft.world.entity.vehicle.locomotive.SteamLocomotive;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftEntityTypes {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, Railcraft.ID);

  public static final RegistryObject<EntityType<TankMinecart>> TANK_MINECART =
      ENTITY_TYPES.register("tank_minecart",
          () -> create("tank_minecart",
              EntityType.Builder
                  .<TankMinecart>of(TankMinecart::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackRemover>> TRACK_REMOVER =
      ENTITY_TYPES.register("track_remover",
          () -> create("track_remover",
              EntityType.Builder
                  .<TrackRemover>of(TrackRemover::new,
                      MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackLayer>> TRACK_LAYER =
      ENTITY_TYPES.register("track_layer",
          () -> create("track_layer",
              EntityType.Builder
                  .<TrackLayer>of(TrackLayer::new,
                      MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TunnelBore>> TUNNEL_BORE =
      ENTITY_TYPES.register("tunnel_bore",
          () -> create("tunnel_bore",
              EntityType.Builder
                  .<TunnelBore>of(TunnelBore::new, MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(TunnelBore.LENGTH, TunnelBore.HEIGHT)));

  public static final RegistryObject<EntityType<CreativeLocomotive>> CREATIVE_LOCOMOTIVE =
      ENTITY_TYPES.register("creative_locomotive",
          () -> create("creative_locomotive",
              EntityType.Builder
                  .<CreativeLocomotive>of(CreativeLocomotive::new,
                      MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<ElectricLocomotive>> ELECTRIC_LOCOMOTIVE =
      ENTITY_TYPES.register("electric_locomotive",
          () -> create("electric_locomotive",
              EntityType.Builder
                  .<ElectricLocomotive>of(ElectricLocomotive::new,
                      MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<SteamLocomotive>> STEAM_LOCOMOTIVE =
      ENTITY_TYPES.register("steam_locomotive",
          () -> create("steam_locomotive",
              EntityType.Builder
                  .<SteamLocomotive>of(SteamLocomotive::new,
                      MobCategory.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<FirestoneItemEntity>> FIRESTONE =
      ENTITY_TYPES.register("firestone",
          () -> create("firestone", EntityType.Builder.<FirestoneItemEntity>of(
              FirestoneItemEntity::new, MobCategory.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(20)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(Railcraft.ID, registryName).toString());
  }
}
