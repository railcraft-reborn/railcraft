package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.model.LowSidesMinecartModel;
import mods.railcraft.client.model.RailcraftModelLayers;
import mods.railcraft.client.renderer.entity.state.RailcraftMinecartRenderState;
import mods.railcraft.world.entity.vehicle.BedMinecart;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class BedMinecartRenderer extends ContentsMinecartRenderer<BedMinecart, RailcraftMinecartRenderState> {

  private final LowSidesMinecartModel<RailcraftMinecartRenderState> bodyModel;
  private final LowSidesMinecartModel<RailcraftMinecartRenderState> snowModel;

  public BedMinecartRenderer(EntityRendererProvider.Context context) {
    super(context);
    this.bodyModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART));
    this.snowModel = new LowSidesMinecartModel<>(
        context.bakeLayer(RailcraftModelLayers.LOW_SIDES_MINECART_SNOW));
  }

  @Override
  protected EntityModel<RailcraftMinecartRenderState> getBodyModel(RailcraftMinecartRenderState cart) {
    return this.bodyModel;
  }

  @Override
  protected EntityModel<RailcraftMinecartRenderState> getSnowModel(RailcraftMinecartRenderState cart) {
    return this.snowModel;
  }

  @Override
  public RailcraftMinecartRenderState createRenderState() {
    return new RailcraftMinecartRenderState();
  }
}
