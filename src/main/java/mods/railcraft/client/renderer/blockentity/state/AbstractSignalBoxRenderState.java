package mods.railcraft.client.renderer.blockentity.state;

import java.util.HashMap;
import java.util.Optional;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class AbstractSignalBoxRenderState extends BlockEntityRenderState {

  public Optional<Component> name;
  public HashMap<Direction, Boolean> directionConnections = new HashMap<>();
  public HashMap<Direction, SignalAspect> directionSignalAspects = new HashMap<>();
}
