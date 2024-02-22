package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.world.entity.vehicle.WorldSpikeMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class WorldSpikeMinecartRenderer extends ContentsMinecartRenderer<WorldSpikeMinecart> {

  private final LowSidesMinecartModel<WorldSpikeMinecart> bodyModel;
  private final LowSidesMinecartModel<WorldSpikeMinecart> snowModel;

  public WorldSpikeMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected EntityModel<WorldSpikeMinecart> getBodyModel(WorldSpikeMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<WorldSpikeMinecart> getSnowModel(WorldSpikeMinecart cart) {
    return this.snowModel;
  }
}
