package mods.railcraft.client.renderer.blockentity.state;

import java.util.HashMap;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.signal.SignalAspect;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class AbstractSignalBoxRenderState extends BlockEntityRenderState {

  public Optional<Component> customName;
  public HashMap<Direction, Boolean> directionConnections = new HashMap<>();
  public HashMap<Direction, SignalAspect> directionSignalAspects = new HashMap<>();
  @Nullable
  public Level level;
}
