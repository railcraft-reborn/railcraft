package mods.railcraft.integrations.jade;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import mods.railcraft.world.level.block.signal.SignalControllerBoxBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import mods.railcraft.world.level.block.track.outfitted.CouplerTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LauncherTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LockingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.LocomotiveTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.RoutingTrackBlock;
import mods.railcraft.world.level.block.track.outfitted.ThrottleTrackBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(RailcraftConstants.ID)
public class RailcraftPlugin implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {
    registration.registerBlockDataProvider(new RoutingTrackComponent(), RoutingTrackBlockEntity.class);
  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    // Tracks
    registration.registerBlockComponent(new LockingTrackComponent(), LockingTrackBlock.class);
    registration.registerBlockComponent(new CouplerTrackComponent(), CouplerTrackBlock.class);
    registration.registerBlockComponent(new LocomotiveTrackComponent(), LocomotiveTrackBlock.class);
    registration.registerBlockComponent(new LauncherTrackComponent(), LauncherTrackBlock.class);
    registration.registerBlockComponent(new EmbarkingTrackComponent(), EmbarkingTrackBlock.class);
    registration.registerBlockComponent(new ThrottleTrackComponent(), ThrottleTrackBlock.class);
    registration.registerBlockComponent(new RoutingTrackComponent(), RoutingTrackBlock.class);

    // Signals
    registration.registerBlockComponent(new SignalControllerComponent(), SignalControllerBoxBlock.class);

    // Actuators
    registration.registerBlockComponent(new SwitchTrackComponent(), SwitchTrackActuatorBlock.class);

    // Entities
    registration.registerEntityComponent(new LocomotiveComponent(), Locomotive.class);
  }
}
