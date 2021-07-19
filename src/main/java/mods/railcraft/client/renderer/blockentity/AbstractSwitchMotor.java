package mods.railcraft.client.renderer.blockentity;

import javax.annotation.Nullable;
import mods.railcraft.api.tracks.ISwitchMotor;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;
import mods.railcraft.world.level.block.track.outfitted.kit.TrackKitSwitch;
import net.minecraft.block.BlockState;
import net.minecraft.block.ComparatorBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractSwitchMotor extends RailcraftTickableBlockEntity
    implements ISwitchMotor {

  private static final int ARROW_UPDATE_INTERVAL = 16;
  private boolean signal;
  private boolean switchedOld;
  private ArrowDirection redArrowRenderState = ArrowDirection.EAST_WEST;
  private ArrowDirection whiteArrowRenderState = ArrowDirection.NORTH_SOUTH;

  public AbstractSwitchMotor(TileEntityType<?> type) {
    super(type);
  }

  public ArrowDirection getRedArrowRenderState() {
    return redArrowRenderState;
  }

  public ArrowDirection getWhiteArrowRenderState() {
    return whiteArrowRenderState;
  }

  @Override
  public void tick() {
    super.tick();
    if (!this.level.isClientSide())
      return;

    if (this.clock(ARROW_UPDATE_INTERVAL))
      this.updateArrows();
  }

  @Override
  public void onSwitch(boolean switched) {
    if (this.switchedOld != switched) {
      this.switchedOld = switched;
      if (switched)
        this.level.playSound(null, this.getBlockPos(), SoundEvents.PISTON_CONTRACT,
            SoundCategory.BLOCKS, 0.25F, this.level.getRandom().nextFloat() * 0.25F + 0.7F);
      else
        this.level.playSound(null, this.getBlockPos(), SoundEvents.PISTON_EXTEND,
            SoundCategory.BLOCKS, 0.25F, this.level.getRandom().nextFloat() * 0.25F + 0.7F);

      Direction.Plane.HORIZONTAL.forEach(direction -> {
        BlockPos pos = this.getBlockPos().relative(direction);
        if (WorldPlugin.isBlockAt(this.level, pos, ComparatorBlock.class)) {
          WorldPlugin.notifyBlockOfStateChange(this.level, pos, this.getBlockState().getBlock());
        }
      });
    }
  }

  @Override
  public void updateArrows() {
    ArrowDirection redArrow = null;
    ArrowDirection whiteArrow = null;
    for (Direction side : Direction.Plane.HORIZONTAL) {
      TrackKitSwitch trackSwitch =
          TrackTools.getTrackInstance(this.adjacentCache.getTileOnSide(side), TrackKitSwitch.class);
      if (trackSwitch != null) {
        redArrow = mergeArrowDirection(redArrow, trackSwitch.getRedSignDirection());
        whiteArrow = mergeArrowDirection(whiteArrow, trackSwitch.getWhiteSignDirection());
      }
    }
    boolean changed = false;
    if (redArrow != null && redArrowRenderState != redArrow) {
      redArrowRenderState = redArrow;
      changed = true;
    }
    if (whiteArrow != null && whiteArrowRenderState != whiteArrow) {
      whiteArrowRenderState = whiteArrow;
      changed = true;
    }
    if (changed)
      markBlockForUpdate();
  }

  private @Nullable ArrowDirection mergeArrowDirection(@Nullable ArrowDirection arrow1,
      @Nullable ArrowDirection arrow2) {
    if (arrow1 == arrow2)
      return arrow1;
    if (arrow1 == null)
      return arrow2;
    if (arrow2 == null)
      return arrow1;
    if (isEastOrWest(arrow1) && isEastOrWest(arrow2))
      return ArrowDirection.EAST_WEST;
    return ArrowDirection.NORTH_SOUTH;
  }

  private boolean isEastOrWest(ArrowDirection arrowDirection) {
    switch (arrowDirection) {
      case EAST:
      case WEST:
      case EAST_WEST:
        return true;
      default:
        return false;
    }
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("signal", hasSignal());
    data.putBoolean("switchedOld", this.switchedOld);
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.signal = data.getBoolean("signal");
    this.switchedOld = data.getBoolean("switchedOld");
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeBoolean(this.signal);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);
    this.signal = data.readBoolean();
  }

  public boolean hasSignal() {
    return this.signal;
  }

  protected void setSignal(boolean signal) {
    this.signal = signal;
    this.sendUpdateToClient();
  }

  protected boolean isBeingPoweredByRedstone() {
    return PowerPlugin.isBlockBeingPowered(this.level, this.getBlockPos())
        || PowerPlugin.isRedstonePowered(this.level, this.getBlockPos());
  }
}
