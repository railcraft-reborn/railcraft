package mods.railcraft.world.level.block.entity.signal;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.ButtonTextureSet;
import mods.railcraft.gui.button.MultiButtonController;
import mods.railcraft.gui.button.StandardButtonTextureSets;
import mods.railcraft.plugins.PowerPlugin;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SignalCapacitorBoxBlockEntity extends AbstractSignalBoxBlockEntity
    implements SignalEmitter {

  private short ticksPowered;
  public short ticksToPower = 200;
  private SignalAspect signalAspect = SignalAspect.OFF;
  private final MultiButtonController<StateMode> stateModeController =
      MultiButtonController.create(StateMode.RISING_EDGE.ordinal(), StateMode.values());

  public SignalCapacitorBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get());
  }

  public MultiButtonController<StateMode> getStateModeController() {
    return stateModeController;
  }

  @Override
  public void tick() {
    if (this.level.isClientSide())
      return;

    if (this.ticksPowered-- > 0) {
      if (this.stateModeController.getCurrentState() == StateMode.FALLING_EDGE) {
        SignalAspect signalAspect = SignalAspect.GREEN;
        boolean powered = PowerPlugin.hasRepeaterSignal(this.level, getBlockPos());
        for (Direction direction : Direction.Plane.HORIZONTAL) {
          TileEntity blockEntity =
              this.level.getBlockEntity(this.getBlockPos().relative(direction));
          if (blockEntity instanceof AbstractSignalBoxBlockEntity) {
            AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) blockEntity;
            if (box.getRedstoneSignal(direction.getOpposite()) > 0) {
              powered = true;
              signalAspect = SignalAspect.mostRestrictive(signalAspect,
                  box.getSignalAspect(direction.getOpposite()));
            }
          }
        }
        if (powered) {
          this.ticksPowered = this.ticksToPower;
          if (this.signalAspect != signalAspect) {
            this.signalAspect = signalAspect; // change to the most restrictive aspect found above.
            this.updateNeighbors();
          }
        }
      }

      if (this.ticksPowered <= 0) {
        this.updateNeighbors();
      }
    }
  }

  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos neighborPos) {
    if (this.level.isClientSide())
      return;
    boolean powered = PowerPlugin.hasRepeaterSignal(this.level, getBlockPos());
    if (this.ticksPowered <= 0 && powered) {
      this.ticksPowered = this.ticksToPower;
      if (Objects.equals(this.stateModeController.getCurrentState(), StateMode.RISING_EDGE))
        this.signalAspect = SignalAspect.GREEN;
      this.updateNeighbors();
    }
  }

  @Override
  public void neighboringSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor,
      Direction direction) {
    if (neighbor.getRedstoneSignal(direction) > 0) {
      this.ticksPowered = this.ticksToPower;
      if (this.stateModeController.getCurrentState() == StateMode.RISING_EDGE) {
        this.signalAspect = neighbor.getSignalAspect(direction);
      }
      this.updateNeighbors();
    }
  }

  @Override
  public void updateNeighbors() {
    super.updateNeighbors();
    this.syncToClient();
    this.updateNeighborBoxes();
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    TileEntity blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(direction.getOpposite()));
    return blockEntity instanceof AbstractSignalBoxBlockEntity || this.ticksPowered <= 0
        ? PowerPlugin.NO_POWER
        : PowerPlugin.FULL_POWER;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putShort("ticksPowered", this.ticksPowered);
    data.putShort("ticksToPower", this.ticksToPower);
    data.putString("signalAspect", this.signalAspect.getSerializedName());
    data.put("mode", this.stateModeController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.ticksPowered = data.getShort("ticksPowered");
    this.ticksToPower = data.getShort("ticksToPower");
    this.signalAspect = SignalAspect.getByName(data.getString("signalAspect")).get();
    this.stateModeController.deserializeNBT(data.getCompound("mode"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeBoolean(this.ticksPowered > 0);
    data.writeShort(this.ticksToPower);
    data.writeEnum(this.signalAspect);
    data.writeByte(this.stateModeController.getCurrentStateIndex());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.ticksPowered = (short) (data.readBoolean() ? 1 : 0);
    this.ticksToPower = data.readShort();
    this.signalAspect = data.readEnum(SignalAspect.class);
    this.stateModeController.setCurrentState(data.readByte());
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.ticksPowered > 0 ? this.signalAspect : SignalAspect.RED;
  }

  public enum StateMode implements ButtonState {

    RISING_EDGE("rising"),
    FALLING_EDGE("falling");

    private final String name;
    private final ITextComponent label;
    private final List<? extends ITextComponent> tooltip;

    private StateMode(String name) {
      this.name = name;
      this.label = new TranslationTextComponent("gui.railcraft.box.capacitor." + name + ".name");
      this.tooltip = Collections.singletonList(new TranslationTextComponent(name + ".tips"));
    }

    @Override
    public ITextComponent getLabel() {
      return this.label;
    }

    @Override
    public ButtonTextureSet getTextureSet() {
      return StandardButtonTextureSets.SMALL_BUTTON;
    }

    @Override
    public List<? extends ITextComponent> getTooltip() {
      return this.tooltip;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }
  }
}
