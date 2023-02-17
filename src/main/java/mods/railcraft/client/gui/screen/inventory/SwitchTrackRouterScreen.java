package mods.railcraft.client.gui.screen.inventory;

import java.util.List;
import java.util.Optional;
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSwitchTrackRouterAttributesMessage;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.SwitchTrackRouterMenu;
import mods.railcraft.world.level.block.entity.SwitchTrackRouterBlockEntity;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SwitchTrackRouterScreen extends RailcraftMenuScreen<SwitchTrackRouterMenu> {

  private static final ResourceLocation BACKGROUND_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/routing.png");
  private static final Component ROUTING_TABLE =
      Component.translatable(Translations.Screen.ROUTING_TABLE_BOOK);
  private static final int REFRESH_INTERVAL_TICKS = 20;
  private final SwitchTrackRouterBlockEntity switchTrackRouting;

  private MultiButton<SwitchTrackRouterBlockEntity.Lock> lockButton;
  private MultiButton<SwitchTrackRouterBlockEntity.Railway> railwayButton;
  private int refreshTimer;

  public SwitchTrackRouterScreen(SwitchTrackRouterMenu menu, Inventory inventory,
      Component title) {
    super(menu, inventory, title);
    this.imageHeight = 158;
    this.imageWidth = 176;
    this.inventoryLabelY = this.imageHeight - 94;
    this.switchTrackRouting = menu.getSwitchTrackRouting();

    this.registerWidgetRenderer(new WidgetRenderer<>(menu.getErrorWidget()) {
      @Override
      public List<Component> getTooltip() {
        return menu.getLogic()
            .map(RoutingLogic::getError)
            .map(RoutingLogicException::getToolTip)
            .orElse(null);
      }

      @Override
      public void render(RailcraftMenuScreen<?> screen, PoseStack poseStack, int centreX,
          int centreY, int mouseX, int mouseY) {
        if (getTooltip() != null) {
          super.render(screen, poseStack, centreX, centreY, mouseX, mouseY);
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
        .builder(ButtonTexture.SMALL_BUTTON, this.switchTrackRouting.getLock())
        .bounds(this.leftPos + 152, this.topPos + 8, 16, 16)
        .tooltipFactory(this::updateLockButtonTooltip)
        .stateCallback(this::setLock)
        .build());
    this.railwayButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.switchTrackRouting.getRailway())
        .bounds(this.leftPos + 68, this.topPos + 50, 100, 16)
        .tooltipFactory(this::updateRailwayButtonTooltip)
        .stateCallback(this::setRailway)
        .build());
    this.updateButtons();
  }

  private void setLock(SwitchTrackRouterBlockEntity.Lock lock) {
    if (this.switchTrackRouting.getLock() != lock) {
      this.switchTrackRouting.setLock(
          lock.equals(SwitchTrackRouterBlockEntity.Lock.UNLOCKED)
          ? null : this.minecraft.getUser().getGameProfile());
      this.sendAttributes();
    }
  }

  private void setRailway(SwitchTrackRouterBlockEntity.Railway railway) {
    if (this.switchTrackRouting.getRailway() != railway) {
      this.switchTrackRouting.setRailway(
          railway.equals(SwitchTrackRouterBlockEntity.Railway.PUBLIC)
          ? null : this.minecraft.getUser().getGameProfile());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(SwitchTrackRouterBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.switchTrackRouting.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  private Optional<Tooltip> updateRailwayButtonTooltip(
      SwitchTrackRouterBlockEntity.Railway railway) {
    return Optional.of(Tooltip.create(switch (railway) {
      case PRIVATE -> Component.translatable(Translations.Screen.SWITCH_TRACK_ROUTER_PRIVATE_RAILWAY_DESC,
          this.switchTrackRouting.getOwnerOrThrow().getName());
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
    var canAccess = this.switchTrackRouting.canAccess(this.minecraft.getUser().getGameProfile());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.switchTrackRouting.getLock());
    this.railwayButton.active = canAccess;
    this.railwayButton.setState(this.switchTrackRouting.getRailway());
  }

  private void sendAttributes() {
    if (!this.switchTrackRouting.canAccess(this.minecraft.getUser().getGameProfile())) {
      return;
    }
    NetworkChannel.GAME.sendToServer(
        new SetSwitchTrackRouterAttributesMessage(this.switchTrackRouting.getBlockPos(),
            this.railwayButton.getState(), this.lockButton.getState()));
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    super.renderLabels(poseStack, mouseX, mouseY);
    this.font.draw(poseStack, ROUTING_TABLE, 64, 29, 4210752);
  }
}
