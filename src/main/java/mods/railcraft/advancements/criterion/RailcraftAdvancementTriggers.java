package mods.railcraft.advancements.criterion;

import mods.railcraft.api.core.RailcraftConstantsAPI;
import net.minecraft.advancements.criterion.ItemPredicate;

/**
 * TODO: move this to RailcraftCriteriaTriggers.
 * (Yes, it is NOT an achivement trigger, it's a Generic trigger)
 * @deprecated use {@link RailcraftCriteriaTriggers}
 */
@Deprecated
public final class RailcraftAdvancementTriggers {

  public static RailcraftAdvancementTriggers getInstance() {
    return Holder.INSTANCE;
  }

  public void register() {
    ItemPredicate.register(RailcraftConstantsAPI.locationOf("is_cart"),
        (json) -> new CartItemPredicate());
    ItemPredicate.register(RailcraftConstantsAPI.locationOf("is_track"),
        TrackItemPredicate.DESERIALIZER);
  }

  RailcraftAdvancementTriggers() {}

  static final class Holder {
    // Lazy init because there are a lot of triggers
    static final RailcraftAdvancementTriggers INSTANCE = new RailcraftAdvancementTriggers();

    private Holder() {}
  }
}
