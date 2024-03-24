package mods.railcraft.advancements;

import java.util.function.Supplier;
import mods.railcraft.util.registration.DeferredCriterionTrigger;
import mods.railcraft.util.registration.RailcraftDeferredRegister;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;

public class RailcraftCriteriaTriggers {

  private static final RailcraftDeferredRegister<CriterionTrigger<?>> deferredRegister =
      new RailcraftDeferredRegister<>(Registries.TRIGGER_TYPE, DeferredCriterionTrigger::new);

  public static void register(IEventBus modEventBus) {
    deferredRegister.register(modEventBus);
  }

  public static final DeferredCriterionTrigger<CartLinkingTrigger.TriggerInstance, CartLinkingTrigger> CART_LINK =
      register("cart_linking", CartLinkingTrigger::new);
  public static final DeferredCriterionTrigger<MultiBlockFormedTrigger.TriggerInstance, MultiBlockFormedTrigger> MULTIBLOCK_FORM =
      register("multiblock_formed", MultiBlockFormedTrigger::new);
  public static final DeferredCriterionTrigger<BedCartSleepTrigger.TriggerInstance, BedCartSleepTrigger> BED_CART_SLEEP =
      register("bed_cart_sleep", BedCartSleepTrigger::new);
  public static final DeferredCriterionTrigger<JukeboxCartPlayMusicTrigger.TriggerInstance, JukeboxCartPlayMusicTrigger> JUKEBOX_CART_MUSIC_PLAY =
      register("jukebox_cart_play_music", JukeboxCartPlayMusicTrigger::new);
  public static final DeferredCriterionTrigger<SurpriseTrigger.TriggerInstance, SurpriseTrigger> CART_SURPRISE_EXPLODE =
      register("surprise", SurpriseTrigger::new);
  public static final DeferredCriterionTrigger<SpikeMaulUseTrigger.TriggerInstance, SpikeMaulUseTrigger> SPIKE_MAUL_USE =
      register("spike_maul_use", SpikeMaulUseTrigger::new);
  public static final DeferredCriterionTrigger<UseTrackKitTrigger.TriggerInstance, UseTrackKitTrigger> TRACK_KIT_USE =
      register("use_track_kit", UseTrackKitTrigger::new);
  public static final DeferredCriterionTrigger<SetSeasonTrigger.TriggerInstance, SetSeasonTrigger> SEASON_SET =
      register("set_season", SetSeasonTrigger::new);
  public static final DeferredCriterionTrigger<KilledByLocomotiveTrigger.TriggerInstance, KilledByLocomotiveTrigger> KILLED_BY_LOCOMOTIVE =
      register("killed_by_locomotive", KilledByLocomotiveTrigger::new);

  private static <I extends CriterionTriggerInstance, T extends CriterionTrigger<I>> DeferredCriterionTrigger<I, T> register(String name, Supplier<T> sup) {
    return (DeferredCriterionTrigger<I, T>) deferredRegister.register(name, sup);
  }
}
