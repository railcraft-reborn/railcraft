package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import java.util.Optional;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.SwitchTrackRouterMenu;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SwitchTrackRouterScreen extends RailcraftMenuScreen<SwitchTrackRouterMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      Railcraft.rl("textures/gui/container/routing.png");
  private static final Component ROUTING_TABLE =
      Component.translatable(Translations.Screen.ROUTING_TABLE_BOOK);
  private static final int REFRESH_INTERVAL_TICKS = 20;
  private final SwitchTrackRouterBlockEntity switchTrackRouter;

  private MultiButton<SwitchTrackRouterBlockEntity.Lock> lockButton;
  private MultiButton<SwitchTrackRouterBlockEntity.Railway> railwayButton;
  private int refreshTimer;

  public SwitchTrackRouterScreen(SwitchTrackRouterMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.imageHeight = 158;
    this.imageWidth = 176;
    this.inventoryLabelY = this.imageHeight - 94;
    this.switchTrackRouter = menu.getSwitchTrackRouter();

    this.registerWidgetRenderer(new WidgetRenderer<>(menu.getErrorWidget()) {
      @Override
      public List<Component> getTooltip() {
        return menu.getSwitchTrackRouter().logicError()
            .map(RoutingLogicException::getTooltip)
            .orElse(null);
      }

      @Override
      public void render(ResourceLocation widgetLocation, GuiGraphics guiGraphics, int centreX,
          int centreY, int mouseX, int mouseY) {
        if (this.getTooltip() != null) {
          super.render(widgetLocation, guiGraphics, centreX, centreY, mouseX, mouseY);
        }
      }
    });
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return BACKGROUND_TEXTURE;
  }

  @Override
  protected void init() {
    super.init();
    this.lockButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.switchTrackRouter.getLock())
        .bounds(this.leftPos + 152, this.topPos + 8, 16, 16)
        .tooltipFactory(this::updateLockButtonTooltip)
        .stateCallback(this::setLock)
        .build());
    this.railwayButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.switchTrackRouter.getRailway())
        .bounds(this.leftPos + 68, this.topPos + 50, 100, 16)
        .tooltipFactory(this::updateRailwayButtonTooltip)
        .stateCallback(this::setRailway)
        .build());
    this.updateButtons();
  }

  private void setLock(SwitchTrackRouterBlockEntity.Lock lock) {
    if (this.switchTrackRouter.getLock() != lock) {
      this.switchTrackRouter.setLock(
          lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
          ? null : this.minecraft.player.getGameProfile());
      this.sendAttributes();
    }
  }

  private void setRailway(SwitchTrackRouterBlockEntity.Railway railway) {
    if (this.switchTrackRouter.getRailway() != railway) {
      this.switchTrackRouter.setRailway(
          railway.equals(SwitchTrackRouterBlockEntity.Railway.PUBLIC)
          ? null : this.minecraft.player.getGameProfile());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(SwitchTrackRouterBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.switchTrackRouter.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  private Optional<Tooltip> updateRailwayButtonTooltip(
      SwitchTrackRouterBlockEntity.Railway railway) {
    return Optional.of(Tooltip.create(switch (railway) {
      case PRIVATE -> Component.translatable(Translations.Screen.SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY_DESC,
          this.switchTrackRouter.getOwnerOrThrow().getName());
      case PUBLIC -> Component.translatable(Translations.Screen.SWITCH_TRACK_ROUTER_PUBLIC_RAILWAY_DESC);
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
    var canAccess = this.switchTrackRouter.canAccess(this.minecraft.player.getGameProfile());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.switchTrackRouter.getLock());
    this.railwayButton.active = canAccess;
    this.railwayButton.setState(this.switchTrackRouter.getRailway());
  }

  private void sendAttributes() {
    if (!this.switchTrackRouter.canAccess(this.minecraft.player.getGameProfile())) {
      return;
    }
    PacketHandler.sendToServer(
        new SetSwitchTrackRouterMessage(this.switchTrackRouter.getBlockPos(),
            this.railwayButton.getState(), this.lockButton.getState()));
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);
    guiGraphics.drawString(this.font, ROUTING_TABLE, 64, 29, 4210752, false);
  }
}
