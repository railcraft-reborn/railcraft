package mods.railcraft.client.gui.screen.inventory;

import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.screen.IngameWindowScreen;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.to_server.SetMaintenanceMinecartMessage;
import mods.railcraft.world.entity.vehicle.MaintenanceMinecart;
import mods.railcraft.world.entity.vehicle.MaintenancePatternMinecart;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public abstract class MaintenanceMinecartScreen<T extends RailcraftMenu> extends RailcraftMenuScreen<T> {

  protected static final Component PATTERN = Component.translatable(Translations.Screen.PATTERN);
  protected static final Component STOCK = Component.translatable(Translations.Screen.STOCK);
  private static final int REFRESH_INTERVAL_TICKS = SharedConstants.TICKS_PER_SECOND;
  private final MaintenancePatternMinecart cart;
  private MultiButton<MaintenanceMinecart.Mode> mode;
  private int refreshTimer;

  protected MaintenanceMinecartScreen(T menu, Inventory inventory, Component title, int imageHeight,
      MaintenancePatternMinecart cart) {
    super(menu, inventory, title, imageHeight);
    this.cart = cart;
  }

  protected MaintenanceMinecartScreen(T menu, Inventory inventory, Component title,
      MaintenancePatternMinecart cart) {
    super(menu, inventory, title);
    this.cart = cart;
  }

  @Override
  protected void init() {
    super.init();
    var centreX = (this.width - this.getImageWidth()) / 2;
    var centreY = (this.height - this.getImageHeight()) / 2;

    this.mode = this.addRenderableWidget(
        MultiButton
            .builder(ButtonTexture.SMALL_BUTTON, this.cart.mode())
            .bounds(centreX + 120, centreY + getImageHeight() - 100, 40, 16)
            .stateCallback(this::setMaintenanceMode)
            .tooltipFactory(this::createLockTooltip)
            .build());
    this.updateButtons();
  }

  private Optional<Tooltip> createLockTooltip(MaintenanceMinecart.Mode mode) {
    return Optional.of(Tooltip.create(Component.translatable(mode.getTipsKey())));
  }

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.updateButtons();
    }
  }

  private void setMaintenanceMode(MaintenanceMinecart.Mode mode) {
    if (mode != this.cart.mode()) {
      this.cart.setMode(mode);
      this.updateButtons();
      ClientPacketDistributor.sendToServer(
          new SetMaintenanceMinecartMessage(this.cart.getId(), mode));
    }
  }

  private void updateButtons() {
    this.mode.setState(cart.mode());
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, PATTERN, 38, 30, IngameWindowScreen.TEXT_COLOR, false);
    graphics.text(this.font, STOCK, 125, 25, IngameWindowScreen.TEXT_COLOR, false);
  }
}
