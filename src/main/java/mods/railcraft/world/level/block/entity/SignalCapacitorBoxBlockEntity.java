package mods.railcraft.world.level.block.entity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import mods.railcraft.api.signals.SignalAspect;
import mods.railcraft.gui.buttons.IButtonTextureSet;
import mods.railcraft.gui.buttons.IMultiButtonState;
import mods.railcraft.gui.buttons.MultiButtonController;
import mods.railcraft.gui.buttons.StandardButtonTextureSets;
import mods.railcraft.plugins.PowerPlugin;
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
    implements IRedstoneEmitter {

  private short ticksPowered;
  public short ticksToPower = 200;
  private SignalAspect aspect = SignalAspect.OFF;
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
    super.tick();

    if (this.level.isClientSide())
      return;

    if (ticksPowered > 0) {
      ticksPowered--;
      if (Objects.equals(stateModeController.getButtonState(), StateMode.FALLING_EDGE)) { // new
                                                                                          // behavior
        SignalAspect tmpAspect = SignalAspect.GREEN;
        boolean hasInput = false;
        if (PowerPlugin.isBlockBeingPoweredByRepeater(this.level, getBlockPos()))
          hasInput = true;
        for (Direction side : Direction.Plane.HORIZONTAL) { // get most restrictive aspect from
          // adjacent (active) boxes
          TileEntity tile = adjacentCache.getTileOnSide(side);
          if (tile instanceof AbstractSignalBoxBlockEntity) {
            AbstractSignalBoxBlockEntity box = (AbstractSignalBoxBlockEntity) tile;
            if (box.isEmittingRedstone(side.getOpposite())) {
              hasInput = true;
              tmpAspect = SignalAspect.mostRestrictive(tmpAspect,
                  box.getBoxSignalAspect(side.getOpposite()));
            }
          }
        }
        if (hasInput) {
          ticksPowered = ticksToPower; // undo any previous decrements
          if (!Objects.equals(aspect, tmpAspect)) {
            aspect = tmpAspect; // change to the most restrictive aspect found above.
            updateNeighbors();
          }
        }
      }
      // in all cases:
      if (ticksPowered <= 0)
        updateNeighbors();
    }
  }

  @Override
  public boolean canReceiveAspect() {
    return true;
  }

  @Override
  public boolean canTransferAspect() {
    return true;
  }

  @Override
  public void neighborChanged(BlockState state, Block neighborBlock, BlockPos neighborPos) {
    super.neighborChanged(state, neighborBlock, neighborPos);
    if (this.level.isClientSide())
      return;
    boolean p = PowerPlugin.isBlockBeingPoweredByRepeater(this.level, getBlockPos());
    if (ticksPowered <= 0 && p) {
      ticksPowered = ticksToPower;
      if (Objects.equals(stateModeController.getButtonState(), StateMode.RISING_EDGE))
        aspect = SignalAspect.GREEN;
      updateNeighbors();
    }
  }

  @Override
  public void neighboringSignalBoxChanged(AbstractSignalBoxBlockEntity neighbor, Direction side) {
    if (neighbor.isEmittingRedstone(side)) {
      ticksPowered = ticksToPower;
      if (Objects.equals(stateModeController.getButtonState(), StateMode.RISING_EDGE))
        aspect = neighbor.getBoxSignalAspect(side);
      updateNeighbors();
    }
  }

  private void updateNeighbors() {
    sendUpdateToClient();
    notifyBlocksOfNeighborChange();
    updateNeighborBoxes();
  }

  @Override
  public int getSignal(Direction side) {
    TileEntity tile = adjacentCache.getTileOnSide(side.getOpposite());
    if (tile instanceof AbstractSignalBoxBlockEntity)
      return PowerPlugin.NO_POWER;
    return ticksPowered > 0 ? PowerPlugin.FULL_POWER : PowerPlugin.NO_POWER;
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);

    data.putShort("ticksPowered", ticksPowered);
    data.putShort("ticksToPower", ticksToPower);
    data.putInt("aspect", aspect.getId());
    stateModeController.writeToNBT(data, "mode");
    return data;
  }

  @Override
  public void load(BlockState state, CompoundNBT data) {
    super.load(state, data);

    ticksPowered = data.getShort("ticksPowered");
    ticksToPower = data.getShort("ticksToPower");
    aspect = SignalAspect.byId(data.getInt("aspect"));
    if (data.contains("mode"))
      stateModeController.readFromNBT(data, "mode");
    else // set old boxes to immediate mode to retain old behavior
      stateModeController.setCurrentState(StateMode.RISING_EDGE.ordinal());

  }

  @Override
  public void writePacketData(PacketBuffer data) {
    super.writePacketData(data);

    data.writeBoolean(ticksPowered > 0);
    data.writeShort(ticksToPower);
    data.writeByte(aspect.ordinal());
    data.writeByte(stateModeController.getCurrentState());
  }

  @Override
  public void readPacketData(PacketBuffer data) {
    super.readPacketData(data);

    ticksPowered = (short) (data.readBoolean() ? 1 : 0);
    ticksToPower = data.readShort();
    aspect = SignalAspect.values()[data.readByte()];
    stateModeController.setCurrentState(data.readByte());
  }

  @Override
  public SignalAspect getBoxSignalAspect(@Nullable Direction side) {
    return ticksPowered > 0 ? aspect : SignalAspect.RED;
  }

  public enum StateMode implements IMultiButtonState {

    RISING_EDGE("rising"),
    FALLING_EDGE("falling");

    private final String label;
    private final List<? extends ITextComponent> tip;

    private StateMode(String label) {
      this.label = "gui.railcraft.box.capacitor." + label + ".name";
      this.tip = Collections.singletonList(new TranslationTextComponent(label + ".tips"));
    }

    @Override
    public ITextComponent getLabel() {
      return new TranslationTextComponent(label);
    }

    @Override
    public IButtonTextureSet getTextureSet() {
      return StandardButtonTextureSets.SMALL_BUTTON;
    }

    @Override
    public List<? extends ITextComponent> getTooltip() {
      return tip;
    }
  }
}
