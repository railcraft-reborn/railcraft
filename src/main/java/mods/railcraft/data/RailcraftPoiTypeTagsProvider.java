package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RailcraftPoiTypeTagsProvider extends PoiTypeTagsProvider {

  public RailcraftPoiTypeTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, RailcraftConstants.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
        .addOptional(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getId())
        .addOptional(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getId());
  }
}
