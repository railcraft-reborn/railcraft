package mods.railcraft.world.level.block.entity;

import net.minecraft.client.renderer.texture.ITickable;

/**
 * Created by CovertJaguar on 7/13/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TickableOutfittedTrackBlockEntity extends OutfittedTrackBlockEntity implements ITickable {

  public TickableOutfittedTrackBlockEntity() {
    super(RailcraftBlockEntityTypes.TICKING_OUTFITTED_TRACK.get());
  }

  @Override
  public void tick() {
    getTrackKitInstance().tick();
  }
}
