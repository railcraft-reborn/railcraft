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
import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive.Speed;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Inventory;

public abstract class LocomotiveScreen<T extends LocomotiveMenu<?>>
    extends RailcraftMenuScreen<T> {

  private static final int REFRESH_INTERVAL_TICKS = 20;

  private final String typeTag;
  private final Map<Locomotive.Mode, Button> modeButtons =
      new EnumMap<>(Locomotive.Mode.class);
  private final Map<Locomotive.Speed, Button> speedButtons =
      new EnumMap<>(Locomotive.Speed.class);
  private ToggleButton reverseButton;

  private MultiButton<Locomotive.Lock> lockButton;
  private Component lockButtonTooltip;

  private int refreshTimer;

  protected LocomotiveScreen(T menu, Inventory inventory, Component title, String typeTag) {
    super(menu, inventory, title);
    this.typeTag = typeTag;
    this.imageHeight = LocomotiveMenu.DEFAULT_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  private void updateLockButtonTooltip() {
    final Locomotive.Lock lock = this.lockButton.getState();
    switch (lock) {
      case LOCKED:
        this.lockButtonTooltip = new TranslatableComponent("screen.locomotive.lock.locked",
            this.menu.getLocomotive().getOwnerOrThrow().getName());
        break;
      case UNLOCKED:
        this.lockButtonTooltip = new TranslatableComponent("screen.locomotive.lock.unlocked");
        break;
      case PRIVATE:
        this.lockButtonTooltip = new TranslatableComponent("screen.locomotive.lock.private",
            this.menu.getLocomotive().getOwnerOrThrow().getName());
        break;
      default:
        break;
    }
  }

  @Override
  public void init() {
    super.init();

    var centreX = (this.width - this.getXSize()) / 2;
    var centreY = (this.height - this.getYSize()) / 2;

    // Mode buttons
    for (var mode : this.getMenu().getLocomotive().getSupportedModes()) {
      var button = new RailcraftButton(0, centreY + this.getYSize() - 129, 55, 16,
          new TranslatableComponent(
              "screen.locomotive.mode." + mode.getSerializedName()),
          b -> this.setMode(mode),
          (btn, matrixStack, mouseX, mouseY) -> this.renderComponentTooltip(
              matrixStack, Collections.singletonList(new TranslatableComponent(
                  "screen.locomotive." + typeTag + ".mode.desription."
                      + mode.getSerializedName())),
              mouseX, mouseY, this.font),
          ButtonTexture.SMALL_BUTTON);
      this.modeButtons.put(mode, button);
    }
    GuiUtil.newButtonRowAuto(this::addRenderableWidget, centreX + 3, 171, this.modeButtons.values());

    // Reverse button
    this.addRenderableWidget(this.reverseButton =
        new ToggleButton(centreX + 4, centreY + this.getYSize() - 112, 12, 16,
            new TextComponent("R"), __ -> this.toggleReverse(),
            ButtonTexture.SMALL_BUTTON, this.getMenu().getLocomotive().isReverse()));

    // Speed buttons
    for (var speed : Speed.values()) {
      var label =
          IntStream.range(0, speed.getLevel()).mapToObj(i -> ">").collect(Collectors.joining());
      var button = new RailcraftButton(0, centreY + this.getYSize() - 112,
          7 + speed.getLevel() * 5, 16, new TextComponent(label), b -> this.setSpeed(speed),
          ButtonTexture.SMALL_BUTTON);
      button.active = this.menu.getLocomotive().getSpeed() == speed;
      this.speedButtons.put(speed, button);
    }
    GuiUtil.newButtonRow(this::addRenderableWidget, centreX + 21, 5, this.speedButtons.values());

    // Lock button
    this.addRenderableWidget(this.lockButton =
        new MultiButton<>(centreX + 152, centreY + this.getYSize() - 111, 16, 16,
            this.menu.getLocomotive().getLock(),
            this::renderComponentTooltip,
            __ -> this.setLock(this.lockButton.getState()),
            this::renderLockButtonTooltip));

    this.updateLockButtonTooltip();
    this.updateButtons();
  }

  private void renderLockButtonTooltip(
      Button button, PoseStack poseStack, int mouseX, int mouseY) {
    this.renderComponentTooltip(poseStack, Collections.singletonList(this.lockButtonTooltip),
        mouseX, mouseY, this.font);
  }

  private void setMode(Locomotive.Mode mode) {
    if (this.getMenu().getLocomotive().getMode() != mode) {
      this.getMenu().getLocomotive().setMode(mode);
      this.sendAttributes();
    }
  }

  private void setSpeed(Locomotive.Speed speed) {
    if (this.getMenu().getLocomotive().getSpeed() != speed) {
      this.getMenu().getLocomotive().setSpeed(speed);
      this.sendAttributes();
    }
  }

  private void setLock(Locomotive.Lock lock) {
    if (this.getMenu().getLocomotive().getLock() != lock) {
      this.getMenu().getLocomotive().setLock(lock);
      this.menu.getLocomotive().setOwner(
          lock == Locomotive.Lock.UNLOCKED ? null
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
    Locomotive locomotive = this.getMenu().getLocomotive();
    NetworkChannel.GAME.getSimpleChannel().sendToServer(
        new SetLocomotiveAttributesMessage(this.menu.getLocomotive().getId(),
            locomotive.getMode(), locomotive.getSpeed(), locomotive.getLock(),
            locomotive.isReverse()));
  }

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.updateButtons();
      this.updateLockButtonTooltip();
    }
  }

  private void updateButtons() {
    Locomotive locomotive = this.getMenu().getLocomotive();
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
