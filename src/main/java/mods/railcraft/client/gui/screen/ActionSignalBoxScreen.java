package mods.railcraft.client.gui.screen;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import mods.railcraft.Translations;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetActionSignalBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.LockableSignalBoxBlockEntity;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class ActionSignalBoxScreen extends IngameWindowScreen {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private final ActionSignalBoxBlockEntity signalBox;

  private final Map<SignalAspect, ToggleButton> signalAspectButtons =
      new EnumMap<>(SignalAspect.class);

  private MultiButton<LockableSignalBoxBlockEntity.Lock> lockButton;

  private int refreshTimer;

  public ActionSignalBoxScreen(ActionSignalBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName());
    this.signalBox = signalBox;
  }

  @Override
  public void init() {
    int centreX = (this.width - this.windowWidth) / 2;
    int centreY = (this.height - this.windowHeight) / 2;

    this.addSignalAspectButton(SignalAspect.GREEN, centreX + 7, centreY + 30, 50);
    this.addSignalAspectButton(SignalAspect.YELLOW, centreX + 63, centreY + 30, 50);
    this.addSignalAspectButton(SignalAspect.RED, centreX + 119, centreY + 30, 50);
    this.addSignalAspectButton(SignalAspect.BLINK_YELLOW, centreX + 12, centreY + 55, 70);
    this.addSignalAspectButton(SignalAspect.BLINK_RED, centreX + 94, centreY + 55, 70);

    this.lockButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.signalBox.getLock())
        .bounds(centreX + 152, centreY + 8, 16, 16)
        .tooltipFactory(this::updateLockButtonTooltip)
        .stateCallback(this::setLock)
        .build());

    this.updateButtons();
  }

  private void addSignalAspectButton(SignalAspect signalAspect, int x, int y, int width) {
    var actionSignalAspects = this.signalBox.getActionSignalAspects();
    var button = this.addRenderableWidget(ToggleButton
        .toggleBuilder(
            signalAspect.getDisplayName(),
            btn -> ((ToggleButton) btn).setToggled(this.toggleSignalAspect(signalAspect)),
            ButtonTexture.LARGE_BUTTON)
        .bounds(x, y, width, 20)
        .toggled(actionSignalAspects.contains(signalAspect))
        .build());
    this.signalAspectButtons.put(signalAspect, button);
  }

  private void setLock(LockableSignalBoxBlockEntity.Lock lock) {
    if (this.signalBox.getLock() != lock) {
      this.signalBox.setLock(lock);
      this.signalBox.setOwner(lock == LockableSignalBoxBlockEntity.Lock.UNLOCKED
          ? null
          : this.minecraft.getUser().getGameProfile());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(LockableSignalBoxBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.signalBox.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  private boolean toggleSignalAspect(SignalAspect signalAspect) {
    boolean toggled = false;
    if (!this.signalBox.getActionSignalAspects().remove(signalAspect)) {
      this.signalBox.getActionSignalAspects().add(signalAspect);
      toggled = true;
    }
    this.sendAttributes();
    return toggled;
  }

  @Override
  public void tick() {
    super.tick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.refreshTimer = 0;
      this.updateButtons();
    }
  }

  private void updateButtons() {
    boolean canAccess = this.signalBox.canAccess(this.minecraft.getUser().getGameProfile());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.signalBox.getLock());
    this.signalAspectButtons.forEach((signalAspect, button) -> {
      button.active = canAccess;
      button.setToggled(
          this.signalBox.getActionSignalAspects().contains(signalAspect));
    });
  }

  private void sendAttributes() {
    if (!this.signalBox.canAccess(this.minecraft.getUser().getGameProfile())) {
      return;
    }
    NetworkChannel.GAME.sendToServer(
        new SetActionSignalBoxAttributesMessage(this.signalBox.getBlockPos(),
            this.signalBox.getActionSignalAspects(),
            this.lockButton.getState()));
  }
}
