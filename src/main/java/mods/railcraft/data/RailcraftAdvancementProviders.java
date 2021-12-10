package mods.railcraft.data;

import java.util.function.Consumer;
import mods.railcraft.data.advancements.CartAdvancements;
import mods.railcraft.data.advancements.TrackAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraftforge.common.data.ExistingFileHelper;


public class RailcraftAdvancementProviders extends AdvancementProvider {

  public RailcraftAdvancementProviders(DataGenerator dataGenerator, ExistingFileHelper fileHelper) {
    super(dataGenerator, fileHelper);
  }

  @Override
  protected void registerAdvancements(Consumer<Advancement> consumer,
      ExistingFileHelper fileHelper) {
    new CartAdvancements().accept(consumer);
    new TrackAdvancements().accept(consumer);
  }

  @Override
  public String getName() {
    return "Railcraft Advancements";
  }
}
