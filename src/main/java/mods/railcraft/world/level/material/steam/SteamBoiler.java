package mods.railcraft.world.level.material.steam;

import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.google.common.primitives.Floats;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.gui.widget.Gauge;
import mods.railcraft.world.level.material.FuelProvider;
import mods.railcraft.world.level.material.RailcraftFluids;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;

/**
 * The boiler itself. Used to simulate turning water into steam.
 */
public class SteamBoiler implements ValueIOSerializable {

  private final Gauge temperatureGauge = new TemperatureGauge();

  private final StandardTank waterTank;
  private final StandardTank steamTank;

  private float burnTime;
  private float currentItemBurnTime;
  protected boolean burning;
  protected byte burnCycle;
  private double partialConversions;

  private float temperature = SteamConstants.COLD_TEMP;
  private float maxTemperature = SteamConstants.MAX_HEAT_LOW;

  private double efficiencyModifier = 1;
  private int ticksPerCycle = 16;
  @Nullable
  private Runnable changeListener;
  @Nullable
  private FuelProvider fuelProvider;

  public SteamBoiler(StandardTank waterTank, StandardTank steamTank) {
    this.waterTank = waterTank;
    this.steamTank = steamTank;
  }

  /**
   * Callback for adding water.
   */
  public void checkFill(FluidStack waterOriginalState, Runnable explosionCallback) {
    if (this.isSuperHeated() && waterOriginalState.isEmpty()) {
      explosionCallback.run();
    }
  }

  public void setFuelProvider(FuelProvider fuelProvider) {
    this.fuelProvider = fuelProvider;
  }

  public void setChangeListener(@Nullable Runnable changeListener) {
    this.changeListener = changeListener;
  }

  public SteamBoiler setTicksPerCycle(int ticks) {
    this.ticksPerCycle = ticks;
    return this;
  }

  public SteamBoiler setEfficiencyModifier(double modifier) {
    this.efficiencyModifier = modifier;
    return this;
  }

  public float getMaxTemperature() {
    return this.maxTemperature;
  }

  public void setMaxTemperature(float maxTemperature) {
    this.maxTemperature = maxTemperature;
    this.temperatureGauge.refresh();
  }

  public float getHeatStep() {
    return this.fuelProvider == null ? SteamConstants.HEAT_STEP : this.fuelProvider.getHeatStep();
  }

  public void reset() {
    this.setTemperature(SteamConstants.COLD_TEMP);
  }

  public float getTemperature() {
    return this.temperature;
  }

  public void setTemperature(float temperature) {
    this.temperature =
        Floats.constrainToRange(temperature, SteamConstants.COLD_TEMP, this.getMaxTemperature());
  }

  public float getTemperaturePercent() {
    return this.temperature / this.getMaxTemperature();
  }

  public void increaseHeat(int numTanks) {
    float max = this.getMaxTemperature();
    if (this.temperature == max) {
      return;
    }
    float step = this.getHeatStep();
    float delta = step + (((max - temperature) / max) * step * 3);
    delta /= numTanks;
    this.setTemperature(this.getTemperature() + delta);
  }

  public void reduceHeat(int numTanks) {
    if (this.temperature == SteamConstants.COLD_TEMP) {
      return;
    }
    float step = SteamConstants.HEAT_STEP;
    float delta = step + ((this.temperature / this.getMaxTemperature()) * step * 3);
    delta /= numTanks;
    this.setTemperature(this.getTemperature() - delta);
  }

  public boolean isCold() {
    return this.getTemperature() < SteamConstants.BOILING_POINT;
  }

  public boolean isSuperHeated() {
    return this.getTemperature() >= SteamConstants.SUPER_HEATED;
  }

  public boolean isBurning() {
    return this.burning;
  }

  public void setBurning(boolean burning) {
    this.burning = burning;
  }

  public boolean hasFuel() {
    return this.getBurnTime() > 0;
  }

  public int getBurnProgressScaled(int i) {
    if (this.isCold()) {
      return 0;
    }
    int scale = (int) ((this.getBurnTime() / this.getCurrentItemBurnTime()) * i);
    scale = Math.max(0, scale);
    scale = Math.min(i, scale);
    return scale;
  }

  private boolean addFuel(Level level) {
    if (this.fuelProvider == null) {
      return false;
    }
    float fuel = this.fuelProvider.consumeFuel(level);
    if (fuel <= 0) {
      return false;
    }
    this.setBurnTime(this.getBurnTime() + fuel);
    this.setCurrentItemBurnTime(this.getBurnTime());
    return true;
  }

  public float getFuelPerCycle(int numTanks) {
    float fuel = SteamConstants.FUEL_PER_BOILER_CYCLE;
    fuel -= numTanks * SteamConstants.FUEL_PER_BOILER_CYCLE * 0.0125F;
    // linear unefficiency for temp
    fuel += (SteamConstants.FUEL_HEAT_INEFFICIENCY * getTemperaturePercent());
    // simulated pressure unefficiency, linear.
    fuel += SteamConstants.FUEL_PRESSURE_INEFFICIENCY
        * (this.getMaxTemperature() / SteamConstants.MAX_HEAT_HIGH);
    fuel *= numTanks;
    fuel *= (float) this.efficiencyModifier;
    fuel *= RailcraftConfig.SERVER.fuelPerSteamMultiplier.get();
    return fuel;
  }

  public void tick(Level level, int numTanks) {
    this.burnCycle++;
    if (this.burnCycle >= this.ticksPerCycle) {
      this.burnCycle = 0;
      float fuelNeeded = this.getFuelPerCycle(numTanks);
      while (this.getBurnTime() < fuelNeeded) {
        boolean addedFuel = addFuel(level);
        if (!addedFuel) {
          break;
        }
      }
      boolean wasBurning = this.burning;
      this.burning = this.getBurnTime() >= fuelNeeded;
      if (this.burning) {
        this.setBurnTime(this.getBurnTime() - fuelNeeded);
      } else {
        this.setBurnTime(0);
      }
      if (this.changeListener != null && this.burning != wasBurning) {
        this.changeListener.run();
      }
      this.convertSteam(numTanks);
    }
    if (this.burning) {
      this.increaseHeat(numTanks);
    } else {
      this.reduceHeat(numTanks);
    }
  }

  public int convertSteam(int numTanks) {
    if (this.isCold()) {
      return 0;
    }
    this.partialConversions += numTanks * getTemperaturePercent();
    int waterCost = (int) this.partialConversions;
    if (waterCost <= 0) {
      return 0;
    }
    this.partialConversions -= waterCost;

    try (var tx = Transaction.openRoot()) {
      int waterExtracted = this.waterTank
          .internalExtract(FluidResource.of(Fluids.WATER), waterCost, tx);
      if (waterExtracted == 0) {
        return 0;
      }

      waterCost = Math.min(waterCost, waterExtracted);

      var steam = new FluidStack(RailcraftFluids.STEAM.get(),
          SteamConstants.STEAM_PER_UNIT_WATER * waterCost);

      this.steamTank.internalInsert(FluidResource.of(steam), steam.getAmount(), tx);
      tx.commit();
      return steam.getAmount();
    }
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    valueOutput.putFloat(CompoundTagKeys.TEMPERATURE, this.temperature);
    valueOutput.putFloat(CompoundTagKeys.MAX_TEMPERATURE, this.maxTemperature);
    valueOutput.putFloat(CompoundTagKeys.BURN_TIME, this.burnTime);
    valueOutput.putFloat(CompoundTagKeys.CURRENT_ITEM_BURN_TIME, this.currentItemBurnTime);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    this.setTemperature(valueInput.getFloatOr(CompoundTagKeys.TEMPERATURE, SteamConstants.COLD_TEMP));
    this.setMaxTemperature(valueInput.getFloatOr(CompoundTagKeys.MAX_TEMPERATURE, SteamConstants.MAX_HEAT_LOW));
    this.setBurnTime(valueInput.getFloatOr(CompoundTagKeys.BURN_TIME, 0));
    this.setCurrentItemBurnTime(valueInput.getFloatOr(CompoundTagKeys.CURRENT_ITEM_BURN_TIME, 0));
  }

  public Gauge getTemperatureGauge() {
    return this.temperatureGauge;
  }

  public float getCurrentItemBurnTime() {
    return this.currentItemBurnTime;
  }

  public void setCurrentItemBurnTime(float currentItemBurnTime) {
    this.currentItemBurnTime = currentItemBurnTime;
  }

  public float getBurnTime() {
    return this.burnTime;
  }

  public void setBurnTime(float burnTime) {
    this.burnTime = burnTime;
  }

  private class TemperatureGauge implements Gauge {

    @Nullable
    private List<ClientTooltipComponent> tooltip;

    @Override
    public void refresh() {
      this.tooltip = List.of(ClientTooltipComponent.create(
          Component.literal(String.format("%.0f°C", SteamBoiler.this.getTemperature()))
              .getVisualOrderText()));
    }

    @Override
    public List<ClientTooltipComponent> getTooltip() {
      return this.tooltip == null ? Collections.emptyList() : this.tooltip;
    }

    @Override
    public float getMeasurement() {
      return (SteamBoiler.this.getTemperature() - SteamConstants.COLD_TEMP)
          / (SteamBoiler.this.getMaxTemperature() - SteamConstants.COLD_TEMP);
    }

    @Override
    public float getServerValue() {
      return SteamBoiler.this.getTemperature();
    }

    @Override
    public float getClientValue() {
      return SteamBoiler.this.getTemperature();
    }

    @Override
    public void setClientValue(float value) {
      SteamBoiler.this.setTemperature(value);
    }
  }
}
