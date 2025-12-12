package mods.railcraft.client.renderer.blockentity;

import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.resources.Identifier;

public class SignalSequencerBoxRenderer extends AbstractSignalBoxRenderer {

  private static final Identifier TEXTURE_LOCATION =
      RailcraftConstants.id("entity/signal_box/signal_sequencer_box");

  @Override
  protected Identifier getTopTextureIdentifier() {
    return TEXTURE_LOCATION;
  }
}
