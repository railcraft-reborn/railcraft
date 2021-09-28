package mods.railcraft.world.level.block.entity.track;

import javax.annotation.Nullable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.entity.ForceTrackEmitterBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Constants;

/**
 * Created by CovertJaguar on 8/15/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public final class ForceTrackBlockEntity extends RailcraftBlockEntity {

  @Nullable
  private ForceTrackEmitterBlockEntity emitter;
  private int index;
  private DyeColor color = ForceTrackEmitterBlock.DEFAULT_COLOR;

  public ForceTrackBlockEntity() {
    this(RailcraftBlockEntityTypes.FORCE_TRACK.get());
  }

  public ForceTrackBlockEntity(TileEntityType<?> type) {
    super(type);
  }

  public DyeColor getColor() {
    return this.color;
  }

  public void notifyEmitterForBreak() {
    if (this.emitter != null) {
      this.emitter.clearTracks(this.index);
    }
  }

  public void notifyEmitterForTrackChange() {
    if (this.emitter != null) {
      this.emitter.notifyTrackChange();
    }
  }

  public void setEmitter(@Nullable ForceTrackEmitterBlockEntity emitter) {
    this.emitter = emitter;
    if (emitter != null) {
      this.setOwner(emitter.getOwner());
      this.color = emitter.getColor();
      this.index = emitter.getTrackCount();
    }
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.color);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.color = data.readEnum(DyeColor.class);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putInt("color", this.color.getId());
    data.putInt("index", this.index);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    if (data.contains("color", Constants.NBT.TAG_INT)) {
      this.color = DyeColor.byId(data.getInt("color"));
    } else {
      this.color = ForceTrackEmitterBlock.DEFAULT_COLOR;
    }
    this.index = data.getInt("index");
  }
}
