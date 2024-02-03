package mods.railcraft.client.renderer.blockentity;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.resources.ResourceLocation;

public class SignalReceiverBoxRenderer extends AbstractSignalBoxRenderer {

  public static final ResourceLocation TEXTURE_LOCATION =
      RailcraftConstants.rl("entity/signal_box/signal_receiver_box");

  @Override
  protected ResourceLocation getTopTextureLocation() {
    return TEXTURE_LOCATION;
  }
}
