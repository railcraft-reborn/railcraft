package mods.railcraft.world.level.block.charge;

import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ZincSilverBatteryBlock extends DisposableBatteryBlock {

  private static final Spec CHARGE_SPEC = BatterySpecs.ZINC_SILVER.buildSpec();

  public ZincSilverBatteryBlock(Properties properties) {
    super(properties);
  }

  @Override
  protected DeferredHolder<Block, EmptyBatteryBlock> getBatteryBlockEmpty() {
    return RailcraftBlocks.ZINC_SILVER_BATTERY_EMPTY;
  }

  @Override
  protected Spec getChargeSpec() {
    return CHARGE_SPEC;
  }
}
