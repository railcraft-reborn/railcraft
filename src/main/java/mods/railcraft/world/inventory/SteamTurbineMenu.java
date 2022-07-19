package mods.railcraft.world.inventory;

import mods.railcraft.gui.widget.AnalogGaugeWidget;
import mods.railcraft.gui.widget.ChargeNetworkIndicator;
import mods.railcraft.gui.widget.Gauge;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.SteamTurbineBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;
import java.util.List;

public class SteamTurbineMenu extends RailcraftMenu {

  public static final int GUI_HEIGHT = 140;

  private final AnalogGaugeWidget turbineWidget;
  private final AnalogGaugeWidget chargeWidget;

  public SteamTurbineMenu(int id, Inventory inventory, SteamTurbineBlockEntity steamTurbine) {
    super(RailcraftMenuTypes.STEAM_TURBINE.get(), id, inventory.player, steamTurbine::stillValid);

    var module = steamTurbine.getSteamTurbineModule();
    var rotorContainer = module.getRotorContainer();

    this.addWidget(this.turbineWidget = new AnalogGaugeWidget(new Gauge() {

      private float clientValue;
      private List<Component> tooltip = Collections.emptyList();

      {
        this.refresh();
      }

      @Override
      public void refresh() {
        this.tooltip =
            List.of(Component.literal(String.format("%.0f%%", this.clientValue * 100.0)));
      }

      @Override
      public List<Component> getTooltip() {
        return this.tooltip;
      }

      @Override
      public float getServerValue() {
        return module.getOperatingRatio();
      }

      @Override
      public float getClientValue() {
        return this.clientValue;
      }

      @Override
      public void setClientValue(float value) {
        this.clientValue = value;
      }
    }, 137, 19, 28, 14, 99, 65));
    this.addWidget(this.chargeWidget = new AnalogGaugeWidget(
        new ChargeNetworkIndicator(
            steamTurbine.getLevel() instanceof ServerLevel level ? level : null,
            steamTurbine.getBlockPos()),
        137, 38, 28, 14, 99, 65));

    this.addSlot(
        new ItemFilterSlot(StackFilter.of(RailcraftItems.TURBINE_ROTOR.get()), rotorContainer, 0,
            60, 24).setStackLimit(1));

    this.addPlayerSlots(inventory, GUI_HEIGHT);
  }

  public AnalogGaugeWidget getTurbineWidget() {
    return this.turbineWidget;
  }

  public AnalogGaugeWidget getChargeWidget() {
    return this.chargeWidget;
  }
}
