package mods.railcraft.data.advancements;

import java.util.List;
import net.minecraft.data.advancements.AdvancementProvider;

public class RailcraftAdvancementProvider extends AdvancementProvider {

  public RailcraftAdvancementProvider() {
    super(List.of(RailcraftTrackAdvancements::new, RailcraftCartAdvancements::new));
  }
}
