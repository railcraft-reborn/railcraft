package mods.railcraft.world.entity;

import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.cart.CreativeLocomotiveEntity;
import mods.railcraft.world.entity.cart.TankMinecartEntity;
import mods.railcraft.world.entity.cart.TrackLayerMinecartEntity;
import mods.railcraft.world.entity.cart.TrackRemoverMinecartEntity;
import mods.railcraft.world.entity.cart.TunnelBoreEntity;
import mods.railcraft.world.entity.cart.locomotives.ElectricLocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotives.SteamLocomotiveEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftEntityTypes {

  public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
      DeferredRegister.create(ForgeRegistries.ENTITIES, Railcraft.ID);

  public static final RegistryObject<EntityType<TankMinecartEntity>> TANK_MINECART =
      ENTITY_TYPES.register("tank_minecart",
          () -> create("tank_minecart",
              EntityType.Builder
                  .<TankMinecartEntity>of(TankMinecartEntity::new, EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackRemoverMinecartEntity>> TRACK_REMOVER =
      ENTITY_TYPES.register("track_remover",
          () -> create("track_remover",
              EntityType.Builder
                  .<TrackRemoverMinecartEntity>of(TrackRemoverMinecartEntity::new,
                      EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TrackLayerMinecartEntity>> TRACK_LAYER =
      ENTITY_TYPES.register("track_layer",
          () -> create("track_layer",
              EntityType.Builder
                  .<TrackLayerMinecartEntity>of(TrackLayerMinecartEntity::new,
                      EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 0.7F)));

  public static final RegistryObject<EntityType<TunnelBoreEntity>> TUNNEL_BORE =
      ENTITY_TYPES.register("tunnel_bore",
          () -> create("tunnel_bore",
              EntityType.Builder
                  .<TunnelBoreEntity>of(TunnelBoreEntity::new, EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(TunnelBoreEntity.LENGTH, TunnelBoreEntity.HEIGHT)));

  public static final RegistryObject<EntityType<CreativeLocomotiveEntity>> CREATIVE_LOCOMOTIVE =
      ENTITY_TYPES.register("creative_locomotive",
          () -> create("creative_locomotive",
              EntityType.Builder
                  .<CreativeLocomotiveEntity>of(CreativeLocomotiveEntity::new,
                      EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<ElectricLocomotiveEntity>> ELECTRIC_LOCOMOTIVE =
      ENTITY_TYPES.register("electric_locomotive",
          () -> create("electric_locomotive",
              EntityType.Builder
                  .<ElectricLocomotiveEntity>of(ElectricLocomotiveEntity::new,
                      EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<SteamLocomotiveEntity>> STEAM_LOCOMOTIVE =
      ENTITY_TYPES.register("steam_locomotive",
          () -> create("steam_locomotive",
              EntityType.Builder
                  .<SteamLocomotiveEntity>of(SteamLocomotiveEntity::new,
                      EntityClassification.MISC)
                  .clientTrackingRange(256)
                  .updateInterval(2)
                  .sized(0.98F, 1F)));

  public static final RegistryObject<EntityType<FirestoneItemEntity>> FIRESTONE =
      ENTITY_TYPES.register("firestone",
          () -> create("firestone", EntityType.Builder.<FirestoneItemEntity>of(
              FirestoneItemEntity::new, EntityClassification.MISC)
              .setTrackingRange(64)
              .setUpdateInterval(20)));

  private static <T extends Entity> EntityType<T> create(String registryName,
      EntityType.Builder<T> builder) {
    return builder.build(new ResourceLocation(Railcraft.ID, registryName).toString());
  }
}
