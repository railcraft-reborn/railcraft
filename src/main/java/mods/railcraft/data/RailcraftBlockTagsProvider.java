package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftBlockTagsProvider extends BlockTagsProvider {

  public RailcraftBlockTagsProvider(DataGenerator generator, ExistingFileHelper fileHelper) {
    super(generator, Railcraft.ID, fileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(RailcraftTags.Blocks.BALLAST)
        .addTag(Tags.Blocks.GRAVEL);
    this.tag(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        .add(RailcraftBlocks.SWITCH_TRACK_LEVER.get())
        .add(RailcraftBlocks.SWITCH_TRACK_MOTOR.get());
    this.tag(BlockTags.RAILS)
        .add(RailcraftBlocks.FORCE_TRACK.get(),
            RailcraftBlocks.ABANDONED_TRACK.get(),
            RailcraftBlocks.ABANDONED_LOCKING_TRACK.get(),
            RailcraftBlocks.ABANDONED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.ABANDONED_BOOSTER_TRACK.get(),
            RailcraftBlocks.ABANDONED_CONTROL_TRACK.get(),
            RailcraftBlocks.ABANDONED_GATED_TRACK.get(),
            RailcraftBlocks.ABANDONED_DETECTOR_TRACK.get(),
            RailcraftBlocks.ELECTRIC_TRACK.get(),
            RailcraftBlocks.ELECTRIC_LOCKING_TRACK.get(),
            RailcraftBlocks.ELECTRIC_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.ELECTRIC_BOOSTER_TRACK.get(),
            RailcraftBlocks.ELECTRIC_CONTROL_TRACK.get(),
            RailcraftBlocks.ELECTRIC_GATED_TRACK.get(),
            RailcraftBlocks.ELECTRIC_DETECTOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_TRANSITION_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_LOCKING_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_BOOSTER_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_DETECTOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRANSITION_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_LOCKING_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_BOOSTER_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_DETECTOR_TRACK.get(),
            RailcraftBlocks.IRON_LOCKING_TRACK.get(),
            RailcraftBlocks.IRON_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.IRON_BOOSTER_TRACK.get(),
            RailcraftBlocks.IRON_CONTROL_TRACK.get(),
            RailcraftBlocks.IRON_GATED_TRACK.get(),
            RailcraftBlocks.IRON_DETECTOR_TRACK.get(),
            RailcraftBlocks.REINFORCED_TRACK.get(),
            RailcraftBlocks.REINFORCED_LOCKING_TRACK.get(),
            RailcraftBlocks.REINFORCED_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.REINFORCED_BOOSTER_TRACK.get(),
            RailcraftBlocks.REINFORCED_CONTROL_TRACK.get(),
            RailcraftBlocks.REINFORCED_GATED_TRACK.get(),
            RailcraftBlocks.REINFORCED_DETECTOR_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_LOCKING_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_ACTIVATOR_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_BOOSTER_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_CONTROL_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_GATED_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_DETECTOR_TRACK.get(),
            RailcraftBlocks.TURNOUT_TRACK.get(),
            RailcraftBlocks.WYE_TRACK.get());
    this.tag(BlockTags.CLIMBABLE).add(RailcraftBlocks.ELEVATOR_TRACK.get());
    this.tag(RailcraftTags.Blocks.ASPECT_EMITTER)
        .add(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(),
            RailcraftBlocks.SIGNAL_RECEIVER_BOX.get(),
            RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get(),
            RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.tag(RailcraftTags.Blocks.ASPECT_RECEIVER)
        .add(RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(),
            RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get(),
            RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get(),
            RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get());
    this.tag(RailcraftTags.Blocks.POST)
        .add(RailcraftBlocks.BLACK_POST.get(), RailcraftBlocks.RED_POST.get(),
            RailcraftBlocks.GREEN_POST.get(), RailcraftBlocks.BROWN_POST.get(),
            RailcraftBlocks.BLUE_POST.get(), RailcraftBlocks.PURPLE_POST.get(),
            RailcraftBlocks.CYAN_POST.get(), RailcraftBlocks.LIGHT_GRAY_POST.get(),
            RailcraftBlocks.GRAY_POST.get(), RailcraftBlocks.PINK_POST.get(),
            RailcraftBlocks.LIME_POST.get(), RailcraftBlocks.YELLOW_POST.get(),
            RailcraftBlocks.LIGHT_BLUE_POST.get(), RailcraftBlocks.MAGENTA_POST.get(),
            RailcraftBlocks.ORANGE_POST.get(), RailcraftBlocks.WHITE_POST.get());
    this.tag(RailcraftTags.Blocks.SIGNAL)
        .add(RailcraftBlocks.BLOCK_SIGNAL.get(), RailcraftBlocks.DISTANT_SIGNAL.get(),
            RailcraftBlocks.TOKEN_SIGNAL.get(), RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(),
            RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(), RailcraftBlocks.DUAL_TOKEN_SIGNAL.get());
    this.tag(RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR)
        .add(RailcraftBlocks.SWITCH_TRACK_LEVER.get(), RailcraftBlocks.SWITCH_TRACK_MOTOR.get(),
            RailcraftBlocks.ANALOG_SIGNAL_CONTROLLER_BOX.get(),
            RailcraftBlocks.SIGNAL_SEQUENCER_BOX.get(), RailcraftBlocks.SIGNAL_CAPACITOR_BOX.get(),
            RailcraftBlocks.SIGNAL_INTERLOCK_BOX.get(),
            RailcraftBlocks.BLOCK_SIGNAL_RELAY_BOX.get(), RailcraftBlocks.SIGNAL_RECEIVER_BOX.get(),
            RailcraftBlocks.SIGNAL_CONTROLLER_BOX.get(), RailcraftBlocks.DUAL_BLOCK_SIGNAL.get(),
            RailcraftBlocks.DUAL_DISTANT_SIGNAL.get(), RailcraftBlocks.DUAL_TOKEN_SIGNAL.get(),
            RailcraftBlocks.BLOCK_SIGNAL.get(), RailcraftBlocks.DISTANT_SIGNAL.get(),
            RailcraftBlocks.TOKEN_SIGNAL.get(), RailcraftBlocks.ELEVATOR_TRACK.get())
        .addTag(BlockTags.RAILS);

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .add(RailcraftBlocks.FIRESTONE.get(), RailcraftBlocks.COKE_OVEN_BRICKS.get());

    this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
        .add(RailcraftBlocks.FIRESTONE.get());

    this.tag(BlockTags.ANVIL)
        .add(RailcraftBlocks.STEEL_ANVIL.get(), RailcraftBlocks.CHIPPED_STEEL_ANVIL.get(),
            RailcraftBlocks.DAMAGED_STEEL_ANVIL.get());
  }

  @Override
  public String getName() {
    return "Railcraft Block Tags";
  }
}
