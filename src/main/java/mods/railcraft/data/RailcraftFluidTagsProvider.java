package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftFluidTagsProvider extends FluidTagsProvider {

  public RailcraftFluidTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper fileHelper) {
    super(packOutput, lookupProvider, RailcraftConstants.ID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RailcraftTags.Fluids.STEAM)
        .add(RailcraftFluids.STEAM.get());
    this.tag(Tags.Fluids.GASEOUS)
        .add(RailcraftFluids.STEAM.get());
    this.tag(RailcraftTags.Fluids.CREOSOTE)
        .add(RailcraftFluids.CREOSOTE.get());
  }
}
