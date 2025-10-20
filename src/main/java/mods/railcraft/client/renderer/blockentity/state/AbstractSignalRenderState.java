package mods.railcraft.client.renderer.blockentity.state;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class AbstractSignalRenderState extends BlockEntityRenderState {

  public Optional<Component> customName;
  @Nullable
  public Level level;
}
