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
import mods.railcraft.network.play.SetSwitchTrackMotorAttributesMessage;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;

public class SwitchTrackMotorScreen extends IngameWindowScreen {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private final SwitchTrackMotorBlockEntity switchTrackMotor;

  private final Map<SignalAspect, ToggleButton> signalAspectButtons =
      new EnumMap<>(SignalAspect.class);

  private MultiButton<LockableSwitchTrackActuatorBlockEntity.Lock> lockButton;

  private ToggleButton redstoneTriggeredButton;

  private int refreshTimer;

  public SwitchTrackMotorScreen(SwitchTrackMotorBlockEntity signalBox) {
    super(signalBox.getDisplayName(), LARGE_WINDOW_TEXTURE, DEFAULT_WINDOW_WIDTH,
        LARGE_WINDOW_HEIGHT);
    this.switchTrackMotor = signalBox;
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

    this.redstoneTriggeredButton = this.addRenderableWidget(ToggleButton
        .toggleBuilder(
            Component.translatable(Translations.Screen.SWITCH_TRACK_MOTOR_REDSTONE),
            button -> ((ToggleButton) button).setToggled(this.toggleRedstoneTriggered()),
            ButtonTexture.LARGE_BUTTON)
        .toggled(this.switchTrackMotor.isRedstoneTriggered())
        .bounds(centreX + 34, centreY + 80, 110, 20)
        .build());

    this.lockButton = this.addRenderableWidget(MultiButton
        .builder(ButtonTexture.SMALL_BUTTON, this.switchTrackMotor.getLock())
        .bounds(centreX + 152, centreY + 8, 16, 16)
        .tooltipFactory(this::updateLockButtonTooltip)
        .stateCallback(this::setLock)
        .build());

    this.updateButtons();
  }

  private void addSignalAspectButton(SignalAspect signalAspect, int x, int y, int width) {
    var actionSignalAspects = this.switchTrackMotor.getActionSignalAspects();
    var button = this.addRenderableWidget(ToggleButton
        .toggleBuilder(
            signalAspect.getDisplayName(),
            btn -> ((ToggleButton) btn).setToggled(this.toggleSignalAspect(signalAspect)),
            ButtonTexture.LARGE_BUTTON)
        .toggled(actionSignalAspects.contains(signalAspect))
        .bounds(x, y, width, 20)
        .build());
    this.signalAspectButtons.put(signalAspect, button);
  }

  private void setLock(LockableSwitchTrackActuatorBlockEntity.Lock lock) {
    if (this.switchTrackMotor.getLock() != lock) {
      this.switchTrackMotor.setLock(
          lock == LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED
          ? null : this.minecraft.getUser().getGameProfile());
      this.sendAttributes();
    }
  }

  private Optional<Tooltip> updateLockButtonTooltip(
      LockableSwitchTrackActuatorBlockEntity.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_LOCKED,
          this.switchTrackMotor.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.ACTION_SIGNAL_BOX_UNLOCKED);
    }));
  }

  private boolean toggleSignalAspect(SignalAspect signalAspect) {
    boolean toggled = false;
    if (!this.switchTrackMotor.getActionSignalAspects().remove(signalAspect)) {
      this.switchTrackMotor.getActionSignalAspects().add(signalAspect);
      toggled = true;
    }
    this.sendAttributes();
    return toggled;
  }

  private boolean toggleRedstoneTriggered() {
    boolean toggled = !this.switchTrackMotor.isRedstoneTriggered();
    this.switchTrackMotor.setRedstoneTriggered(toggled);
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
    boolean canAccess = this.switchTrackMotor.canAccess(this.minecraft.getUser().getGameProfile());
    this.lockButton.active = canAccess;
    this.lockButton.setState(this.switchTrackMotor.getLock());
    this.signalAspectButtons.forEach((signalAspect, button) -> {
      button.active = canAccess;
      button.setToggled(
          this.switchTrackMotor.getActionSignalAspects().contains(signalAspect));
    });
    this.redstoneTriggeredButton.active = canAccess;
    this.redstoneTriggeredButton.setToggled(this.switchTrackMotor.isRedstoneTriggered());
  }

  private void sendAttributes() {
    if (!this.switchTrackMotor.canAccess(this.minecraft.getUser().getGameProfile())) {
      return;
    }
    NetworkChannel.GAME.sendToServer(
        new SetSwitchTrackMotorAttributesMessage(this.switchTrackMotor.getBlockPos(),
            this.switchTrackMotor.getActionSignalAspects(),
            this.switchTrackMotor.isRedstoneTriggered(),
            this.lockButton.getState()));
  }
}
