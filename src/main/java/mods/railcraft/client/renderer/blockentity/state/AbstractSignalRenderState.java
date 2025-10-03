package mods.railcraft.client.renderer.blockentity.state;

import java.util.Optional;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.network.chat.Component;

public class AbstractSignalRenderState extends BlockEntityRenderState {

  public Optional<Component> customName;
}
