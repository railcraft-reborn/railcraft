package mods.railcraft.client.renderer.blockentity;

import mods.railcraft.Railcraft;
import net.minecraft.resources.ResourceLocation;

public class SignalCapacitorBoxRenderer extends AbstractSignalBoxRenderer {

  public static final ResourceLocation TEXTURE_LOCATION =
      Railcraft.rl("entity/signal_box/signal_capacitor_box");

  @Override
  protected ResourceLocation getTopTextureLocation() {
    return TEXTURE_LOCATION;
  }
}
