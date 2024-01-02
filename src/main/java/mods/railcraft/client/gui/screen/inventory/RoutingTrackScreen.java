package mods.railcraft.client.gui.screen.inventory;

import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetRoutingTrackMessage;
import mods.railcraft.world.inventory.RoutingTrackMenu;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import mods.railcraft.world.level.block.entity.track.RoutingTrackBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RoutingTrackScreen extends RailcraftMenuScreen<RoutingTrackMenu> {

  private static final int REFRESH_INTERVAL_TICKS = 20;
  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      RailcraftConstants.rl("textures/gui/container/routing_track.png");
  private final RoutingTrackBlockEntity routingBlockEntity;
  private MultiButton<LockableSwitchTrackActuatorBlockEntity.Lock> lockButton;
  private int refreshTimer;

  public RoutingTrackScreen(RoutingTrackMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
    this.routingBlockEntity = menu.getRoutingBlockEntity();

    this.imageHeight = 140;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  @Override
  protected void init() {
    super.init();
    this.lockButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.routingBlockEntity.getLock())
        .bounds(this.leftPos + 152, this.topPos + 8, 16, 16)
        .tooltipFactory(this::updateLockButtonTooltip)
        .stateCallback(this::setLock)
        .build());
    this.updateButtons();
  }

  private void setLock(SwitchTrackRouterBlockEntity.Lock lock) {
    if (this.routingBlockEntity.getLock() != lock) {
      this.routingBlockEntity.setLock(
          lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
              ? null : this.minecraft.player.getGameProfile());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(SwitchTrackRouterBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.routingBlockEntity.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  @Override
  protected void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.refreshTimer = 0;
      this.updateButtons();
    }
  }

  private void updateButtons() {
    var canAccess = this.routingBlockEntity.canAccess(this.minecraft.player.getGameProfile());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.routingBlockEntity.getLock());
  }

  private void sendAttributes() {
    if (!this.routingBlockEntity.canAccess(this.minecraft.player.getGameProfile())) {
      return;
    }
    PacketHandler.sendToServer(
        new SetRoutingTrackMessage(this.routingBlockEntity.getBlockPos(),
            this.lockButton.getState()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, Component.translatable(Translations.Screen.GOLDEN_TICKET_TITLE), 64,
        29, 0x404040, false);
  }
}
