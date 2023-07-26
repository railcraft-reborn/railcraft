package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.Railcraft;
import mods.railcraft.world.damagesource.RailcraftDamageType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftDamageTypeTagsProvider extends TagsProvider<DamageType> {

  public RailcraftDamageTypeTagsProvider(PackOutput packOutput,
      CompletableFuture<HolderLookup.Provider> lookupProvider,
      ExistingFileHelper existingFileHelper) {
    super(packOutput, Registries.DAMAGE_TYPE, lookupProvider, Railcraft.ID, existingFileHelper);
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
