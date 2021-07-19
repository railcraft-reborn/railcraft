package mods.railcraft.world.level.block.track.outfitted.kit;

import java.util.function.Predicate;
import mods.railcraft.Railcraft;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackType;
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

  private static final Predicate<TrackType> IS_HIGH_SPEED = TrackType::isHighSpeed;
  private static final Predicate<TrackType> NOT_HIGH_SPEED = IS_HIGH_SPEED.negate();

  public static final RegistryObject<TrackKit> ACTIVATOR = TRACK_KITS.register("activator",
      () -> new TrackKit.Builder(TrackKitActivator::new)
          .setRenderStates(2)
          .build());

  public static final RegistryObject<TrackKit> BOOSTER = TRACK_KITS.register("booster",
      () -> new TrackKit.Builder(TrackKitBooster::new)
          .setRenderStates(2)
          .build());

  public static final RegistryObject<TrackKit> HIGH_SPEED_TRANSITION =
      TRACK_KITS.register("high_speed_transition",
          () -> new TrackKit.Builder(TrackKitBooster::new)
              .setRenderStates(4)
              .setTrackTypeFilter(IS_HIGH_SPEED)
              .build());
}
