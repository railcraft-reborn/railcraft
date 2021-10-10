package mods.railcraft.client.gui.screen;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetSwitchTrackMotorAttributesMessage;
import mods.railcraft.world.level.block.entity.LockableSwitchTrackActuatorBlockEntity;
import mods.railcraft.world.level.block.entity.SwitchTrackMotorBlockEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SwitchTrackMotorScreen extends IngameWindowScreen {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private final SwitchTrackMotorBlockEntity switchTrackMotor;

  private final Map<SignalAspect, ToggleButton> signalAspectButtons =
      new EnumMap<>(SignalAspect.class);

  private MultiButton<LockableSwitchTrackActuatorBlockEntity.Lock> lockButton;
  private ITextComponent lockButtonTooltip;

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

    this.addButton(
        this.redstoneTriggeredButton = new ToggleButton(centreX + 34, centreY + 80, 110, 20,
            new TranslationTextComponent("screen.switch_track_motor.redstone_triggered"),
            button -> ((ToggleButton) button).setToggled(this.toggleRedstoneTriggered()),
            ButtonTexture.LARGE_BUTTON,
            this.switchTrackMotor.isRedstoneTriggered()));

    this.addButton(this.lockButton = new MultiButton<>(centreX + 152, centreY + 8, 16, 16,
        this.switchTrackMotor.getLock(),
        __ -> this.setLock(this.lockButton.getState()),
        this::renderLockButtonTooltip));

    this.updateButtons();
    this.updateLockButtonTooltip();
  }

  private void addSignalAspectButton(SignalAspect signalAspect, int x, int y, int width) {
    Set<SignalAspect> actionSignalAspects = this.switchTrackMotor.getActionSignalAspects();
    ToggleButton button = new ToggleButton(x, y, width, 20, signalAspect.getDisplayName(),
        btn -> ((ToggleButton) btn).setToggled(this.toggleSignalAspect(signalAspect)),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(signalAspect));
    this.addButton(button);
    this.signalAspectButtons.put(signalAspect, button);
  }

  private void setLock(LockableSwitchTrackActuatorBlockEntity.Lock lock) {
    if (this.switchTrackMotor.getLock() != lock) {
      this.switchTrackMotor.setLock(lock);
      this.switchTrackMotor.setOwner(lock == LockableSwitchTrackActuatorBlockEntity.Lock.UNLOCKED
          ? null
          : this.minecraft.getUser().getGameProfile());
      this.updateLockButtonTooltip();
      this.sendAttributes();
    }
  }

  private void updateLockButtonTooltip() {
    final LockableSwitchTrackActuatorBlockEntity.Lock lock = this.lockButton.getState();
    switch (lock) {
      case LOCKED:
        this.lockButtonTooltip =
            new TranslationTextComponent("screen.action_signal_box.lock.locked",
                this.switchTrackMotor.getOwnerOrThrow().getName());
        break;
      case UNLOCKED:
        this.lockButtonTooltip =
            new TranslationTextComponent("screen.action_signal_box.lock.unlocked");
        break;
      default:
        break;
    }
  }

  private void renderLockButtonTooltip(Button button, MatrixStack matrixStack,
      int mouseX, int mouseY) {
    this.renderWrappedToolTip(matrixStack, Collections.singletonList(this.lockButtonTooltip),
        mouseX, mouseY, this.font);
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
      this.updateLockButtonTooltip();
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
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(
        new SetSwitchTrackMotorAttributesMessage(this.switchTrackMotor.getBlockPos(),
            this.switchTrackMotor.getActionSignalAspects(),
            this.switchTrackMotor.isRedstoneTriggered(),
            this.lockButton.getState()));
  }
}
