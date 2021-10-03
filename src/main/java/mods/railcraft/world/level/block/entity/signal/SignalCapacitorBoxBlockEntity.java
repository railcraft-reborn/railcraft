package mods.railcraft.world.level.block.entity.signal;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.TexturePosition;
import mods.railcraft.gui.button.ButtonState;
import mods.railcraft.gui.button.MultiButtonController;
import mods.railcraft.util.PowerUtil;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SignalCapacitorBoxBlockEntity extends AbstractSignalBoxBlockEntity {

  private short ticksPowered;
  private short ticksToPower = 200;
  private SignalAspect signalAspect = SignalAspect.OFF;
  private final MultiButtonController<Mode> modeButtonController =
      MultiButtonController.create(Mode.RISING_EDGE.ordinal(), Mode.values());

  public SignalCapacitorBoxBlockEntity() {
    super(RailcraftBlockEntityTypes.SIGNAL_CAPACITOR_BOX.get());
  }

  public short getTicksToPower() {
    return this.ticksToPower;
  }

  public void setTicksToPower(short ticksToPower) {
    this.ticksToPower = ticksToPower;
  }

  public MultiButtonController<Mode> getModeButtonController() {
    return this.modeButtonController;
  }

  @Override
  public void tick() {
    if (this.level.isClientSide()) {
      return;
    }

    if (this.ticksPowered-- > 0) {
      if (this.modeButtonController.getCurrentState() == Mode.FALLING_EDGE) {
        SignalAspect signalAspect = SignalAspect.GREEN;
        boolean powered = PowerUtil.hasRepeaterSignal(this.level, getBlockPos());
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

  @Override
  public void neighborChanged() {
    if (this.level.isClientSide()) {
      return;
    }
    boolean powered = PowerUtil.hasRepeaterSignal(this.level, this.getBlockPos());
    if (this.ticksPowered <= 0 && powered) {
      this.ticksPowered = this.ticksToPower;
      if (Objects.equals(this.modeButtonController.getCurrentState(), Mode.RISING_EDGE))
        this.signalAspect = SignalAspect.GREEN;
      this.updateNeighbors();
    }
  }

  @Override
  public void neighborSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor,
      Direction direction, boolean removed) {
    if (neighbor.getRedstoneSignal(direction) > 0) {
      this.ticksPowered = this.ticksToPower;
      if (this.modeButtonController.getCurrentState() == Mode.RISING_EDGE) {
        this.signalAspect = neighbor.getSignalAspect(direction);
      }
      this.updateNeighbors();
    }
  }

  @Override
  public void updateNeighbors() {
    super.updateNeighbors();
    this.syncToClient();
    this.updateNeighborSignalBoxes(false);
  }

  @Override
  public int getRedstoneSignal(Direction direction) {
    TileEntity blockEntity =
        this.level.getBlockEntity(this.getBlockPos().relative(direction.getOpposite()));
    return blockEntity instanceof AbstractSignalBoxBlockEntity || this.ticksPowered <= 0
        ? PowerUtil.NO_POWER
        : PowerUtil.FULL_POWER;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putShort("ticksPowered", this.ticksPowered);
    data.putShort("ticksToPower", this.ticksToPower);
    data.putString("signalAspect", this.signalAspect.getSerializedName());
    data.put("modeButtonController", this.modeButtonController.serializeNBT());
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);
    this.ticksPowered = data.getShort("ticksPowered");
    this.ticksToPower = data.getShort("ticksToPower");
    this.signalAspect = SignalAspect.getByName(data.getString("signalAspect")).get();
    this.modeButtonController.deserializeNBT(data.getCompound("modeButtonController"));
  }

  @Override
  public void writeSyncData(PacketBuffer data) {
    super.writeSyncData(data);
    data.writeBoolean(this.ticksPowered > 0);
    data.writeShort(this.ticksToPower);
    data.writeEnum(this.signalAspect);
    data.writeEnum(this.modeButtonController.getCurrentState());
  }

  @Override
  public void readSyncData(PacketBuffer data) {
    super.readSyncData(data);
    this.ticksPowered = (short) (data.readBoolean() ? 1 : 0);
    this.ticksToPower = data.readShort();
    this.signalAspect = data.readEnum(SignalAspect.class);
    this.modeButtonController.setCurrentState(data.readEnum(Mode.class));
  }

  @Override
  public SignalAspect getSignalAspect(Direction direction) {
    return this.ticksPowered > 0 ? this.signalAspect : SignalAspect.RED;
  }

  public enum Mode implements ButtonState {

    RISING_EDGE("rising_edge"),
    FALLING_EDGE("falling_edge");

    private final String name;
    private final ITextComponent label;
    private final List<? extends ITextComponent> tooltip;

    private Mode(String name) {
      this.name = name;
      this.label = new TranslationTextComponent("signal_capacitor_box.mode." + name);
      this.tooltip = Collections.singletonList(
          new TranslationTextComponent("signal_capacitor_box.mode." + name + ".description"));
    }

    @Override
    public ITextComponent getLabel() {
      return this.label;
    }

    @Override
    public TexturePosition getTexturePosition() {
      return ButtonTexture.SMALL_BUTTON;
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
