package mods.railcraft.data;

import java.util.stream.Collectors;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;

public class RailcraftBlockLootTable extends BlockLootTables {

  @Override
  protected void addTables() {
    /* === tracks === */
    this.dropSelf(RailcraftBlocks.ABANDONED_TRACK.get());
    this.dropSelf(RailcraftBlocks.ELECTRIC_TRACK.get());
    this.dropSelf(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.dropSelf(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get());
    this.dropSelf(RailcraftBlocks.HIGH_SPEED_TRACK.get());
    this.dropSelf(RailcraftBlocks.REINFORCED_TRACK.get());
    this.dropSelf(RailcraftBlocks.STRAP_IRON_TRACK.get());
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
        .filter(block -> !(block instanceof OutfittedTrackBlock)) // No drops for outfitted track
        .collect(Collectors.toSet());
  }
}
