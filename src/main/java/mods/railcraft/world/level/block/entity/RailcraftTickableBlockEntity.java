package mods.railcraft.world.level.block.entity;

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * Created by CovertJaguar on 7/13/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public abstract class RailcraftTickableBlockEntity extends RailcraftBlockEntity
    implements ITickableTileEntity {

  private int maxInternal;
  private int clock = 0;

  private boolean loaded;

  public RailcraftTickableBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  @Override
  public void tick() {
    if (!this.loaded) {
      this.loaded = true;
      this.load();
    }
    if (this.clock++ >= this.maxInternal) {
      this.clock = 0;
    }
  }

  protected void load() {}

  protected boolean clock(int interval) {
    if (interval > this.maxInternal) {
      this.maxInternal = interval;
    }
    return clock % interval == 0;
  }
}
