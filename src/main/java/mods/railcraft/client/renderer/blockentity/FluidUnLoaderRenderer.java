package mods.railcraft.client.renderer.blockentity;

import mods.railcraft.client.renderer.blockentity.state.FluidManipulatorRenderState;
import mods.railcraft.world.level.block.entity.manipulator.FluidUnloaderBlockEntity;

public class FluidUnLoaderRenderer extends FluidManipulatorRenderer<FluidUnloaderBlockEntity, FluidManipulatorRenderState> {

  @Override
  public FluidManipulatorRenderState createRenderState() {
    return new FluidManipulatorRenderState();
  }
}
