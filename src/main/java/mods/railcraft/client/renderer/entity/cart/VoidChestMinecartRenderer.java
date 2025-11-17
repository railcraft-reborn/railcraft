package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.world.entity.vehicle.VoidChestMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class VoidChestMinecartRenderer extends ContentsMinecartRenderer<VoidChestMinecart> {

  private final LowSidesMinecartModel<VoidChestMinecart> bodyModel;
  private final LowSidesMinecartModel<VoidChestMinecart> snowModel;

  public VoidChestMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(ModelLayers.MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.MINECART_SNOW));
  }

  @Override
  protected EntityModel<? super VoidChestMinecart> getBodyModel(VoidChestMinecart cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<? super VoidChestMinecart> getSnowModel(VoidChestMinecart cart) {
    return this.snowModel;
  }
}
