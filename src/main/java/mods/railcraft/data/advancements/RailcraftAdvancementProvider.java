package mods.railcraft.data.advancements;

import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class RailcraftAdvancementProvider extends AdvancementProvider {

  public RailcraftAdvancementProvider(DataGenerator output,
                                      ExistingFileHelper existingFileHelper) {
    super(output, existingFileHelper);
  }

  @Override
  protected void registerAdvancements(Consumer<Advancement> consumer,
                                      ExistingFileHelper fileHelper) {
    RailcraftCartAdvancements.registerAdvancements(consumer, fileHelper);
    RailcraftTrackAdvancements.registerAdvancements(consumer, fileHelper);
  }
}
