package mods.railcraft.gui.widget;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class GaugeWidget extends Widget {

  private final Gauge gauge;
  private final boolean vertical;
  private final Map<ServerPlayer, Float> previousValues = new HashMap<>();
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
  public boolean requiresSync(ServerPlayer listener) {
    float previousValue = this.previousValues.getOrDefault(listener, 0.0F);
    return previousValue != this.gauge.getServerValue();
  }

  @Override
  public void writeToBuf(ServerPlayer listener, FriendlyByteBuf data) {
    float value = this.gauge.getServerValue();
    data.writeFloat(value);
    this.previousValues.put(listener, value);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    this.gauge.setClientValue(data.readFloat());
  }
}
