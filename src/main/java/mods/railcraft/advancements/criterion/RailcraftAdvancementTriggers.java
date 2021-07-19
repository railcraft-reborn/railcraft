package mods.railcraft.advancements.criterion;

import mods.railcraft.api.core.RailcraftConstantsAPI;
import mods.railcraft.plugins.SeasonPlugin;
import mods.railcraft.world.entity.LocomotiveEntity;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public final class RailcraftAdvancementTriggers {

  private final CartLinkingTrigger cartLinking = new CartLinkingTrigger();
  private final MultiBlockFormedTrigger multiBlockFormed = new MultiBlockFormedTrigger();
  private final JukeboxCartPlayMusicTrigger jukeboxCartPlayMusic =
      new JukeboxCartPlayMusicTrigger();
  private final BedCartSleepTrigger bedCartSleep = new BedCartSleepTrigger();
  private final SurpriseTrigger surprise = new SurpriseTrigger();
  private final SetSeasonTrigger setSeason = new SetSeasonTrigger();
  private final SpikeMaulUseTrigger spikeMaulUse = new SpikeMaulUseTrigger();
  private final UseTrackKitTrigger useTrackKit = new UseTrackKitTrigger();
  private final CartRidingTrigger cartRiding = new CartRidingTrigger();
  private final KilledByLocomotiveTrigger killedByLocomotive = new KilledByLocomotiveTrigger();

  public static RailcraftAdvancementTriggers getInstance() {
    return Holder.INSTANCE;
  }

  public void register() {
    CriteriaTriggers.register(cartLinking);
    CriteriaTriggers.register(multiBlockFormed);
    CriteriaTriggers.register(jukeboxCartPlayMusic);
    CriteriaTriggers.register(bedCartSleep);
    CriteriaTriggers.register(surprise);
    CriteriaTriggers.register(setSeason);
    CriteriaTriggers.register(spikeMaulUse);
    CriteriaTriggers.register(useTrackKit);
    CriteriaTriggers.register(cartRiding);
    CriteriaTriggers.register(killedByLocomotive);
    ItemPredicate.register(RailcraftConstantsAPI.locationOf("is_cart"),
        (json) -> new CartItemPredicate());
    ItemPredicate.register(RailcraftConstantsAPI.locationOf("is_track"),
        TrackItemPredicate.DESERIALIZER);
  }

  public void onJukeboxCartPlay(ServerPlayerEntity player, AbstractMinecartEntity cart,
      ResourceLocation music) {
    jukeboxCartPlayMusic.trigger(player, instance -> instance.test(player, cart, music));
  }

  public void onPlayerSleepInCart(ServerPlayerEntity player, AbstractMinecartEntity cart) {
    bedCartSleep.trigger(player, instance -> instance.cartPredicate.test(player, cart));
  }

  public void onSurpriseExplode(ServerPlayerEntity owner, AbstractMinecartEntity cart) {
    surprise.trigger(owner, instance -> instance.test(owner, cart));
  }

  public void onSeasonSet(ServerPlayerEntity player, AbstractMinecartEntity cart,
      SeasonPlugin.Season season) {
    setSeason.trigger(player, instance -> instance.test(player, cart, season));
  }

  public void onSpikeMaulUsageSuccess(ServerPlayerEntity player, World world, BlockPos pos,
      ItemStack tool) {
    spikeMaulUse.trigger(player, instance -> instance.matches(tool, (ServerWorld) world, pos));
  }

  public void onTrackKitUse(ServerPlayerEntity player, World world, BlockPos pos, ItemStack stack) {
    useTrackKit.trigger(player, instance -> instance.matches((ServerWorld) world, pos, stack));
  }

  public void onKilledByLocomotive(ServerPlayerEntity player, LocomotiveEntity loco) {
    killedByLocomotive.trigger(player, instance -> instance.cart.test(player, loco));
  }

  RailcraftAdvancementTriggers() {}

  static final class Holder {
    // Lazy init because there are a lot of triggers
    static final RailcraftAdvancementTriggers INSTANCE = new RailcraftAdvancementTriggers();

    private Holder() {}
  }
}
