package mods.railcraft.client.renderer.entity.state;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.TunnelBoreHead;

public class TunnelBoreRendererState extends RailcraftMinecartRenderState {

  @Nullable
  public TunnelBoreHead head;
  public float rotationAngle;
  public boolean isMinecartPowered;
}
