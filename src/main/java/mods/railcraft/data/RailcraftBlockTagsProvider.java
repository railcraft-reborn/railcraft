package mods.railcraft.data;

import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftBlockTagsProvider extends BlockTagsProvider {

  public RailcraftBlockTagsProvider(DataGenerator dataGenerator,
      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, Railcraft.ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(RailcraftTags.Blocks.SWITCH_TRACK_ACTUATOR)
        .add(RailcraftBlocks.SWITCH_TRACK_LEVER.get());
    this.tag(BlockTags.RAILS)
        .add(RailcraftBlocks.REINFORCED_FLEX_TRACK.get(),
            RailcraftBlocks.ABANDONED_FLEX_TRACK.get(),
            RailcraftBlocks.ELECTRIC_FLEX_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_FLEX_TRACK.get(),
            RailcraftBlocks.HIGH_SPEED_ELECTRIC_FLEX_TRACK.get(),
            RailcraftBlocks.STRAP_IRON_FLEX_TRACK.get(),
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
        .add(RailcraftBlocks.BLACK_METAL_POST.get(), RailcraftBlocks.RED_METAL_POST.get(),
            RailcraftBlocks.GREEN_METAL_POST.get(), RailcraftBlocks.BROWN_METAL_POST.get(),
            RailcraftBlocks.BLUE_METAL_POST.get(), RailcraftBlocks.PURPLE_METAL_POST.get(),
            RailcraftBlocks.CYAN_METAL_POST.get(), RailcraftBlocks.LIGHT_GRAY_METAL_POST.get(),
            RailcraftBlocks.GRAY_METAL_POST.get(), RailcraftBlocks.PINK_METAL_POST.get(),
            RailcraftBlocks.LIME_METAL_POST.get(), RailcraftBlocks.YELLOW_METAL_POST.get(),
            RailcraftBlocks.LIGHT_BLUE_METAL_POST.get(), RailcraftBlocks.MAGENTA_METAL_POST.get(),
            RailcraftBlocks.ORANGE_METAL_POST.get(), RailcraftBlocks.WHITE_METAL_POST.get());
  }

  @Override
  public String getName() {
    return "Railcraft Block Tags";
  }
}
