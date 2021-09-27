package mods.railcraft.world.level.block.entity;

import javax.annotation.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.signals.ITile;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.util.HostEffects;
import mods.railcraft.world.item.IMagnifiable;
import mods.railcraft.world.level.block.ForceTrackEmitterBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.track.ForceTrackBlockEntity;
import mods.railcraft.world.level.block.track.ForceTrackBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class ForceTrackEmitterBlockEntity extends RailcraftTickableBlockEntity
    implements ITile, IMagnifiable {

  private static final double BASE_DRAW = 22;
  private static final double CHARGE_PER_TRACK = 2;
  // TODO the neighbor update from force tracks is probably good enough
  private boolean powered;
  private int numTracks;
  private ForceTrackEmitterState state = ForceTrackEmitterState.RETRACTED;
  private DyeColor color = ForceTrackEmitterBlock.DEFAULT_COLOR;
  /**
   * Field to prevent recursive removing of tracks when a track is broken by the emitter
   */
  private boolean removingTrack;

  public ForceTrackEmitterBlockEntity() {
    super(RailcraftBlockEntityTypes.FORCE_TRACK_EMITTER.get());
  }

  public ForceTrackEmitterState getState() {
    return this.state;
  }

  public int getNumTracks() {
    return this.numTracks;
  }

  public void setNumTracks(int numTracks) {
    this.numTracks = numTracks;
  }


  @Override
  public void setPlacedBy(BlockState state, @Nullable LivingEntity entityLiving,
      ItemStack stack) {
    super.setPlacedBy(state, entityLiving, stack);
    this.checkRedstone();
    this.color = DyeColor.getColor(stack);
    if (this.color == null) {
      this.color = ForceTrackEmitterBlock.DEFAULT_COLOR;
    }
  }

  private void setPowered(boolean powered) {
    this.powered = powered;
    this.updateBlockState();
  }

  public void checkRedstone() {
    if (this.level.isClientSide())
      return;
    boolean powered = PowerPlugin.isBlockBeingPowered(this.level, this.getBlockPos());
    if (this.powered != powered) {
      this.setPowered(powered);
      this.markBlockForUpdate();
    }
  }

  @Override
  public void tick() {
    super.tick();

    if (this.level.isClientSide())
      return;

    ForceTrackEmitterState previous = state;
    if (!powered) {
      state = previous.whenNoCharge(this);
    } else {
      double draw = getMaintenanceCost(getNumTracks());
      if (Charge.distribution.network(this.level).access(this.worldPosition).useCharge(draw)) {
        state = previous.afterUseCharge(this);
      } else {
        state = previous.whenNoCharge(this);
      }
    }

    if (state != previous) {
      state.onTransition(this);
      if (previous.appearPowered != state.appearPowered)
        markBlockForUpdate();
    }
  }

  // always logical server
  private void spawnParticles(BlockPos pos) {
    HostEffects.INSTANCE.forceTrackSpawnEffect(this.level, pos, color.getColorValue());
  }

  boolean placeTrack(BlockPos toPlace, BlockState prevState, RailShape direction) {
    ForceTrackBlock trackForce = RailcraftBlocks.FORCE_TRACK.get();
    if (trackForce != null && WorldPlugin.isAir(this.level, toPlace, prevState)) {
      spawnParticles(toPlace);
      BlockState place =
          trackForce.defaultBlockState().setValue(ForceTrackBlock.SHAPE, direction);
      this.level.setBlockAndUpdate(toPlace, place);
      TileEntity tile = this.level.getBlockEntity(toPlace);
      if (tile instanceof ForceTrackBlockEntity) {
        ForceTrackBlockEntity track = (ForceTrackBlockEntity) tile;
        track.setEmitter(this);
        setNumTracks(getNumTracks() + 1);
        return true;
      }
    }
    return false;
  }

  public int getNumberOfTracks() {
    return getNumTracks();
  }

  public DyeColor getColor() {
    return color;
  }

  public boolean setColor(DyeColor color) {
    if (this.color != color) {
      this.color = color;
      clearTracks();
      markBlockForUpdate();
      return true;
    }
    return false;
  }

  public static double getMaintenanceCost(int tracks) {
    return BASE_DRAW + CHARGE_PER_TRACK * tracks;
  }

  public boolean isOutOfPower() {
    return !Charge.distribution.network(this.level)
        .access(this.getBlockPos())
        .hasCapacity(getMaintenanceCost(getNumTracks() + 1));
  }

  void removeFirstTrack() {
    BlockPos toRemove = this.worldPosition.above()
        .relative(this.getBlockState().getValue(ForceTrackEmitterBlock.FACING), getNumTracks());
    removeTrack(toRemove);
  }

  private void removeTrack(BlockPos toRemove) {
    removingTrack = true;
    if (this.level.isLoaded(toRemove)
        && WorldPlugin.isBlockAt(this.level, toRemove, RailcraftBlocks.FORCE_TRACK.get())) {
      spawnParticles(toRemove);
      WorldPlugin.setBlockToAir(this.level, toRemove);
    }
    setNumTracks(getNumTracks() - 1);
    removingTrack = false;
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    clearTracks();
  }

  public void clearTracks() {
    clearTracks(0);
  }

  public void clearTracks(int lastIndex) {
    if (removingTrack || lastIndex == getNumTracks()) {
      return;
    }
    Direction facing = this.getBlockState().getValue(ForceTrackEmitterBlock.FACING);
    BlockPos.Mutable toRemove = this.getBlockPos().mutable();
    toRemove.move(Direction.UP);
    toRemove.move(facing, getNumTracks());
    while (getNumTracks() > lastIndex) {
      removeTrack(toRemove);
      toRemove.move(facing.getOpposite());
    }
    notifyTrackChange();
  }

  public void notifyTrackChange() {
    state = ForceTrackEmitterState.HALTED;
  }

  private void updateBlockState() {
    if (this.level != null) {
      BlockState state = this.getBlockState();
      if (state.is(RailcraftBlocks.FORCE_TRACK_EMITTER.get())) {
        this.level.setBlock(this.worldPosition,
            state.setValue(ForceTrackEmitterBlock.POWERED, this.powered),
            Constants.BlockFlags.DEFAULT);
      }
    }
  }

  @Override
  public void onMagnify(PlayerEntity viewer) {
    viewer.sendMessage(new TranslationTextComponent("gui.railcraft.force.track.emitter.info",
        this.getNumTracks()), Util.NIL_UUID);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("powered", this.powered);
    data.putInt("numTracks", this.getNumTracks());
    data.putString("state", this.state.name());
    data.putInt("color", this.color.getId());
    return data;
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);
    this.powered = data.getBoolean("powered");
    this.setNumTracks(data.getInt("numTracks"));
    this.state = ForceTrackEmitterState.valueOf(data.getString("state"));
    if (data.contains("color", Constants.NBT.TAG_INT)) {
      this.color = DyeColor.byId(data.getInt("color"));
    } else {
      this.color = ForceTrackEmitterBlock.DEFAULT_COLOR;
    }
  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);
    data.writeBoolean(powered);
    data.writeEnum(color);
    data.writeEnum(state);
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);

    boolean update = false;

    boolean powered = data.readBoolean();
    if (this.powered != powered) {
      this.setPowered(powered);
      update = true;
    }

    setColor(data.readEnum(DyeColor.class));

    ForceTrackEmitterState state = data.readEnum(ForceTrackEmitterState.class);
    if (state != this.state) {
      this.state = state;
      update = true;
    }

    if (update)
      markBlockForUpdate();
  }
}
