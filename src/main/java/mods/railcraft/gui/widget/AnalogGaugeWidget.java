package mods.railcraft.gui.widget;

public class AnalogGaugeWidget extends GaugeWidget {

  public final int ox;
  public final int oy;
  public final int ou;
  public final int ov;

  public AnalogGaugeWidget(Gauge controller, int x, int y, int w, int h, int ox, int oy, int ou,
      int ov) {
    super(controller, x, y, 0, 0, w, h);
    this.ox = ox;
    this.oy = oy;
    this.ou = ou;
    this.ov = ov;
  }

  public AnalogGaugeWidget(Gauge controller, int x, int y, int w, int h, int ox, int oy) {
    this(controller, x, y, w, h, ox, oy, ox, oy);
  }

  public AnalogGaugeWidget(Gauge controller, int x, int y, int w, int h, int ox, int oy, int ou,
      int ov, boolean vertical) {
    super(controller, x, y, 0, 0, w, h, vertical);
    this.ox = ox;
    this.oy = oy;
    this.ou = ou;
    this.ov = ov;
  }

  public AnalogGaugeWidget(Gauge controller, int x, int y, int w, int h, int ox, int oy,
      boolean vertical) {
    this(controller, x, y, w, h, ox, oy, ox, oy, vertical);
  }
}
