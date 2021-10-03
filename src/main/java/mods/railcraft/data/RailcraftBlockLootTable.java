package mods.railcraft.data;

import java.util.stream.Collectors;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraftforge.fml.RegistryObject;

public class RailcraftBlockLootTable extends BlockLootTables {

  @Override
  protected void addTables() {
    /* === tracks === */
    this.dropSelf(RailcraftBlocks.ABANDONED_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.ELECTRIC_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.dropSelf(RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.REINFORCED_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get());
    this.dropSelf(RailcraftBlocks.TURNOUT_TRACK.get());
    this.dropSelf(RailcraftBlocks.WYE_TRACK.get());
    this.add(RailcraftBlocks.FORCE_TRACK.get(), noDrop());

    /* === signals === */
    this.dropSelf(RailcraftBlocks.SIGNAL.get());
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
    this.dropSelf(RailcraftBlocks.TOKEN_SIGNAL.get());

    /* === machines === */
    this.dropSelf(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());

    /* === misc === */
    this.dropWhenSilkTouch(RailcraftBlocks.FIRESTONE.get());
    // TODO: implement this ritual thingy
    this.add(RailcraftBlocks.RITUAL.get(), noDrop());
  }

  @Override
  protected Iterable<Block> getKnownBlocks() {
    return RailcraftBlocks.BLOCKS.getEntries().stream()
        .map(RegistryObject::get)
        .collect(Collectors.toSet());
  }
}
