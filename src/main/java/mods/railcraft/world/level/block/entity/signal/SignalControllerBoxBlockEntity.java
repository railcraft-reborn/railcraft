package mods.railcraft.world.level.block.entity.signal;

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

  @Override
  public void load() {
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
      this.neighborChanged();
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
  public void neighborChanged() {
    SignalAspect signalAspect = this.defaultAspect;
    for (Direction direction : Direction.Plane.HORIZONTAL) {
      TileEntity blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
        AbstractSignalBoxBlockEntity signalBox = (AbstractSignalBoxBlockEntity) blockEntity;
        if (SignalBoxBlock.isAspectEmitter(signalBox.getBlockState())) {
          signalAspect = SignalAspect.mostRestrictive(signalAspect,
              signalBox.getSignalAspect(direction.getOpposite()));
        }
      } else if (this.level.getSignal(this.getBlockPos(), direction) > 0) {
        signalAspect = SignalAspect.mostRestrictive(signalAspect, this.poweredAspect);
      }
    }
    this.signalController.setSignalAspect(signalAspect);
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
