package mods.railcraft.client.renderer.blockentity.state;

import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.core.Direction;

public class DualSignalRenderState extends AbstractSignalRenderState {

  public Direction direction;
  public SignalAspect primarySignalAspect;
  public SignalAspect secondarySignalAspect;
}
