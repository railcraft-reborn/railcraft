package mods.railcraft.data;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.ai.village.poi.RailcraftPoiTypes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.PoiTypeTagsProvider;
import net.minecraft.tags.PoiTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftPoiTypeTagsProvider extends PoiTypeTagsProvider {

  public RailcraftPoiTypeTagsProvider(DataGenerator dataGenerator,
                                      ExistingFileHelper existingFileHelper) {
    super(dataGenerator, RailcraftConstants.ID, existingFileHelper);
  }

  @Override
  protected void addTags() {
    this.tag(PoiTypeTags.ACQUIRABLE_JOB_SITE)
        .addOptional(RailcraftPoiTypes.MANUAL_ROLLING_MACHINE_POI.getId())
        .addOptional(RailcraftPoiTypes.POWERED_ROLLING_MACHINE_POI.getId());
  }
}
