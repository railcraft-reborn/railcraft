package mods.railcraft.data;

import java.util.stream.Collectors;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftBlockLoot extends BlockLoot {

  @Override
  protected void addTables() {
    this.dropSelf(RailcraftBlocks.FLUID_LOADER.get());
    this.dropSelf(RailcraftBlocks.FLUID_UNLOADER.get());
    this.dropSelf(RailcraftBlocks.ITEM_LOADER.get());
    this.dropSelf(RailcraftBlocks.ITEM_UNLOADER.get());
    this.dropSelf(RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
    this.dropSelf(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());
    this.dropSelf(RailcraftBlocks.STEEL_BLOCK.get());
    this.dropSelf(RailcraftBlocks.STEEL_ANVIL.get());
    this.dropSelf(RailcraftBlocks.CHIPPED_STEEL_ANVIL.get());
    this.dropSelf(RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());
    this.dropSelf(RailcraftBlocks.FEED_STATION.get());

    /* === tracks === */
    this.dropSelf(RailcraftBlocks.ABANDONED_TRACK.get());
    this.dropOther(RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ABANDONED_TURNOUT_TRACK.get(),
        RailcraftBlocks.ABANDONED_TRACK.get());
    this.dropOther(RailcraftBlocks.ABANDONED_WYE_TRACK.get(),
        RailcraftBlocks.ABANDONED_TRACK.get());
    this.dropOther(RailcraftBlocks.ABANDONED_JUNCTION_TRACK.get(),
        RailcraftBlocks.ABANDONED_TRACK.get());

    this.dropSelf(RailcraftBlocks.ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.ELECTRIC_TRACK.get());

    this.dropSelf(RailcraftBlocks.HIGH_SPEED_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_TRACK.get());

    this.dropSelf(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
        RailcraftItems.TRANSITION_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get());
    this.dropOther(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK.get(),
        RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get());

    this.dropOther(RailcraftBlocks.IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.IRON_TURNOUT_TRACK.get(), Blocks.RAIL);
    this.dropOther(RailcraftBlocks.IRON_WYE_TRACK.get(), Blocks.RAIL);
    this.dropOther(RailcraftBlocks.IRON_JUNCTION_TRACK.get(), Blocks.RAIL);

    this.dropSelf(RailcraftBlocks.REINFORCED_TRACK.get());
    this.dropOther(RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.REINFORCED_TURNOUT_TRACK.get(),
        RailcraftBlocks.REINFORCED_TRACK.get());
    this.dropOther(RailcraftBlocks.REINFORCED_WYE_TRACK.get(),
        RailcraftBlocks.REINFORCED_TRACK.get());
    this.dropOther(RailcraftBlocks.REINFORCED_JUNCTION_TRACK.get(),
        RailcraftBlocks.REINFORCED_TRACK.get());

    this.dropSelf(RailcraftBlocks.STRAP_IRON_TRACK.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
        RailcraftItems.LOCKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_BUFFER_STOP_TRACK.get(),
        RailcraftItems.BUFFER_STOP_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
        RailcraftItems.ACTIVATOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
        RailcraftItems.BOOSTER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
        RailcraftItems.CONTROL_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
        RailcraftItems.GATED_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
        RailcraftItems.DETECTOR_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_COUPLER_TRACK.get(),
        RailcraftItems.COUPLER_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_EMBARKING_TRACK.get(),
        RailcraftItems.EMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_DISEMBARKING_TRACK.get(),
        RailcraftItems.DISEMBARKING_TRACK_KIT.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TRACK.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_WYE_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TRACK.get());
    this.dropOther(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK.get(),
        RailcraftBlocks.STRAP_IRON_TRACK.get());

    this.dropSelf(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.add(RailcraftBlocks.FORCE_TRACK.get(), noDrop());

    /* === signals === */
    this.dropSelf(RailcraftBlocks.BLOCK_SIGNAL.get());
    this.dropSelf(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get());
    this.dropSelf(RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get());
    this.dropSelf(RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get());
    this.dropSelf(RailcraftBlocks.SIGNAL_RECEIVER_BOX.get());
    this.dropSelf(RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());

    this.dropSelf(RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get());
    this.dropSelf(RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get());
    this.dropSelf(RailcraftBlocks.DISTANT_SIGNAL.get());
    this.dropSelf(RailcraftBlocks.DUAL_BLOCK_SIGNAL.get());
    this.dropSelf(RailcraftBlocks.DUAL_DISTANT_SIGNAL.get());
    this.dropSelf(RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.dropSelf(RailcraftBlocks.FORCE_TRACK_EMITTER.get());
    this.dropSelf(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.dropSelf(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());
    this.dropSelf(RailcraftBlocks.TOKEN_SIGNAL.get());

    /* === machines === */
    this.dropSelf(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
    this.dropSelf(RailcraftBlocks.COKE_OVEN_BRICKS.get());
    this.dropSelf(RailcraftBlocks.BLAST_FURNACE_BRICKS.get());

    /* === misc === */
    this.dropSelf(RailcraftBlocks.FIRESTONE.get());
    // TODO: implement this ritual thingy
    this.add(RailcraftBlocks.RITUAL.get(), noDrop());

    this.dropSelf(RailcraftBlocks.BLACK_POST.get());
    this.dropSelf(RailcraftBlocks.RED_POST.get());
    this.dropSelf(RailcraftBlocks.GREEN_POST.get());
    this.dropSelf(RailcraftBlocks.BROWN_POST.get());
    this.dropSelf(RailcraftBlocks.BLUE_POST.get());
    this.dropSelf(RailcraftBlocks.PURPLE_POST.get());
    this.dropSelf(RailcraftBlocks.CYAN_POST.get());
    this.dropSelf(RailcraftBlocks.LIGHT_GRAY_POST.get());
    this.dropSelf(RailcraftBlocks.GRAY_POST.get());
    this.dropSelf(RailcraftBlocks.PINK_POST.get());
    this.dropSelf(RailcraftBlocks.LIME_POST.get());
    this.dropSelf(RailcraftBlocks.YELLOW_POST.get());
    this.dropSelf(RailcraftBlocks.LIGHT_BLUE_POST.get());
    this.dropSelf(RailcraftBlocks.MAGENTA_POST.get());
    this.dropSelf(RailcraftBlocks.ORANGE_POST.get());
    this.dropSelf(RailcraftBlocks.WHITE_POST.get());
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return RailcraftBlocks.BLOCKS.getEntries().stream()
        .map(RegistryObject::get)
        .collect(Collectors.toSet());
  }
}
