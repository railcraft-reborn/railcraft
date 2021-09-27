package mods.railcraft.world.level.block.entity;

import mods.railcraft.network.PacketBuilder;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * Created by CovertJaguar on 7/13/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class RailcraftTickableBlockEntity extends RailcraftBlockEntity
    implements ITickableTileEntity {

  private int maxInternal;
  private int clock = 0;
  private boolean dirty;

  public RailcraftTickableBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void tick() {
    if (this.clock++ >= this.maxInternal) {
      this.clock = 0;
    }
    if (this.dirty) {
      this.dirty = false;
      PacketBuilder.instance().sendTileEntityPacket(this);
    }
  }

  @Override
  public void syncToClient() {
    this.dirty = true;
  }

  protected boolean clock(int interval) {
    if (interval > this.maxInternal) {
      this.maxInternal = interval;
    }
    return clock % interval == 0;
  }
}
