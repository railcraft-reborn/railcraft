package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.charge.ChargeNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class ChargeNetworkUtilizationIndicator implements Gauge {

  @Nullable
  protected final ServerLevel level;
  protected final BlockPos pos;
  private float chargePercent;
  private final List<Component> tooltip = new ArrayList<>(1);

  public ChargeNetworkUtilizationIndicator(@Nullable ServerLevel level, BlockPos pos) {
    this.level = level;
    this.pos = pos;
  }

  @Override
  public void refresh() {
    this.tooltip.clear();
    this.tooltip.add(Component.literal(String.format("%.0f%%", this.chargePercent * 100.0)));
  }

  @Override
  public List<Component> getTooltip() {
    return this.tooltip;
  }

  @Override
  public float getServerValue() {
    return ((ChargeNetworkImpl) Charge.distribution.network(this.level))
        .grid(this.pos).getUtilization();
  }

  @Override
  public float getClientValue() {
    return this.chargePercent;
  }

  @Override
  public void setClientValue(float value) {
    this.chargePercent = value;
  }
}
