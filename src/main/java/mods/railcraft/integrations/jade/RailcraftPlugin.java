package mods.railcraft.integrations.jade;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.level.block.signal.SignalControllerBoxBlock;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin(RailcraftConstants.ID)
public class RailcraftPlugin implements IWailaPlugin {

  @Override
  public void register(IWailaCommonRegistration registration) {

  }

  @Override
  public void registerClient(IWailaClientRegistration registration) {
    registration.registerBlockComponent(new SignalControllerComponent(), SignalControllerBoxBlock.class);
    registration.registerBlockComponent(new SwitchTrackComponent(), SwitchTrackActuatorBlock.class);

    registration.registerEntityComponent(new LocomotiveComponent(), Locomotive.class);
  }
}
