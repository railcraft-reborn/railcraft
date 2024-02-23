package mods.railcraft.client.renderer.blockentity;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class RailcraftBlockEntityRenderers {

  public static void register(EntityRenderersEvent.RegisterRenderers event) {
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.IRON_TANK.get(),
        supply(TankRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.STEEL_TANK.get(),
        supply(TankRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.BLOCK_SIGNAL.get(),
        supply(SignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.DISTANT_SIGNAL.get(),
        supply(SignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.TOKEN_SIGNAL.get(),
        supply(SignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.DUAL_BLOCK_SIGNAL.get(),
        supply(DualSignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.DUAL_DISTANT_SIGNAL.get(),
        supply(DualSignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.DUAL_TOKEN_SIGNAL.get(),
        supply(DualSignalRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get(),
        supply(SignalControllerBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.TOKEN_SIGNAL_BOX.get(),
        supply(TokenSignalBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get(),
        supply(SignalCapacitorBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_RECEIVER_BOX.get(),
        supply(SignalReceiverBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.BLOCK_SIGNAL_RELAY_BOX.get(),
        supply(SignalBlockRelayBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
        supply(AnalogSignalControllerBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_SEQUENCER_BOX.get(),
        supply(SignalSequencerBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.SIGNAL_INTERLOCK_BOX.get(),
        supply(SignalInterlockBoxRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.FLUID_LOADER.get(),
        supply(FluidLoaderRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.FLUID_UNLOADER.get(),
        supply(FluidManipulatorRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.STEAM_TURBINE.get(),
        supply(SteamTurbineRenderer::new));
    event.registerBlockEntityRenderer(RailcraftBlockEntityTypes.RITUAL.get(),
        RitualBlockRenderer::new);
  }

  private static <T extends BlockEntity> BlockEntityRendererProvider<T> supply(
      Supplier<BlockEntityRenderer<T>> supplier) {
    return __ -> supplier.get();
  }
}
