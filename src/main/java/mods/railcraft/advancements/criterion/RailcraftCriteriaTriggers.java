package mods.railcraft.advancements.criterion;

import net.minecraft.advancements.CriteriaTriggers;

public class RailcraftCriteriaTriggers {

  public static final CartLinkingTrigger CART_LINK = new CartLinkingTrigger();
  public static final MultiBlockFormedTrigger MULTIBLOCK_FORM = new MultiBlockFormedTrigger();
  public static final BedCartSleepTrigger BED_CART_SLEEP = new BedCartSleepTrigger();
  public static final JukeboxCartPlayMusicTrigger JUKEBOX_CART_MUSIC_PLAY =
      new JukeboxCartPlayMusicTrigger();
  public static final SurpriseTrigger CART_SURPRISE_EXPLODE = new SurpriseTrigger();
  public static final SpikeMaulUseTrigger SPIKE_MAUL_USE = new SpikeMaulUseTrigger();
  public static final UseTrackKitTrigger TRACK_KIT_USE = new UseTrackKitTrigger();
  public static final SetSeasonTrigger SEASON_SET = new SetSeasonTrigger();
  public static final KilledByLocomotiveTrigger KILLED_BY_LOCOMOTIVE =
      new KilledByLocomotiveTrigger();

  /**
   * Notice: This <b>must</b> be registered during <code>FMLCommonSetupEvent</code>.
   */
  public static void register() {
    CriteriaTriggers.register(CART_LINK);
    CriteriaTriggers.register(MULTIBLOCK_FORM);
    CriteriaTriggers.register(BED_CART_SLEEP);
    CriteriaTriggers.register(JUKEBOX_CART_MUSIC_PLAY);
    CriteriaTriggers.register(CART_SURPRISE_EXPLODE);
    CriteriaTriggers.register(SPIKE_MAUL_USE);
    CriteriaTriggers.register(TRACK_KIT_USE);
    CriteriaTriggers.register(SEASON_SET);
    CriteriaTriggers.register(KILLED_BY_LOCOMOTIVE);
  }

}
