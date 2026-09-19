package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.client.renderer.entity.state.LocomotiveRenderState;
import mods.railcraft.season.Seasons;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.item.DyeColor;

public abstract class LocomotiveRenderer<T extends Locomotive, S extends LocomotiveRenderState>
    extends CustomMinecartRenderer<T, S> {

  public LocomotiveRenderer(EntityRendererProvider.Context context) {
    super(context);
  }

  protected int getPrimaryColor(S loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColor()
        : loco.primaryColor;
  }

  protected int getSecondaryColor(S loco) {
    return Seasons.isGhostTrain(loco)
        ? DyeColor.LIGHT_GRAY.getTextureDiffuseColor()
        : loco.secondaryColor;
  }

  @Override
  public void extractRenderState(T entity, S reusedState, float partialTick) {
    super.extractRenderState(entity, reusedState, partialTick);
    reusedState.primaryColor = entity.getPrimaryColor();
    reusedState.secondaryColor = entity.getSecondaryColor();
    reusedState.mode = entity.getMode();
    reusedState.destination = entity.getDestination();
    reusedState.season = entity.getSeason();
  }
}
