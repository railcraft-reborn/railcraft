package mods.railcraft.gui.widget;

import mods.railcraft.api.charge.Charge;
import mods.railcraft.charge.ChargeNetworkImpl;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public class ChargeNetworkIndicator implements Gauge {

  private float chargePercent;
  private List<Component> tooltip = Collections.emptyList();

  @Nullable
  private final ServerLevel level;
  private final BlockPos pos;

  public ChargeNetworkIndicator(BlockPos pos) {
    this(null, pos);
  }

  public ChargeNetworkIndicator(@Nullable ServerLevel level, BlockPos pos) {
    this.level = level;
    this.pos = pos;
  }

  @Override
  public void refresh() {
    this.tooltip = List.of(Component.literal(String.format("%.0f%%", this.chargePercent * 100.0)));
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
  public void setClientValue(float value) {
    this.chargePercent = value;
  }

  @Override
  public float getClientValue() {
    return this.chargePercent;
  }
}
