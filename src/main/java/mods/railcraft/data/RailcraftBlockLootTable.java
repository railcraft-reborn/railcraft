package mods.railcraft.data;

import java.util.stream.Collectors;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;

public class RailcraftBlockLootTable extends BlockLootTables {

  @Override
  protected void addTables() {
    this.dropSelf(RailcraftBlocks.ITEM_LOADER.get());
    this.dropSelf(RailcraftBlocks.ITEM_UNLOADER.get());
    this.dropSelf(RailcraftBlocks.ADVANCED_ITEM_LOADER.get());
    this.dropSelf(RailcraftBlocks.ADVANCED_ITEM_UNLOADER.get());

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

    this.dropSelf(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.add(RailcraftBlocks.TURNOUT_TRACK.get(), noDrop());
    this.add(RailcraftBlocks.WYE_TRACK.get(), noDrop());
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
