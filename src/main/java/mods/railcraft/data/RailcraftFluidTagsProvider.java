package mods.railcraft.data;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftFluidTagsProvider extends FluidTagsProvider {

  public RailcraftFluidTagsProvider(DataGenerator packOutput, ExistingFileHelper fileHelper) {
    super(packOutput, RailcraftConstants.ID, fileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(RailcraftTags.Fluids.STEAM)
        .add(RailcraftFluids.STEAM.get());
    this.tag(Tags.Fluids.GASEOUS)
        .add(RailcraftFluids.STEAM.get());
    this.tag(RailcraftTags.Fluids.CREOSOTE)
        .add(RailcraftFluids.CREOSOTE.get());
  }
}
