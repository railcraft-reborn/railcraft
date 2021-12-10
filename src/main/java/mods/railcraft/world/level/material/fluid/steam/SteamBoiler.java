package mods.railcraft.world.level.material.fluid.steam;

import java.util.Collections;
import java.util.List;
import com.google.common.primitives.Floats;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.gui.widget.Gauge;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntity;
import mods.railcraft.world.level.material.fluid.FuelProvider;
import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler.FluidAction;

/**
 * The boiler itself. Used to simulate turning water into steam.
 * 
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class SteamBoiler implements INBTSerializable<CompoundTag> {

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
  private RailcraftBlockEntity tile;
  private FuelProvider fuelProvider;

  public SteamBoiler(StandardTank waterTank, StandardTank tankSteam) {
    this.waterTank = waterTank;
    this.steamTank = tankSteam;
  }

  public IFluidTank getWaterTank() {
    return this.waterTank;
  }

  public IFluidTank getSteamTank() {
    return this.steamTank;
  }

  public SteamBoiler setFuelProvider(FuelProvider fuelProvider) {
    this.fuelProvider = fuelProvider;
    return this;
  }

  public SteamBoiler setTile(RailcraftBlockEntity tile) {
    this.tile = tile;
    return this;
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

  public SteamBoiler setMaxTemperature(float maxTemperature) {
    this.maxTemperature = maxTemperature;
    this.temperatureGauge.refresh();
    return this;
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
    this.temperatureGauge.refresh();
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

  private boolean addFuel() {
    if (this.fuelProvider == null) {
      return false;
    }
    float fuel = this.fuelProvider.consumeFuel();
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
    fuel *= this.efficiencyModifier;
    fuel *= RailcraftConfig.server.fuelPerSteamMultiplier.get();
    return fuel;
  }

  public void tick(int numTanks) {
    this.burnCycle++;
    if (this.burnCycle >= this.ticksPerCycle) {
      this.burnCycle = 0;
      float fuelNeeded = this.getFuelPerCycle(numTanks);
      while (this.getBurnTime() < fuelNeeded) {
        boolean addedFuel = addFuel();
        if (!addedFuel) {
          break;
        }
      }
      boolean wasBurning = this.burning;
      this.burning = this.getBurnTime() >= fuelNeeded;
      if (this.burning) {
        this.setBurnTime(this.getBurnTime() - fuelNeeded);
      }
      if (this.tile != null && this.burning != wasBurning) {
        this.tile.syncToClient();
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

    FluidStack water = this.waterTank.drain(waterCost, FluidAction.SIMULATE);
    if (water.isEmpty()) {
      return 0;
    }

    waterCost = Math.min(waterCost, water.getAmount());

    FluidStack steam = new FluidStack(RailcraftFluids.STEAM.get(),
        SteamConstants.STEAM_PER_UNIT_WATER * waterCost);

    this.waterTank.internalDrain(waterCost, FluidAction.EXECUTE);
    this.steamTank.internalFill(steam, FluidAction.EXECUTE);

    return steam.getAmount();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag tag = new CompoundTag();
    tag.putFloat("temperature", this.temperature);
    tag.putFloat("maxTemperature", this.maxTemperature);
    tag.putFloat("burnTime", this.burnTime);
    tag.putFloat("currentItemBurnTime", this.currentItemBurnTime);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.setTemperature(tag.getFloat("temperature"));
    this.setMaxTemperature(tag.getFloat("maxTemperature"));
    this.setBurnTime(tag.getFloat("burnTime"));
    this.setCurrentItemBurnTime(tag.getFloat("currentItemBurnTime"));
  }

  public Gauge getTemperatureGauge() {
    return this.temperatureGauge;
  }

  public float getCurrentItemBurnTime() {
    return this.currentItemBurnTime;
  }

  public float setCurrentItemBurnTime(float currentItemBurnTime) {
    this.currentItemBurnTime = currentItemBurnTime;
    return currentItemBurnTime;
  }

  public float getBurnTime() {
    return this.burnTime;
  }

  public double setBurnTime(float burnTime) {
    this.burnTime = burnTime;
    return burnTime;
  }

  private class TemperatureGauge implements Gauge {

    private List<Component> tooltip;

    @Override
    public void refresh() {
      this.tooltip = Collections.singletonList(
          new TextComponent(String.format("%.0f°", SteamBoiler.this.getTemperature())));
    }

    @Override
    public List<Component> getTooltip() {
      return this.tooltip;
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
