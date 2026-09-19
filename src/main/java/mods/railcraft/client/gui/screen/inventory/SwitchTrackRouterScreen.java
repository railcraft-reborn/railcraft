package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.to_server.SetSwitchTrackRouterMessage;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.SwitchTrackRouterMenu;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class SwitchTrackRouterScreen extends RailcraftMenuScreen<SwitchTrackRouterMenu> {

  private static final Identifier BACKGROUND_TEXTURE =
      RailcraftConstants.id("textures/gui/container/routing.png");
  private static final Component ROUTING_TABLE =
      Component.translatable(Translations.Screen.ROUTING_TABLE_BOOK);
  private static final int REFRESH_INTERVAL_TICKS = SharedConstants.TICKS_PER_SECOND;
  private final SwitchTrackRouterBlockEntity switchTrackRouter;

  private MultiButton<SwitchTrackRouterBlockEntity.Lock> lockButton;
  private MultiButton<SwitchTrackRouterBlockEntity.Railway> railwayButton;
  private int refreshTimer;

  public SwitchTrackRouterScreen(SwitchTrackRouterMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title, 176, 158);
    this.inventoryLabelY = this.imageHeight - 94;
    this.switchTrackRouter = menu.getSwitchTrackRouter();

    this.registerWidgetRenderer(new WidgetRenderer<>(menu.getErrorWidget()) {
      @Override
      public List<ClientTooltipComponent> getTooltip() {
        return menu.getSwitchTrackRouter().logicError()
            .map(RoutingLogicException::getTooltip)
            .orElse(List.of());
      }

      @Override
      public void render(Identifier widgetLocation, GuiGraphicsExtractor graphics, int centreX,
          int centreY, int mouseX, int mouseY) {
        if (!this.getTooltip().isEmpty()) {
          super.render(widgetLocation, graphics, centreX, centreY, mouseX, mouseY);
        }
      }
    });
  }

  @Override
  public Identifier getWidgetsTexture() {
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
          ? null : this.minecraft.player.nameAndId());
      this.sendAttributes();
    }
  }

  private void setRailway(SwitchTrackRouterBlockEntity.Railway railway) {
    if (this.switchTrackRouter.getRailway() != railway) {
      this.switchTrackRouter.setRailway(
          railway.equals(SwitchTrackRouterBlockEntity.Railway.PUBLIC)
          ? null : this.minecraft.player.nameAndId());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(SwitchTrackRouterBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.switchTrackRouter.getOwnerOrThrow().name());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  private Optional<Tooltip> updateRailwayButtonTooltip(
      SwitchTrackRouterBlockEntity.Railway railway) {
    return Optional.of(Tooltip.create(switch (railway) {
      case PRIVATE -> Component.translatable(Translations.Screen.SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY_DESC,
          this.switchTrackRouter.getOwnerOrThrow().name());
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
    var canAccess = this.switchTrackRouter.canAccess(this.minecraft.player.nameAndId());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.switchTrackRouter.getLock());
    this.railwayButton.active = canAccess;
    this.railwayButton.setState(this.switchTrackRouter.getRailway());
  }

  private void sendAttributes() {
    if (!this.switchTrackRouter.canAccess(this.minecraft.player.nameAndId())) {
      return;
    }
    ClientPacketDistributor.sendToServer(
        new SetSwitchTrackRouterMessage(this.switchTrackRouter.getBlockPos(),
            this.railwayButton.getState(), this.lockButton.getState()));
  }

  @Override
  protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {
    super.extractLabels(graphics, xm, ym);
    graphics.text(this.font, ROUTING_TABLE, 64, 29, 4210752, false);
  }
}
