package mods.railcraft.gui.widget;

import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class GaugeWidget extends Widget {

  private final Gauge gauge;
  private final boolean vertical;
  private final Object2FloatMap<ServerPlayer> previousValues = new Object2FloatOpenHashMap<>();
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
    this.gauge.refresh();
  }
}
