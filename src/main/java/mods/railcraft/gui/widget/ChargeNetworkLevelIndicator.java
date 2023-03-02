package mods.railcraft.gui.widget;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.charge.ChargeNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public class ChargeNetworkLevelIndicator extends ChargeNetworkUtilizationIndicator {

  public ChargeNetworkLevelIndicator(@Nullable ServerLevel level, BlockPos pos) {
    super(level, pos);
  }

  @Override
  public float getServerValue() {
    return ((ChargeNetworkImpl) Charge.distribution.network(this.level))
        .grid(this.pos).getChargeLevel();
  }
}
