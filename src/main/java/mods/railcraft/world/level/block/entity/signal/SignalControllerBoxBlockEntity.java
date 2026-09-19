package mods.railcraft.world.level.block.entity.signal;

import java.util.EnumSet;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.api.signal.SimpleSignalController;
import mods.railcraft.api.signal.entity.SignalControllerEntity;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.signal.SignalBoxBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
  protected void blockRemoved() {
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
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.store(CompoundTagKeys.DEFAULT_ASPECT, SignalAspect.CODEC, this.defaultAspect);
    output.store(CompoundTagKeys.POWERED_ASPECT, SignalAspect.CODEC, this.poweredAspect);
    output.putChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.defaultAspect =
        input.read(CompoundTagKeys.DEFAULT_ASPECT, SignalAspect.CODEC).orElse(SignalAspect.GREEN);
    this.poweredAspect =
        input.read(CompoundTagKeys.POWERED_ASPECT, SignalAspect.CODEC).orElse(SignalAspect.RED);
    input.readChild(CompoundTagKeys.SIGNAL_CONTROLLER, this.signalController);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.defaultAspect);
    data.writeEnum(this.poweredAspect);
    this.signalController.writeToBuf(data);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
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
