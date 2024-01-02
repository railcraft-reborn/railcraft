package mods.railcraft.advancements;

import mods.railcraft.api.core.RailcraftConstants;
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
    CriteriaTriggers.register(RailcraftConstants.rl("cart_linking").toString(), CART_LINK);
    CriteriaTriggers.register(RailcraftConstants.rl("multiblock_formed").toString(), MULTIBLOCK_FORM);
    CriteriaTriggers.register(RailcraftConstants.rl("bed_cart_sleep").toString(), BED_CART_SLEEP);
    CriteriaTriggers.register(RailcraftConstants.rl("jukebox_cart_play_music").toString(), JUKEBOX_CART_MUSIC_PLAY);
    CriteriaTriggers.register(RailcraftConstants.rl("surprise").toString(), CART_SURPRISE_EXPLODE);
    CriteriaTriggers.register(RailcraftConstants.rl("spike_maul_use").toString(), SPIKE_MAUL_USE);
    CriteriaTriggers.register(RailcraftConstants.rl("use_track_kit").toString(), TRACK_KIT_USE);
    CriteriaTriggers.register(RailcraftConstants.rl("set_season").toString(), SEASON_SET);
    CriteriaTriggers.register(RailcraftConstants.rl("killed_by_locomotive").toString(), KILLED_BY_LOCOMOTIVE);
  }
}
