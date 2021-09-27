package mods.railcraft.world.level.block.track.kit;

import mods.railcraft.api.tracks.ITrackKitInstance;
import mods.railcraft.api.tracks.TrackKitInstance;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class TrackKitRailcraft extends TrackKitInstance {

  public int getPowerPropagation() {
    return 0;
  }

  public boolean canPropagatePowerTo(ITrackKitInstance track) {
    return true;
  }
}
