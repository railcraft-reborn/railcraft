package mods.railcraft.integrations.buildcraft;

import java.util.Optional;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.world.level.block.entity.BlastFurnaceBlockEntity;
import mods.railcraft.world.level.block.entity.CokeOvenBlockEntity;
import mods.railcraft.world.level.block.entity.CrusherBlockEntity;
import mods.railcraft.world.level.block.entity.HobbyistSteamEngineBlockEntity;
import mods.railcraft.world.level.block.entity.PoweredRollingMachineBlockEntity;
import mods.railcraft.world.level.block.entity.SteamEngineBlockEntity;
import mods.railcraft.world.level.block.entity.SteamOvenBlockEntity;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBlockEntity;
import mods.railcraft.world.level.block.entity.signal.AbstractSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.steamboiler.SteamBoilerBlockEntity;
import mods.railcraft.world.level.material.steam.SteamBoiler;
import mods.railcraft.world.module.CrafterModule;
import mods.railcraft.world.module.FluidFueledSteamBoilerModule;
import mods.railcraft.world.module.SolidFueledSteamBoilerModule;
import mods.railcraft.world.module.SteamBoilerModule;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Reads the state of Railcraft block entities for BuildCraft gates.
 *
 * <p>The {@code is*} methods tell which statements a block entity supports (used to populate the
 * gate GUI), the other methods read or change the actual state. Multiblocks are resolved to their
 * master, so a gate can be attached to any block of the structure.
 */
final class GateTargets {

  private GateTargets() {}

  // ---------------------------------------------------------------------------
  // Work
  // ---------------------------------------------------------------------------

  static boolean isWorker(BlockEntity blockEntity) {
    return blockEntity instanceof ManipulatorBlockEntity
        || blockEntity instanceof PoweredRollingMachineBlockEntity
        || isCrafter(blockEntity);
  }

  static boolean hasWork(BlockEntity blockEntity) {
    if (blockEntity instanceof ManipulatorBlockEntity manipulator) {
      return manipulator.hasWork();
    }
    if (blockEntity instanceof PoweredRollingMachineBlockEntity rollingMachine) {
      return rollingMachine.isWorking();
    }
    return crafter(blockEntity).map(CrafterModule::isProcessing).orElse(false);
  }

  static void pause(BlockEntity blockEntity) {
    if (blockEntity instanceof ManipulatorBlockEntity manipulator) {
      manipulator.pause();
    } else if (blockEntity instanceof PoweredRollingMachineBlockEntity rollingMachine) {
      rollingMachine.pause();
    } else {
      crafter(blockEntity).ifPresent(CrafterModule::pause);
    }
  }

  private static boolean isCrafter(BlockEntity blockEntity) {
    return blockEntity instanceof CokeOvenBlockEntity
        || blockEntity instanceof BlastFurnaceBlockEntity
        || blockEntity instanceof CrusherBlockEntity
        || blockEntity instanceof SteamOvenBlockEntity;
  }

  private static Optional<? extends CrafterModule<?>> crafter(BlockEntity blockEntity) {
    return switch (blockEntity) {
      case CokeOvenBlockEntity cokeOven -> cokeOven.getMasterBlockEntity()
          .map(CokeOvenBlockEntity::getCokeOvenModule);
      case BlastFurnaceBlockEntity blastFurnace -> blastFurnace.getMasterBlockEntity()
          .map(BlastFurnaceBlockEntity::getBlastFurnaceModule);
      case CrusherBlockEntity crusher -> crusher.getMasterBlockEntity()
          .map(CrusherBlockEntity::getCrusherModule);
      case SteamOvenBlockEntity steamOven -> steamOven.getMasterBlockEntity()
          .map(SteamOvenBlockEntity::getSteamOvenModule);
      default -> Optional.empty();
    };
  }

  // ---------------------------------------------------------------------------
  // Carts
  // ---------------------------------------------------------------------------

  static boolean isCartHandler(BlockEntity blockEntity) {
    return blockEntity instanceof ManipulatorBlockEntity;
  }

  static boolean hasCart(BlockEntity blockEntity) {
    return blockEntity instanceof ManipulatorBlockEntity manipulator && manipulator.hasCart();
  }

  static void sendCart(BlockEntity blockEntity) {
    if (blockEntity instanceof ManipulatorBlockEntity manipulator) {
      manipulator.requestSendCart();
    }
  }

  // ---------------------------------------------------------------------------
  // Fuel
  // ---------------------------------------------------------------------------

  static boolean usesFuel(BlockEntity blockEntity) {
    return blockEntity instanceof SteamBoilerBlockEntity
        || blockEntity instanceof HobbyistSteamEngineBlockEntity
        || blockEntity instanceof CokeOvenBlockEntity
        || blockEntity instanceof BlastFurnaceBlockEntity
        || blockEntity instanceof SteamOvenBlockEntity;
  }

  static boolean needsFuel(BlockEntity blockEntity) {
    return switch (blockEntity) {
      case SteamBoilerBlockEntity boiler -> boiler.getMasterBlockEntity()
          .map(GateTargets::boilerNeedsFuel)
          .orElse(false);
      case HobbyistSteamEngineBlockEntity engine -> engine.getContainer()
          .getItem(HobbyistSteamEngineBlockEntity.SLOT_FUEL).getCount() < 8;
      case CokeOvenBlockEntity cokeOven -> cokeOven.getMasterBlockEntity()
          .map(master -> master.getCokeOvenModule().needsFuel())
          .orElse(false);
      case BlastFurnaceBlockEntity blastFurnace -> blastFurnace.getMasterBlockEntity()
          .map(master -> master.getBlastFurnaceModule().needsFuel())
          .orElse(false);
      case SteamOvenBlockEntity steamOven -> steamOven.getMasterBlockEntity()
          .map(master -> master.getSteamOvenModule().needsFuel())
          .orElse(false);
      default -> false;
    };
  }

  private static boolean boilerNeedsFuel(SteamBoilerBlockEntity master) {
    var solid = master.getModule(SolidFueledSteamBoilerModule.class);
    if (solid.isPresent()) {
      return solid.get().needsFuel();
    }
    return master.getModule(FluidFueledSteamBoilerModule.class)
        .map(FluidFueledSteamBoilerModule::getFuelTank)
        .map(tank -> tank.getFluidAmount() < tank.getCapacity() / 4)
        .orElse(false);
  }

  // ---------------------------------------------------------------------------
  // Temperature
  // ---------------------------------------------------------------------------

  static boolean hasTemperature(BlockEntity blockEntity) {
    return blockEntity instanceof SteamBoilerBlockEntity
        || blockEntity instanceof HobbyistSteamEngineBlockEntity;
  }

  static Optional<Float> temperature(BlockEntity blockEntity) {
    return switch (blockEntity) {
      case SteamBoilerBlockEntity boiler -> boiler.getMasterBlockEntity()
          .flatMap(master -> master.getModule(SteamBoilerModule.class))
          .map(module -> module.getBoiler().getTemperature());
      case HobbyistSteamEngineBlockEntity engine -> Optional.of(engine.getBoiler())
          .map(SteamBoiler::getTemperature);
      default -> Optional.empty();
    };
  }

  // ---------------------------------------------------------------------------
  // Engines
  // ---------------------------------------------------------------------------

  static boolean isEngine(BlockEntity blockEntity) {
    return blockEntity instanceof SteamEngineBlockEntity;
  }

  static Optional<SteamEngineBlockEntity.EnergyStage> energyStage(BlockEntity blockEntity) {
    return blockEntity instanceof SteamEngineBlockEntity engine
        ? Optional.of(engine.getEnergyStage())
        : Optional.empty();
  }

  // ---------------------------------------------------------------------------
  // Maintenance
  // ---------------------------------------------------------------------------

  static boolean needsMaintenanceSupported(BlockEntity blockEntity) {
    return blockEntity instanceof SteamTurbineBlockEntity;
  }

  static boolean needsMaintenance(BlockEntity blockEntity) {
    return blockEntity instanceof SteamTurbineBlockEntity turbine
        && turbine.getMasterBlockEntity()
            .map(master -> master.getSteamTurbineModule().needsMaintenance())
            .orElse(false);
  }

  // ---------------------------------------------------------------------------
  // Signals
  // ---------------------------------------------------------------------------

  static boolean hasSignalAspect(BlockEntity blockEntity) {
    return blockEntity instanceof AbstractSignalBoxBlockEntity
        || blockEntity instanceof AbstractSignalBlockEntity;
  }

  /**
   * @param side the side of the gate's pipe the block entity is attached to
   */
  static Optional<SignalAspect> signalAspect(BlockEntity blockEntity, Direction side) {
    return switch (blockEntity) {
      case AbstractSignalBoxBlockEntity box -> Optional.of(box.getSignalAspect(side.getOpposite()));
      case AbstractSignalBlockEntity signal -> Optional.of(signal.getPrimarySignalAspect());
      default -> Optional.empty();
    };
  }
}
