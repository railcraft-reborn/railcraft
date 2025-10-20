package mods.railcraft.world.module;

import java.util.concurrent.atomic.AtomicReference;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.material.StandardTank;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

public class SteamTurbineModule extends ChargeModule<SteamTurbineBlockEntity> {

  private static final int ROTOR_DAMAGE_CHANCE = 200;

  public static final int CHARGE_OUTPUT = 225;
  private static final int STEAM_USAGE = 360;
  public static final int TANK_STEAM = 0;
  public static final int TANK_WATER = 1;

  private final StandardTank steamTank;
  private final StandardTank waterTank;

  private final AdvancedContainer rotorContainer = new AdvancedContainer(1);
  private float operatingRatio;
  private int energy;

  private final ResourceHandler<FluidResource> fluidHandler = new FluidHandler();

  public SteamTurbineModule(SteamTurbineBlockEntity provider, Charge network) {
    super(provider, network);
    this.rotorContainer.listener(this.provider);
    this.steamTank = StandardTank.ofBuckets(4)
        .filter(RailcraftTags.Fluids.STEAM)
        .disableExtract()
        .changeCallback(provider::setChanged);
    this.waterTank = StandardTank.ofBuckets(4)
        .filter(FluidTags.WATER)
        .disableInsert()
        .changeCallback(provider::setChanged);
  }

  public ResourceHandler<FluidResource> getFluidHandler() {
    return this.fluidHandler;
  }

  @Override
  public void serverTick() {
    super.serverTick();
    var addedEnergy = false;
    if (this.energy < CHARGE_OUTPUT && !this.steamTank.getFluidStack().isEmpty()) {
      try (var tx = Transaction.openRoot()) {
        var steamExtracted =
            this.steamTank.internalExtract(this.steamTank.getResource(0), STEAM_USAGE, tx);
        if (steamExtracted >= STEAM_USAGE) {
          var rotorStack = this.rotorContainer.getItem(0);
          if (rotorStack.is(RailcraftItems.TURBINE_ROTOR.get())) {
            addedEnergy = true;
            this.energy += CHARGE_OUTPUT;
            this.waterTank.internalInsert(FluidResource.of(Fluids.WATER), 2, tx);
            tx.commit();
            this.rotorContainer.setItem(0, useRotor((ServerLevel) this.provider.level(), rotorStack));
          }
        }
      }
    }

    var thisTick = addedEnergy ? 1 : 0;
    this.operatingRatio = (thisTick - this.operatingRatio) * 0.05F + this.operatingRatio;

    var chargeStorage = this.storage().get();
    if (!chargeStorage.isFull()) {
      try (var tx = Transaction.openRoot()) {
        chargeStorage.insert(this.energy, tx);
        tx.commit();
        this.energy = 0;
      }
    }
  }

  public float getOperatingRatio() {
    return this.operatingRatio;
  }

  public Container getRotorContainer() {
    return this.rotorContainer;
  }

  public boolean needsMaintenance() {
    var rotorStack = this.rotorContainer.getItem(0);
    return rotorStack.isEmpty()
        || !rotorStack.is(RailcraftItems.TURBINE_ROTOR.get())
        || rotorStack.getDamageValue() / (float) rotorStack.getMaxDamage() > 0.75F;
  }

  @Override
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putChild(CompoundTagKeys.STEAM_TANK, this.steamTank);
    valueOutput.putChild(CompoundTagKeys.WATER_TANK, this.waterTank);
    valueOutput.putChild(CompoundTagKeys.ROTOR_CONTAINER, this.rotorContainer);
    valueOutput.putInt(CompoundTagKeys.ENERGY, this.energy);
    valueOutput.putFloat(CompoundTagKeys.OPERATING_RATIO, this.operatingRatio);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    valueInput.readChild(CompoundTagKeys.STEAM_TANK, this.steamTank);
    valueInput.readChild(CompoundTagKeys.WATER_TANK, this.waterTank);
    valueInput.readChild(CompoundTagKeys.ROTOR_CONTAINER, this.rotorContainer);
    this.energy = valueInput.getIntOr(CompoundTagKeys.ENERGY, 0);
    this.operatingRatio = valueInput.getFloatOr(CompoundTagKeys.OPERATING_RATIO, 0);
  }

  private static ItemStack useRotor(ServerLevel level, ItemStack stack) {
    var random = level.getRandom();
    if (random.nextInt(ROTOR_DAMAGE_CHANCE) == 0) {
      var result = new AtomicReference<>(stack);
      stack.hurtAndBreak(1, level, null, item -> result.set(ItemStack.EMPTY));
      return result.get();
    } else {
      return stack;
    }
  }

  private class FluidHandler implements ResourceHandler<FluidResource> {

    @Override
    public int size() {
      return 2;
    }

    private StandardTank getTank(int index) {
      return index == 0 ? SteamTurbineModule.this.steamTank : SteamTurbineModule.this.waterTank;
    }

    @Override
    public FluidResource getResource(int index) {
      return this.getTank(index).getResource(0);
    }

    @Override
    public long getAmountAsLong(int index) {
      return this.getTank(index).getAmountAsLong(0);
    }

    @Override
    public long getCapacityAsLong(int index, FluidResource resource) {
      return this.getTank(index).getCapacityAsLong(0, resource);
    }

    @Override
    public boolean isValid(int index, FluidResource resource) {
      return this.getTank(index).isValid(0, resource);
    }

    @Override
    public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
      return SteamTurbineModule.this.steamTank.insert(index, resource, amount, transaction);
    }

    @Override
    public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
      return SteamTurbineModule.this.waterTank.extract(index, resource, amount, transaction);
    }
  }
}
