package mods.railcraft.world.level.block.charge;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ZincCarbonBatteryBlock extends DisposableBatteryBlock {

  private static final Spec CHARGE_SPEC = BatterySpecs.ZINC_CARBON.buildSpec();

  public ZincCarbonBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected DeferredHolder<Block, EmptyBatteryBlock> getBatteryBlockEmpty() {
    return RailcraftBlocks.ZINC_CARBON_BATTERY_EMPTY;
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }
}
