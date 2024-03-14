package mods.railcraft.data;

import java.util.concurrent.CompletableFuture;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.client.renderer.texture.atlas.sources.DirectoryLister;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SpriteSourceProvider;

public class RailcraftSpriteSourceProvider extends SpriteSourceProvider {
  public RailcraftSpriteSourceProvider(PackOutput output,
      CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper) {
    super(output, lookupProvider, RailcraftConstants.ID, fileHelper);
  }

  @Override
  protected void gather() {
    atlas(SpriteSourceProvider.BLOCKS_ATLAS)
        .addSource(new DirectoryLister("entity/signal_aspect", "entity/signal_aspect/"))
        .addSource(new DirectoryLister("entity/signal_box_aspect", "entity/signal_box_aspect/"))
        .addSource(new DirectoryLister("entity/signal_box", "entity/signal_box/"))
        .addSource(new DirectoryLister("entity/fluid_manipulator", "entity/fluid_manipulator/"))
        .addSource(new DirectoryLister("entity/fluid_loader", "entity/fluid_loader/"))
        .addSource(new DirectoryLister("entity/minecart", "entity/minecart/"));
  }
}
