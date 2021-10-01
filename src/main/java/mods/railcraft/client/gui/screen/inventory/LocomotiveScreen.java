/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2019
 http://railcraft.info

 This code is the property of CovertJaguar
 and may only be used with explicit written
 permission unless otherwise specified on the
 license page at http://railcraft.info/wiki/info:license.
 -----------------------------------------------------------------------------*/
package mods.railcraft.client.gui.screen.inventory;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.mojang.blaze3d.matrix.MatrixStack;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.GuiMultiButton;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.util.GuiUtil;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetLocomotiveAttributesMessage;
import mods.railcraft.world.entity.LocomotiveEntity;
import mods.railcraft.world.entity.LocomotiveEntity.Mode;
import mods.railcraft.world.entity.LocomotiveEntity.Speed;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class LocomotiveScreen<T extends LocomotiveMenu<?>>
    extends RailcraftMenuScreen<T> {

  private final String typeTag;
  private final Map<LocomotiveEntity.Mode, Button> modeButtons =
      new EnumMap<>(LocomotiveEntity.Mode.class);
  private final Map<LocomotiveEntity.Speed, Button> speedButtons =
      new EnumMap<>(LocomotiveEntity.Speed.class);
  private Button reverseButton;

  private GuiMultiButton<?> lockButton;
  private List<? extends ITextProperties> lockedTooltip;
  private List<? extends ITextProperties> unlockedTooltip;
  private List<? extends ITextProperties> privateTooltip;

  private LocomotiveEntity.Mode mode;
  private LocomotiveEntity.Speed speed;
  private boolean reverse;

  protected LocomotiveScreen(T menu, PlayerInventory inv, ITextComponent title, String typeTag) {
    super(menu, inv, title);
    this.typeTag = typeTag;
    this.mode = menu.getLocomotive().getMode();
    this.speed = menu.getLocomotive().getSpeed();
    this.reverse = menu.getLocomotive().isReverse();

    this.imageHeight = LocomotiveMenu.DEFAULT_HEIGHT;

    this.lockedTooltip = Collections.singletonList(
        new TranslationTextComponent("screen.locomotive.lock.locked",
            menu.getLocomotive().getClientOwnerName()));
    this.unlockedTooltip = Collections.singletonList(
        new TranslationTextComponent("screen.locomotive.lock.unlocked",
            menu.getLocomotive().getClientOwnerName()));
    this.privateTooltip = Collections.singletonList(
        new TranslationTextComponent("screen.locomotive.lock.private",
            menu.getLocomotive().getClientOwnerName()));
  }

  @Override
  public void init() {
    super.init();
    if (menu.getLocomotive() == null)
      return;
    int w = (width - this.getXSize()) / 2;
    int h = (height - this.getYSize()) / 2;

    // Mode buttons
    for (Mode mode : menu.getLocomotive().getAllowedModes()) {
      Button button = new RailcraftButton(0, h + this.getYSize() - 129, 55, 16,
          new TranslationTextComponent(
              "screen.locomotive.mode." + mode.getSerializedName()),
          b -> this.setMode(mode),
          (btn, matrixStack, mouseX, mouseY) -> this.renderWrappedToolTip(
              matrixStack, Collections.singletonList(new TranslationTextComponent(
                  "screen.locomotive." + typeTag + ".tips.button.mode."
                      + mode.getSerializedName())),
              mouseX, mouseY, this.font),
          ButtonTexture.SMALL_BUTTON);
      this.modeButtons.put(mode, button);
    }
    GuiUtil.newButtonRowAuto(this::addButton, w + 3, 171, this.modeButtons.values());

    // Reverse button
    this.addButton(this.reverseButton =
        new RailcraftButton(0, h + this.getYSize() - 112, 12, 16, new StringTextComponent("R"),
            b -> this.setReverse(!this.reverse), ButtonTexture.SMALL_BUTTON));
    this.reverseButton.active = this.menu.getLocomotive().isReverse();

    // Speed buttons
    for (Speed speed : Speed.values()) {
      String label =
          IntStream.range(0, speed.getLevel()).mapToObj(i -> ">").collect(Collectors.joining());
      Button button = new RailcraftButton(0, h + this.getYSize() - 112,
          7 + speed.getLevel() * 5, 16, new StringTextComponent(label), b -> this.setSpeed(speed),
          ButtonTexture.SMALL_BUTTON);
      button.active = this.menu.getLocomotive().getSpeed() == speed;
      this.speedButtons.put(speed, button);
    }
    GuiUtil.newButtonRow(this::addButton, w + 8, 3, this.speedButtons.values());

    // Lock button
    this.addButton(this.lockButton =
        new GuiMultiButton<>(w + 152, h + this.getYSize() - 111, 16, 16,
            menu.getLocomotive().getLockController(), b -> this.sendAttributes(),
            this::renderLockTooltip, ButtonTexture.LOCKED_BUTTON));
    this.lockButton.active = true;

    this.updateButtons();
  }

  private void setMode(LocomotiveEntity.Mode mode) {
    this.mode = mode;
    this.sendAttributes();
  }

  private void setSpeed(LocomotiveEntity.Speed speed) {
    this.speed = speed;
    this.sendAttributes();
  }

  private void setReverse(boolean reverse) {
    this.reverse = reverse;
    this.sendAttributes();
  }

  protected void sendAttributes() {
    if (menu.getLocomotive() == null)
      return;
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(
        new SetLocomotiveAttributesMessage(this.menu.getLocomotive().getId(),
            this.mode, this.speed,
            this.menu.getLocomotive().getLockController().getCurrentStateIndex(),
            this.reverse));
  }

  @Override
  public void tick() {
    super.tick();
    updateButtons();
  }

  private void updateButtons() {
    this.modeButtons.forEach((mode, button) -> button.active = this.mode != mode);
    this.speedButtons.forEach((speed, button) -> button.active = this.speed != speed);
    // if (ownerName != null && !ownerName.equals(locoOwner)) {
    // this.locoOwner = ownerName;
    // this.lockedTooltip =
    // Collections.singletonList(new TranslationTextComponent(
    // "gui.railcraft.locomotive.tips.button.locked", ownerName));
    // this.unlockedTooltip =
    // Collections.singletonList(new TranslationTextComponent(
    // "gui.railcraft.locomotive.tips.button.unlocked", ownerName));
    // this.privateTooltip =
    // Collections.singletonList(new TranslationTextComponent(
    // "gui.railcraft.locomotive.tips.button.private", ownerName));
    // }
  }

  private void renderLockTooltip(Button button, MatrixStack matrixStack, int mouseX, int mouseY) {
    List<? extends ITextProperties> tooltip = this.menu.getLocomotive().isPrivate()
        ? this.privateTooltip
        : this.menu.getLocomotive().isSecure()
            ? this.lockedTooltip
            : this.lockButton.active ? this.unlockedTooltip : null;
    if (tooltip != null) {
      this.renderWrappedToolTip(matrixStack, tooltip, mouseX, mouseY, font);
    }
  }
}
