package mods.railcraft.client;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;

class RenderLayers {

  static void register() {
    // TODO: Set these values from JSON
    ItemBlockRenderTypes.setRenderLayer(RailcraftBlocks.FIRESTONE.get(), RenderType.cutoutMipped());


    cutout(RailcraftBlocks.ABANDONED_CONTROL_TRACK);
    cutout(RailcraftBlocks.ABANDONED_GATED_TRACK);
    cutout(RailcraftBlocks.ABANDONED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.ABANDONED_COUPLER_TRACK);
    cutout(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.ABANDONED_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_CONTROL_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_GATED_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_COUPLER_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.IRON_CONTROL_TRACK);
    cutout(RailcraftBlocks.IRON_GATED_TRACK);
    cutout(RailcraftBlocks.IRON_DETECTOR_TRACK);
    cutout(RailcraftBlocks.IRON_COUPLER_TRACK);
    cutout(RailcraftBlocks.IRON_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.IRON_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.REINFORCED_CONTROL_TRACK);
    cutout(RailcraftBlocks.REINFORCED_GATED_TRACK);
    cutout(RailcraftBlocks.REINFORCED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.REINFORCED_COUPLER_TRACK);
    cutout(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.REINFORCED_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_GATED_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_LOCOMOTIVE_TRACK);
    cutout(RailcraftBlocks.FORCE_TRACK_EMITTER);
    cutout(RailcraftBlocks.BLOCK_SIGNAL);
    cutout(RailcraftBlocks.DISTANT_SIGNAL);
    cutout(RailcraftBlocks.TOKEN_SIGNAL);
    cutout(RailcraftBlocks.DUAL_BLOCK_SIGNAL);
    cutout(RailcraftBlocks.DUAL_DISTANT_SIGNAL);
    cutout(RailcraftBlocks.DUAL_TOKEN_SIGNAL);
  }

  private static void cutout(Supplier<? extends Block> block) {
    cutout(block.get());
  }

  private static void cutout(Block block) {
    ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
  }
}
