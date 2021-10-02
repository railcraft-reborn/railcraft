package mods.railcraft.gui.widget;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class GaugeWidget extends Widget {

  private final Gauge gauge;
  private final boolean vertical;
  private final Map<ServerPlayerEntity, Float> previousValues = new HashMap<>();
  private boolean first = true;
  private float measurement;

  public GaugeWidget(Gauge gauge, int x, int y, int u, int v, int w, int h) {
    this(gauge, x, y, u, v, w, h, true);
  }

  public GaugeWidget(Gauge gauge, int x, int y, int u, int v, int w, int h,
      boolean vertical) {
    super(x, y, u, v, w, h);
    this.gauge = gauge;
    this.vertical = vertical;
  }

  public final float getMeasurement() {
    if (this.first) {
      this.measurement = this.gauge.getMeasurement();
      this.first = false;
    } else {
      this.measurement = (this.gauge.getMeasurement() - this.measurement) * 0.1F + this.measurement;
    }

    return this.measurement;
  }

  public Gauge getGauge() {
    return this.gauge;
  }

  public boolean isVertical() {
    return this.vertical;
  }

  @Override
  public boolean hasServerSyncData(ServerPlayerEntity listener) {
    float previousValue = this.previousValues.getOrDefault(listener, 0.0F);
    return previousValue != this.gauge.getServerValue();
  }

  @Override
  public void writeServerSyncData(ServerPlayerEntity listener, PacketBuffer data) {
    float value = this.gauge.getServerValue();
    data.writeFloat(value);
    this.previousValues.put(listener, value);
  }

  @Override
  public void readServerSyncData(PacketBuffer data) {
    this.gauge.setClientValue(data.readFloat());
  }
}
