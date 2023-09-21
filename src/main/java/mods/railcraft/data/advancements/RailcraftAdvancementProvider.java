package mods.railcraft.data.advancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;

public class RailcraftAdvancementProvider extends ForgeAdvancementProvider {

  public RailcraftAdvancementProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
    super(output, registries, existingFileHelper, List.of(
        new RailcraftTrackAdvancements(),
        new RailcraftCartAdvancements(),
        new RailcraftAdvancements()
    ));
  }
}
