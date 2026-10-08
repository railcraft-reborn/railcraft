package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.world.entity.vehicle.BedMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BedMinecartRenderer extends ContentsMinecartRenderer<BedMinecart> {

  private final LowSidesMinecartModel<BedMinecart> bodyModel;
  private final LowSidesMinecartModel<BedMinecart> snowModel;

  public BedMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected EntityModel<? super BedMinecart> getBodyModel(BedMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<? super BedMinecart> getSnowModel(BedMinecart cart) {
    return this.snowModel;
  }
}
