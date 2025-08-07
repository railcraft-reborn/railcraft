package mods.railcraft.data.advancements;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;

public class RailcraftAdvancementProvider extends AdvancementProvider {

  public RailcraftAdvancementProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> registries) {
    super(output, registries, List.of(
        new RailcraftTrackAdvancements(),
        new RailcraftCartAdvancements()
    ));
  }
}
