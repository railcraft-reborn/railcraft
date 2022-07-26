package mods.railcraft.client;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;

class RenderLayers {

  static void register() {
    // TODO: Set these values from JSON
    ItemBlockRenderTypes.setRenderLayer(RailcraftBlocks.FIRESTONE.get(), RenderType.cutoutMipped());

    for (var dyeColor : DyeColor.values()) {
      cutout(RailcraftBlocks.STRENGTHENED_GLASS.variantFor(dyeColor));
      cutout(RailcraftBlocks.IRON_TANK_GAUGE.variantFor(dyeColor));
      cutout(RailcraftBlocks.STEEL_TANK_GAUGE.variantFor(dyeColor));
    }

    cutout(RailcraftBlocks.FLUID_LOADER);
    cutout(RailcraftBlocks.FLUID_UNLOADER);

    cutout(RailcraftBlocks.ELEVATOR_TRACK);
    cutout(RailcraftBlocks.FORCE_TRACK);
    cutout(RailcraftBlocks.ABANDONED_TRACK);
    cutout(RailcraftBlocks.ABANDONED_LOCKING_TRACK);
    cutout(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK);
    cutout(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.ABANDONED_BOOSTER_TRACK);
    cutout(RailcraftBlocks.ABANDONED_CONTROL_TRACK);
    cutout(RailcraftBlocks.ABANDONED_GATED_TRACK);
    cutout(RailcraftBlocks.ABANDONED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.ABANDONED_COUPLER_TRACK);
    cutout(RailcraftBlocks.ABANDONED_EMBARKING_TRACK);
    cutout(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.ABANDONED_TURNOUT_TRACK);
    cutout(RailcraftBlocks.ABANDONED_WYE_TRACK);
    cutout(RailcraftBlocks.ABANDONED_JUNCTION_TRACK);
    cutout(RailcraftBlocks.ABANDONED_LAUNCHER_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_LOCKING_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_CONTROL_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_GATED_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_COUPLER_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_WYE_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK);
    cutout(RailcraftBlocks.ELECTRIC_LAUNCHER_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_WYE_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK);
    cutout(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK);
    cutout(RailcraftBlocks.IRON_LOCKING_TRACK);
    cutout(RailcraftBlocks.IRON_BUFFER_STOP_TRACK);
    cutout(RailcraftBlocks.IRON_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.IRON_BOOSTER_TRACK);
    cutout(RailcraftBlocks.IRON_CONTROL_TRACK);
    cutout(RailcraftBlocks.IRON_GATED_TRACK);
    cutout(RailcraftBlocks.IRON_DETECTOR_TRACK);
    cutout(RailcraftBlocks.IRON_COUPLER_TRACK);
    cutout(RailcraftBlocks.IRON_EMBARKING_TRACK);
    cutout(RailcraftBlocks.IRON_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.IRON_TURNOUT_TRACK);
    cutout(RailcraftBlocks.IRON_WYE_TRACK);
    cutout(RailcraftBlocks.IRON_JUNCTION_TRACK);
    cutout(RailcraftBlocks.IRON_LAUNCHER_TRACK);
    cutout(RailcraftBlocks.REINFORCED_TRACK);
    cutout(RailcraftBlocks.REINFORCED_LOCKING_TRACK);
    cutout(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK);
    cutout(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.REINFORCED_BOOSTER_TRACK);
    cutout(RailcraftBlocks.REINFORCED_CONTROL_TRACK);
    cutout(RailcraftBlocks.REINFORCED_GATED_TRACK);
    cutout(RailcraftBlocks.REINFORCED_DETECTOR_TRACK);
    cutout(RailcraftBlocks.REINFORCED_COUPLER_TRACK);
    cutout(RailcraftBlocks.REINFORCED_EMBARKING_TRACK);
    cutout(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.REINFORCED_TURNOUT_TRACK);
    cutout(RailcraftBlocks.REINFORCED_WYE_TRACK);
    cutout(RailcraftBlocks.REINFORCED_JUNCTION_TRACK);
    cutout(RailcraftBlocks.REINFORCED_LAUNCHER_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_GATED_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_WYE_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK);
    cutout(RailcraftBlocks.STRAP_IRON_LAUNCHER_TRACK);
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
