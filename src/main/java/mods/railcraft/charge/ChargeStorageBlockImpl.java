package mods.railcraft.charge;

import mods.railcraft.api.charge.ChargeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.energy.EnergyStorage;

public class ChargeStorageBlockImpl extends EnergyStorage implements ChargeStorage {

  private final BlockPos pos;
  private final Spec batterySpec;
  private StateImpl stateImpl = StateImpl.RECHARGEABLE;
  private State state = State.RECHARGEABLE;
  private int chargeDrawnThisTick;

  public ChargeStorageBlockImpl(BlockPos pos, Spec batterySpec) {
    super(batterySpec.capacity());
    this.pos = pos;
    this.batterySpec = batterySpec;
    this.setState(batterySpec.initialState());
  }

  public void setEnergyStored(int energy) {
    this.energy = energy;
  }

  @Override
  public BlockPos getBlockPos() {
    return pos;
  }

  @Override
  public State getState() {
    return state;
  }

  @Override
  public void setState(State state) {
    this.state = state;
    this.stateImpl = StateImpl.valueOf(state.name());
  }

  public Spec getBatterySpec() {
    return this.batterySpec;
  }

  @Override
  public int getEnergyStored() {
    return this.stateImpl.getEnergyStored(this);
  }

  @Override
  public float getEfficiency() {
    return this.batterySpec.efficiency();
  }

  /**
   * The maximum amount of charge that can be drawn from this battery per tick.
   */
  @Override
  public int getMaxDraw() {
    return this.stateImpl.getMaxDraw(this);
  }

  public void tick() {
    this.chargeDrawnThisTick = 0;
  }

  @Override
  public int getMaxEnergyStored() {
    return this.stateImpl.getMaxEnergyStored(this);
  }

  /**
   * Remove up to the requested amount of charge and returns the amount removed.
   *
   * @return charge removed
   */
  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    var drawn = this.stateImpl.extractEnergy(this, maxExtract, simulate);
    if (!simulate) {
      this.chargeDrawnThisTick += drawn;
    }
    return drawn;
  }

  private int superExtractEnergy(int maxExtract, boolean simulate) {
    return super.extractEnergy(maxExtract, simulate);
  }

  /**
   * The amount of charge remaining that can be drawn from this battery this tick.
   *
   * @return The amount of charge that can be withdraw from the battery right now
   */
  @Override
  public int getAvailableCharge() {
    return Mth.clamp(this.getMaxDraw() - this.chargeDrawnThisTick, 0,
        Mth.floor(this.getEnergyStored() * this.getEfficiency()));
  }

  public int getInitialCharge() {
    return this.state == State.DISPOSABLE ? this.getMaxEnergyStored() : 0;
  }

  @Override
  public String toString() {
    return String.format("%s@%s { energy: %d }", getClass().getSimpleName(),
        Integer.toHexString(hashCode()), this.energy);
  }

  private enum StateImpl {
    INFINITE {
      @Override
      public int getEnergyStored(ChargeStorageBlockImpl battery) {
        return battery.getMaxEnergyStored();
      }

      @Override
      public int extractEnergy(ChargeStorageBlockImpl battery, int request, boolean simulate) {
        return request;
      }
    },
    SOURCE,
    RECHARGEABLE,
    DISPOSABLE,
    DISABLED {
      @Override
      public int getEnergyStored(ChargeStorageBlockImpl battery) {
        return 0;
      }

      @Override
      public int getMaxEnergyStored(ChargeStorageBlockImpl battery) {
        return 0;
      }

      @Override
      public int getMaxDraw(ChargeStorageBlockImpl battery) {
        return 0;
      }

      @Override
      public int extractEnergy(ChargeStorageBlockImpl battery, int request, boolean simulate) {
        return 0;
      }
    };

    public int getEnergyStored(ChargeStorageBlockImpl battery) {
      return battery.energy;
    }

    public int getMaxEnergyStored(ChargeStorageBlockImpl battery) {
      return battery.getBatterySpec().capacity();
    }

    public int getMaxDraw(ChargeStorageBlockImpl battery) {
      return battery.getBatterySpec().maxDraw();
    }

    public int extractEnergy(ChargeStorageBlockImpl battery, int desiredAmount, boolean simulate) {
      return battery.superExtractEnergy(desiredAmount, simulate);
    }
  }
}
