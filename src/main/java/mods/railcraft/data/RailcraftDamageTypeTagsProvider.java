package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

public class RailcraftDamageTypeTagsProvider extends DamageTypeTagsProvider {

  public RailcraftDamageTypeTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(packOutput, registries, RailcraftConstants.ID);
  }

  @Override
  protected void addTags(HolderLookup.Provider registries) {
    this.tag(DamageTypeTags.BYPASSES_ARMOR)
        .addOptional(RailcraftDamageType.BORE.location())
        .addOptional(RailcraftDamageType.ELECTRIC.location())
        .addOptional(RailcraftDamageType.TRACK_ELECTRIC.location())
        .addOptional(RailcraftDamageType.TRAIN.location())
        .addOptional(RailcraftDamageType.CREOSOTE.location());
  }
}
