package mods.railcraft.client.gui.screen;

import java.util.Collections;
import java.util.Set;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.api.signal.SignalAspect;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetActionSignalBoxAttributesMessage;
import mods.railcraft.world.level.block.entity.signal.ActionSignalBoxBlockEntity;
import mods.railcraft.world.level.block.entity.signal.SecureSignalBoxBlockEntity;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ActionSignalBoxScreen extends BasicIngameScreen {

  private final ActionSignalBoxBlockEntity signalBox;

  private MultiButton<SecureSignalBoxBlockEntity.Lock> lockButton;

  private ITextComponent lockButtonTooltip;

  public ActionSignalBoxScreen(ActionSignalBoxBlockEntity signalBox) {
    super(signalBox.getDisplayName());
    this.signalBox = signalBox;
  }

  @Override
  public void init() {
    int centreX = (this.width - this.x) / 2;
    int centreY = (this.height - this.y) / 2;
    Set<SignalAspect> actionSignalAspects = this.signalBox.getActionSignalAspects();
    this.addButton(new ToggleButton(centreX + 7, centreY + 30, 50, 20,
        SignalAspect.GREEN.getDisplayName(),
        btn -> this.toggleSignalAspect((ToggleButton) btn, SignalAspect.GREEN),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(SignalAspect.GREEN)));
    this.addButton(new ToggleButton(centreX + 12, centreY + 55, 70, 20,
        SignalAspect.BLINK_YELLOW.getDisplayName(),
        btn -> this.toggleSignalAspect((ToggleButton) btn, SignalAspect.BLINK_YELLOW),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(SignalAspect.BLINK_YELLOW)));
    this.addButton(new ToggleButton(centreX + 63, centreY + 30, 50, 20,
        SignalAspect.YELLOW.getDisplayName(),
        btn -> this.toggleSignalAspect((ToggleButton) btn, SignalAspect.YELLOW),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(SignalAspect.YELLOW)));
    this.addButton(new ToggleButton(centreX + 94, centreY + 55, 70, 20,
        SignalAspect.BLINK_RED.getDisplayName(),
        btn -> this.toggleSignalAspect((ToggleButton) btn, SignalAspect.BLINK_RED),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(SignalAspect.BLINK_RED)));
    this.addButton(new ToggleButton(centreX + 119, centreY + 30, 50, 20,
        SignalAspect.RED.getDisplayName(),
        btn -> this.toggleSignalAspect((ToggleButton) btn, SignalAspect.RED),
        ButtonTexture.LARGE_BUTTON,
        actionSignalAspects.contains(SignalAspect.RED)));
    this.addButton(this.lockButton = new MultiButton<>(centreX + 152, centreY + 8, 16, 16,
        this.signalBox.getLockController(), __ -> {
          if (this.signalBox.getLockController()
              .getCurrentState() == SecureSignalBoxBlockEntity.Lock.LOCKED) {
            this.signalBox.setOwner(this.minecraft.getUser().getGameProfile());
          }
          this.sendAttributes();
        },
        this::renderLockButtonTooltip));

    this.updateButtons();
    this.updateLockButtonTooltip();
  }

  private void updateLockButtonTooltip() {
    final SecureSignalBoxBlockEntity.Lock lock =
        this.lockButton.getController().getCurrentState();
    switch (lock) {
      case LOCKED:
        this.lockButtonTooltip =
            new TranslationTextComponent("screen.action_signal_box.lock.locked",
                this.signalBox.getOwnerOrThrow().getName());
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

  private void toggleSignalAspect(ToggleButton button, SignalAspect signalAspect) {
    boolean toggled = false;
    if (!this.signalBox.getActionSignalAspects().remove(signalAspect)) {
      this.signalBox.getActionSignalAspects().add(signalAspect);
      toggled = true;
    }
    button.setToggled(toggled);
    this.sendAttributes();
  }

  @Override
  public void tick() {
    super.tick();
    this.updateButtons();
    this.updateLockButtonTooltip();
  }

  private void updateButtons() {
    boolean canAccess = this.signalBox.canAccess(this.minecraft.getUser().getGameProfile());
    this.buttons.forEach(widget -> widget.active = canAccess);
  }

  private void sendAttributes() {
    if (!this.signalBox.canAccess(this.minecraft.getUser().getGameProfile())) {
      return;
    }
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(
        new SetActionSignalBoxAttributesMessage(this.signalBox.getBlockPos(),
            this.signalBox.getActionSignalAspects(),
            this.lockButton.getController().getCurrentState()));
  }
}
