package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class SignalControllerBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalControllerEntity {

  private final SimpleSignalController signalController =
      new SimpleSignalController(1, this::syncToClient, this, true);

  private SignalAspect defaultAspect = SignalAspect.GREEN;
  private SignalAspect poweredAspect = SignalAspect.RED;

  public SignalControllerBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SIGNAL_CONTROLLER_BOX.get(), blockPos, blockState);
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
  public void onLoad() {
    super.onLoad();
    if (!this.level.isClientSide()) {
      this.signalController.refresh();
      this.updateSignalAspect();
    }
  }

  @Override
  public void blockRemoved() {
    super.blockRemoved();
    this.signalController.destroy();
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
    var signalDirections = EnumSet.allOf(Direction.class);
    signalDirections.remove(Direction.UP);

    var neighborAspect = SignalAspect.GREEN;
    for (var direction : Direction.Plane.HORIZONTAL) {
      var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity instanceof AbstractSignalBoxBlockEntity signalBox) {
        if (SignalBoxBlock.isAspectEmitter(signalBox.getBlockState())) {
          neighborAspect = SignalAspect.mostRestrictive(neighborAspect,
              signalBox.getSignalAspect(direction.getOpposite()));
        }
        signalDirections.remove(direction);
      }
    }

    var signalAspect = this.defaultAspect;
    for (var direction : signalDirections) {
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
    return this.signalController.aspect();
  }

  @Override
  public SimpleSignalController getSignalController() {
    return this.signalController;
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("defaultAspect", this.defaultAspect.getSerializedName());
    tag.putString("poweredAspect", this.poweredAspect.getSerializedName());
    tag.put("signalController", this.signalController.serializeNBT());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.defaultAspect = SignalAspect.getByName(tag.getString("defaultAspect")).get();
    this.poweredAspect = SignalAspect.getByName(tag.getString("poweredAspect")).get();
    this.signalController.deserializeNBT(tag.getCompound("signalController"));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.defaultAspect);
    data.writeEnum(this.poweredAspect);
    this.signalController.writeToBuf(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.defaultAspect = data.readEnum(SignalAspect.class);
    this.poweredAspect = data.readEnum(SignalAspect.class);
    this.signalController.readFromBuf(data);
  }

  public static void clientTick(Level level, BlockPos blockPos, BlockState blockState,
      SignalControllerBoxBlockEntity blockEntity) {
    blockEntity.signalController.spawnTuningAuraParticles();
  }
}
