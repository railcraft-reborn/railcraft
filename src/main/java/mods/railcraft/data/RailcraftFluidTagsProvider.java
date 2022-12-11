package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.Railcraft;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftFluidTagsProvider extends FluidTagsProvider {

  public RailcraftFluidTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper fileHelper) {
    super(packOutput, lookupProvider, Railcraft.ID, fileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(RailcraftTags.Fluids.STEAM)
        .add(RailcraftFluids.STEAM.get());
    //https://forge.gemwire.uk/wiki/User:ChampionAsh5357/Sandbox/Fluids_API#Gaseous_Fluids
    this.tag(RailcraftTags.Fluids.GASEOUS)
        .add(RailcraftFluids.STEAM.get());
  }

  @Override
  public String getName() {
    return "Railcraft Fluid Tags";
  }
}
