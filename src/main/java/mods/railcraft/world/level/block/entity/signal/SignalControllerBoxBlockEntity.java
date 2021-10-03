package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import java.util.Set;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SignalControllerProvider;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

public class SignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerProvider {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private SignalAspect defaultAspect = SignalAspect.GREEN;
  private SignalAspect poweredAspect = SignalAspect.RED;

  public SignalControllerBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get());
  }

  public SignalAspect getDefaultAspect() {
    return this.defaultAspect;
  }

  public void setDefaultAspect(SignalAspect defaultAspect) {
    this.defaultAspect = defaultAspect;
    this.updateSignalAspect();
  }

  public SignalAspect getPoweredAspect() {
    return this.poweredAspect;
  }

  public void setPoweredAspect(SignalAspect poweredAspect) {
    this.poweredAspect = poweredAspect;
    this.updateSignalAspect();
  }

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
      this.updateSignalAspect();
    }
  }

  @Override
  public void setRemoved() {
    super.setRemoved();
    this.signalController.removed();
  }

  @Override
  public void tick() {
    super.tick();
    if (this.level.isClientSide()) {
      this.signalController.spawnTuningAuraParticles();
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighborSignalBox,
      Direction neighborDirection, boolean removed) {
    this.updateSignalAspect();
  }

  @Override
  public void neighborChanged() {
    this.updateSignalAspect();
  }

  private void updateSignalAspect() {
    Set<Direction> signalDirections = EnumSet.allOf(Direction.class);
    signalDirections.remove(Direction.UP);

    SignalAspect neighborAspect = SignalAspect.GREEN;
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (SignalBoxBlock.isAspectEmitter(signalBox.getBlockState())) {
          neighborAspect = SignalAspect.mostRestrictive(neighborAspect,
              signalBox.getSignalAspect(direction.getOpposite()));
        }
        signalDirections.remove(direction);
      }
    }

    SignalAspect signalAspect = this.defaultAspect;
    for (Direction direction : signalDirections) {
      if (this.level.getSignal(this.getBlockPos().relative(direction), direction) > 0) {
        signalAspect = this.poweredAspect;
        break;
      }
    }

    this.signalController.setSignalAspect(
        SignalAspect.mostRestrictive(neighborAspect, signalAspect));
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.signalController.getSignalAspect();
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putString("defaultAspect", this.defaultAspect.getSerializedName());
    data.putString("poweredAspect", this.poweredAspect.getSerializedName());
    data.put("signalController", this.signalController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.defaultAspect = SignalAspect.getByName(data.getString("defaultAspect")).get();
    this.poweredAspect = SignalAspect.getByName(data.getString("poweredAspect")).get();
    this.signalController.deserializeNBT(data.getCompound("signalController"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeEnum(this.defaultAspect);
    data.writeEnum(this.poweredAspect);
    this.signalController.writeSyncData(data);
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.defaultAspect = data.readEnum(SignalAspect.class);
    this.poweredAspect = data.readEnum(SignalAspect.class);
    this.signalController.readSyncData(data);
  }
}
