package mods.railcraft.gui.widget;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.charge.ChargeNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class ChargeNetworkLevelIndicator extends ChargeNetworkUtilizationIndicator {

  public ChargeNetworkLevelIndicator(@Nullable Level level, BlockPos pos) {
    super(level instanceof ServerLevel serverLevel ? serverLevel : null, pos);
  }

  @Override
  public float getServerValue() {
    return ((ChargeNetworkImpl) Charge.distribution.network(this.level))
        .grid(this.pos).getChargeLevel();
  }
}
