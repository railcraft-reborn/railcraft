package mods.railcraft.client.renderer.entity.cart;

import mods.railcraft.Railcraft;
import mods.railcraft.client.model.MaintenanceModel;
import mods.railcraft.client.model.SimpleTexturedModel;
import mods.railcraft.world.entity.cart.MaintenanceMinecartEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public class TrackRemoverMinecartRenderer extends MaintenanceMinecartRenderer {

  private static final MaintenanceModel CONTENTS_MODEL =
      Util.make(new MaintenanceModel(), model -> model.setTexture(
          new ResourceLocation(Railcraft.ID,
              "textures/entity/minecart/track_remover_contents.png")));

  public TrackRemoverMinecartRenderer(EntityRendererManager renderManager) {
    super(renderManager);
  }

  @Override
  protected SimpleTexturedModel getContentsModel(MaintenanceMinecartEntity cart) {
    return CONTENTS_MODEL;
  }
}
