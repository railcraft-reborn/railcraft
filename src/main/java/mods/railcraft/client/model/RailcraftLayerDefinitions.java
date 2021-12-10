package mods.railcraft.client.model;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

public class RailcraftLayerDefinitions {

  private static final CubeDeformation SNOW_DEFORMATION = new CubeDeformation(0.125F);

  public static void createRoots(
      BiConsumer<ModelLayerLocation, Supplier<LayerDefinition>> consumer) {
    consumer.accept(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE,
        () -> ElectricLocomotiveModel.createBodyLayer(CubeDeformation.NONE));
    consumer.accept(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_SNOW,
        () -> ElectricLocomotiveModel.createBodyLayer(SNOW_DEFORMATION));

    consumer.accept(RailcraftModelLayers.STEAM_LOCOMOTIVE,
        () -> SteamLocomotiveModel.createBodyLayer(CubeDeformation.NONE));
    consumer.accept(RailcraftModelLayers.STEAM_LOCOMOTIVE_SNOW,
        () -> SteamLocomotiveModel.createBodyLayer(SNOW_DEFORMATION));

    consumer.accept(RailcraftModelLayers.LOW_SIDES_MINECART,
        () -> LowSidesMinecartModel.createBodyLayer(CubeDeformation.NONE));
    consumer.accept(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW,
        () -> LowSidesMinecartModel.createBodyLayer(SNOW_DEFORMATION));

    consumer.accept(RailcraftModelLayers.ELECTRIC_LOCOMOTIVE_LAMP,
        ElectricLocomotiveLampModel::createBodyLayer);

    consumer.accept(RailcraftModelLayers.MAINTENANCE_LAMP,
        MaintenanceLampModel::createBodyLayer);
    consumer.accept(RailcraftModelLayers.MAINTENANCE,
        MaintenanceModel::createBodyLayer);

    consumer.accept(RailcraftModelLayers.TUNNEL_BORE, TunnelBoreModel::createBodyLayer);

    consumer.accept(RailcraftModelLayers.MINECART_SNOW,
        () -> DeformableMinecartModel.createBodyLayer(SNOW_DEFORMATION));

    consumer.accept(RailcraftModelLayers.CUBE, CubeModel::createBodyLayer);
  }
}
