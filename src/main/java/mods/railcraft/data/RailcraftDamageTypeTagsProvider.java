package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class RailcraftDamageTypeTagsProvider extends DamageTypeTagsProvider {

  public RailcraftDamageTypeTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, lookupProvider, RailcraftConstants.ID, existingFileHelper);
  }

  @Override
  protected void addTags(HolderLookup.Provider provider) {
    this.tag(DamageTypeTags.BYPASSES_ARMOR)
        .add(RailcraftDamageType.BORE)
        .add(RailcraftDamageType.ELECTRIC)
        .add(RailcraftDamageType.TRACK_ELECTRIC)
        .add(RailcraftDamageType.TRAIN)
        .add(RailcraftDamageType.CREOSOTE);
  }
}
