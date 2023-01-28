package mods.railcraft.client.gui.screen.inventory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import mods.railcraft.Railcraft;
import mods.railcraft.Translations;
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
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
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

  private int refreshTimer;

  protected LocomotiveScreen(T menu, Inventory inventory, Component title, String typeTag) {
    super(menu, inventory, title);
    this.typeTag = typeTag;// TODO: Change to an ENUM
    this.imageHeight = LocomotiveMenu.DEFAULT_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  private Optional<Tooltip> createLockTooltip(Locomotive.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_LOCKED,
          this.menu.getLocomotive().getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_UNLOCKED);
      case PRIVATE -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_PRIVATE,
          this.menu.getLocomotive().getOwnerOrThrow().getName());
    }));
  }

  @Override
  public void init() {
    super.init();

    var centreX = (this.width - this.getXSize()) / 2;
    var centreY = (this.height - this.getYSize()) / 2;

    // Mode buttons
    for (var mode : this.getMenu().getLocomotive().getSupportedModes()) {
      var text = switch (mode) {
        case IDLE -> Translations.Screen.LOCOMOTIVE_MODE_IDLE;
        case SHUTDOWN -> Translations.Screen.LOCOMOTIVE_MODE_SHUTDOWN;
        case RUNNING -> Translations.Screen.LOCOMOTIVE_MODE_RUNNING;
      };
      var button = RailcraftButton
          .builder(
              Component.translatable(text),
              __ -> this.setMode(mode),
              ButtonTexture.SMALL_BUTTON)
          .pos(0, centreY + this.getYSize() - 129)
          .size(55, 16)
          .tooltip(Tooltip.create(Component.translatable(
              "screen." + Railcraft.ID + ".locomotive." + typeTag + ".mode.description."
                  + mode.getSerializedName())))
          .build();
      this.modeButtons.put(mode, button);
    }
    GuiUtil.newButtonRowAuto(this::addRenderableWidget, centreX + 3, 171,
        this.modeButtons.values());

    // Reverse button
    this.reverseButton = this.addRenderableWidget(ToggleButton
        .toggleBuilder(
            Component.literal("R"),
            __ -> this.toggleReverse(),
            ButtonTexture.SMALL_BUTTON)
        .bounds(centreX + 4, centreY + this.getYSize() - 112, 12, 16)
        .toggled(this.getMenu().getLocomotive().isReverse())
        .build());

    // Speed buttons
    for (var speed : Speed.values()) {
      var label =
          IntStream.range(0, speed.getLevel()).mapToObj(i -> ">").collect(Collectors.joining());

      var button = RailcraftButton
          .builder(
              Component.literal(label),
              __ -> this.setSpeed(speed),
              ButtonTexture.SMALL_BUTTON)
          .pos(0, centreY + this.getYSize() - 112)
          .size(7 + speed.getLevel() * 5, 16)
          .build();
      button.active = this.menu.getLocomotive().getSpeed() == speed;

      this.speedButtons.put(speed, button);
    }
    GuiUtil.newButtonRow(this::addRenderableWidget, centreX + 21, 5, this.speedButtons.values());

    // Lock button
    this.lockButton = this.addRenderableWidget(
        MultiButton.builder(ButtonTexture.SMALL_BUTTON, this.menu.getLocomotive().getLock())
            .bounds(centreX + 152, centreY + this.getYSize() - 111, 16, 16)
            .tooltipFactory(this::createLockTooltip)
            .stateCallback(this::setLock)
            .build());

    this.updateButtons();
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
      this.menu.getLocomotive().setOwner(lock == Locomotive.Lock.UNLOCKED
          ? null
          : this.minecraft.getUser().getGameProfile());
      this.sendAttributes();
    }
  }

  private void toggleReverse() {
    this.getMenu().getLocomotive().setReverse(!this.getMenu().getLocomotive().isReverse());
    this.sendAttributes();
  }

  protected void sendAttributes() {
    this.updateButtons();
    Locomotive locomotive = this.getMenu().getLocomotive();
    NetworkChannel.GAME.sendToServer(
        new SetLocomotiveAttributesMessage(this.menu.getLocomotive().getId(),
            locomotive.getMode(), locomotive.getSpeed(), locomotive.getLock(),
            locomotive.isReverse()));
  }

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.updateButtons();
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
