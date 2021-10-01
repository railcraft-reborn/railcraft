package mods.railcraft.client.renderer.blockentity;

import mods.railcraft.Railcraft;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;

public class BlockSignalRelayBoxRenderer extends AbstractSignalBoxRenderer {

  public static final ResourceLocation TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "entity/signal_box/block_signal_relay_box.png");

  public BlockSignalRelayBoxRenderer(TileEntityRendererDispatcher dispatcher) {
    super(dispatcher);
  }

  @Override
  protected ResourceLocation getTopTextureLocation() {
    return TEXTURE_LOCATION;
  }
}
