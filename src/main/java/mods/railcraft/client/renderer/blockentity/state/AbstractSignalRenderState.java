package mods.railcraft.client.renderer.blockentity.state;

import java.util.Optional;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntity;

public class AbstractSignalRenderState extends BlockEntityRenderState {

  public Optional<Component> customName;
  public BlockEntity blockEntity;
}
