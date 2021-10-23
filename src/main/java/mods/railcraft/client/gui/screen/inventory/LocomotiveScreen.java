/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 https://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at https://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.gui.screen.inventory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity.Mode;
import mods.railcraft.world.entity.cart.locomotive.LocomotiveEntity.Speed;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class LocomotiveScreen<T extends LocomotiveMenu<?>>
    extends RailcraftMenuScreen<T> {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private final String typeTag;
  private final Map<LocomotiveEntity.Mode, Button> modeButtons =
      new EnumMap<>(LocomotiveEntity.Mode.class);
  private final Map<LocomotiveEntity.Speed, Button> speedButtons =
      new EnumMap<>(LocomotiveEntity.Speed.class);
  private ToggleButton reverseButton;

  private MultiButton<LocomotiveEntity.Lock> lockButton;
  private ITextComponent lockButtonTooltip;

  private int refreshTimer;

  protected LocomotiveScreen(T menu, PlayerInventory inv, ITextComponent title, String typeTag) {
    super(menu, inv, title);
    this.typeTag = typeTag;
    this.imageHeight = LocomotiveMenu.DEFAULT_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  private void updateLockButtonTooltip() {
    final LocomotiveEntity.Lock lock = this.lockButton.getState();
    switch (lock) {
      case LOCKED:
        this.lockButtonTooltip = new TranslationTextComponent("screen.locomotive.lock.locked",
            this.menu.getLocomotive().getOwnerOrThrow().getName());
        break;
      case UNLOCKED:
        this.lockButtonTooltip = new TranslationTextComponent("screen.locomotive.lock.unlocked");
        break;
      case PRIVATE:
        this.lockButtonTooltip = new TranslationTextComponent("screen.locomotive.lock.private",
            this.menu.getLocomotive().getOwnerOrThrow().getName());
        break;
      default:
        break;
    }
  }

  @Override
  public void init() {
    super.init();

    int centreX = (this.width - this.getXSize()) / 2;
    int centreY = (this.height - this.getYSize()) / 2;

    // Mode buttons
    for (Mode mode : this.getMenu().getLocomotive().getSupportedModes()) {
      Button button = new RailcraftButton(0, centreY + this.getYSize() - 129, 55, 16,
          new TranslationTextComponent(
              "screen.locomotive.mode." + mode.getSerializedName()),
          b -> this.setMode(mode),
          (btn, matrixStack, mouseX, mouseY) -> this.renderWrappedToolTip(
              matrixStack, Collections.singletonList(new TranslationTextComponent(
                  "screen.locomotive." + typeTag + ".mode.desription."
                      + mode.getSerializedName())),
              mouseX, mouseY, this.font),
          ButtonTexture.SMALL_BUTTON);
      this.modeButtons.put(mode, button);
    }
    GuiUtil.newButtonRowAuto(this::addButton, centreX + 3, 171, this.modeButtons.values());

    // Reverse button
    this.addButton(this.reverseButton =
        new ToggleButton(centreX + 4, centreY + this.getYSize() - 112, 12, 16,
            new StringTextComponent("R"), __ -> this.toggleReverse(),
            ButtonTexture.SMALL_BUTTON, this.getMenu().getLocomotive().isReverse()));

    // Speed buttons
    for (Speed speed : Speed.values()) {
      String label =
          IntStream.range(0, speed.getLevel()).mapToObj(i -> ">").collect(Collectors.joining());
      Button button = new RailcraftButton(0, centreY + this.getYSize() - 112,
          7 + speed.getLevel() * 5, 16, new StringTextComponent(label), b -> this.setSpeed(speed),
          ButtonTexture.SMALL_BUTTON);
      button.active = this.menu.getLocomotive().getSpeed() == speed;
      this.speedButtons.put(speed, button);
    }
    GuiUtil.newButtonRow(this::addButton, centreX + 21, 5, this.speedButtons.values());

    // Lock button
    this.addButton(this.lockButton =
        new MultiButton<>(centreX + 152, centreY + this.getYSize() - 111, 16, 16,
            this.menu.getLocomotive().getLock(),
            __ -> this.setLock(this.lockButton.getState()),
            this::renderLockButtonTooltip));

    this.updateLockButtonTooltip();
    this.updateButtons();
  }

  private void renderLockButtonTooltip(
      Button button, MatrixStack matrixStack, int mouseX, int mouseY) {
    this.renderWrappedToolTip(matrixStack, Collections.singletonList(this.lockButtonTooltip),
        mouseX, mouseY, this.font);
  }

  private void setMode(LocomotiveEntity.Mode mode) {
    if (this.getMenu().getLocomotive().getMode() != mode) {
      this.getMenu().getLocomotive().setMode(mode);
      this.sendAttributes();
    }
  }

  private void setSpeed(LocomotiveEntity.Speed speed) {
    if (this.getMenu().getLocomotive().getSpeed() != speed) {
      this.getMenu().getLocomotive().setSpeed(speed);
      this.sendAttributes();
    }
  }

  private void setLock(LocomotiveEntity.Lock lock) {
    if (this.getMenu().getLocomotive().getLock() != lock) {
      this.getMenu().getLocomotive().setLock(lock);
      this.menu.getLocomotive().setOwner(
          lock == LocomotiveEntity.Lock.UNLOCKED ? null
              : this.minecraft.getUser().getGameProfile());
      this.updateLockButtonTooltip();
      this.sendAttributes();
    }
  }

  private void toggleReverse() {
    this.getMenu().getLocomotive().setReverse(!this.getMenu().getLocomotive().isReverse());
    this.sendAttributes();
  }

  protected void sendAttributes() {
    this.updateButtons();
    this.updateLockButtonTooltip();
    LocomotiveEntity locomotive = this.getMenu().getLocomotive();
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(
        new SetLocomotiveAttributesMessage(this.menu.getLocomotive().getId(),
            locomotive.getMode(), locomotive.getSpeed(), locomotive.getLock(),
            locomotive.isReverse()));
  }

  @Override
  public void tick() {
    super.tick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.updateButtons();
      this.updateLockButtonTooltip();
    }
  }

  private void updateButtons() {
    LocomotiveEntity locomotive = this.getMenu().getLocomotive();
    this.modeButtons
        .forEach((mode, button) -> button.active = locomotive.getMode() != mode
            && locomotive.isAllowedMode(mode));
    this.speedButtons
        .forEach((speed, button) -> button.active = locomotive.getSpeed() != speed
            && (!locomotive.isReverse()
                || speed.getLevel() <= locomotive.getMaxReverseSpeed().getLevel()));
    this.reverseButton.setToggled(locomotive.isReverse());
    this.lockButton.active = !locomotive.isLocked()
        || locomotive.getOwnerOrThrow().equals(this.minecraft.getUser().getGameProfile());
    this.lockButton.setState(locomotive.getLock());
  }
}
