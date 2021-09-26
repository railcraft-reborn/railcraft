package mods.railcraft.world.level.block.track.kit;

import mods.railcraft.Railcraft;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class TrackKits {

  public static final DeferredRegister<TrackKit> TRACK_KITS =
      DeferredRegister.create(TrackKit.class, Railcraft.ID);

  public static final Lazy<IForgeRegistry<TrackKit>> REGISTRY =
      Lazy.of(TRACK_KITS.makeRegistry("track_kits", RegistryBuilder::new));

  public static final RegistryObject<TrackKit> TURNOUT =
      TRACK_KITS.register("turnout",
          () -> new TrackKit.Builder(TurnoutSwitchTrackKit::new)
              .setRequiresTicks(true)
              .build());

  public static final RegistryObject<TrackKit> WYE =
      TRACK_KITS.register("wye",
          () -> new TrackKit.Builder(WyeSwitchTrackKit::new)
              .setRequiresTicks(true)
              .build());

  public static final RegistryObject<TrackKit> ACTIVATOR =
      TRACK_KITS.register("activator",
          () -> new TrackKit.Builder(RailcraftItems.ACTIVATOR_TRACK_KIT, ActivatorTrackKit::new)
              .build());

  public static final RegistryObject<TrackKit> BOOSTER =
      TRACK_KITS.register("booster",
          () -> new TrackKit.Builder(RailcraftItems.BOOSTER_TRACK_KIT, BoosterTrackKit::new)
              .build());

  public static final RegistryObject<TrackKit> HIGH_SPEED_TRANSITION =
      TRACK_KITS.register("high_speed_transition",
          () -> new TrackKit.Builder(RailcraftItems.HIGH_SPEED_TRANSITION_TRACK_KIT,
              SpeedTransitionTrackKit::new)
                  .build());
}
