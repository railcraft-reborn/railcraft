package mods.railcraft.client.gui.screen.inventory;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import mods.railcraft.Translations;
import mods.railcraft.client.gui.widget.button.ButtonTexture;
import mods.railcraft.client.gui.widget.button.MultiButton;
import mods.railcraft.client.gui.widget.button.RailcraftButton;
import mods.railcraft.client.gui.widget.button.ToggleButton;
import mods.railcraft.network.PacketHandler;
import mods.railcraft.network.to_server.SetLocomotiveMessage;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive.Speed;
import mods.railcraft.world.inventory.LocomotiveMenu;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class LocomotiveScreen<T extends LocomotiveMenu<?>>
    extends RailcraftMenuScreen<T> {

  private static final int REFRESH_INTERVAL_TICKS = SharedConstants.TICKS_PER_SECOND;

  private final Locomotive locomotive;
  private final String type;
  private final Map<Locomotive.Mode, Button> modeButtons = new EnumMap<>(Locomotive.Mode.class);
  private final Map<Locomotive.Speed, Button> speedButtons = new EnumMap<>(Locomotive.Speed.class);
  private ToggleButton reverseButton;
  private MultiButton<Locomotive.Lock> lockButton;

  private int refreshTimer;

  protected LocomotiveScreen(T menu, Inventory inventory, Component title, String type) {
    super(menu, inventory, title);
    this.locomotive = menu.getLocomotive();
    this.type = type;
    this.imageHeight = LocomotiveMenu.DEFAULT_HEIGHT;
    this.inventoryLabelY = this.imageHeight - 94;
  }

  private Optional<Tooltip> createLockTooltip(Locomotive.Lock lock) {
    return Optional.of(Tooltip.create(switch (lock) {
      case LOCKED -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_LOCKED,
          this.locomotive.getOwnerOrThrow().getName());
      case UNLOCKED -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_UNLOCKED);
      case PRIVATE -> Component.translatable(Translations.Screen.LOCOMOTIVE_LOCK_PRIVATE,
          this.locomotive.getOwnerOrThrow().getName());
    }));
  }

  @Override
  public void init() {
    super.init();

    var centreX = (this.width - this.getXSize()) / 2;
    var centreY = (this.height - this.getYSize()) / 2;

    var layout = LinearLayout.vertical();
    layout.setPosition(centreX + 4, centreY + this.getYSize() - 129);

    var modeLayout = layout.addChild(LinearLayout.horizontal().spacing(2));

    // Mode buttons
    for (var mode : this.locomotive.getSupportedModes()) {
      var translationKey = switch (mode) {
        case IDLE -> Translations.Screen.LOCOMOTIVE_MODE_IDLE;
        case SHUTDOWN -> Translations.Screen.LOCOMOTIVE_MODE_SHUTDOWN;
        case RUNNING -> Translations.Screen.LOCOMOTIVE_MODE_RUNNING;
      };
      var tooltip = Component.translatable(Translations.makeKey("screen",
          String.format("locomotive.%s.mode.description.%s", type, mode.getSerializedName())));
      var button = this.addRenderableWidget(RailcraftButton
          .builder(translationKey, __ -> this.setMode(mode), ButtonTexture.SMALL_BUTTON)
          .size(54, 16)
          .tooltip(Tooltip.create(tooltip))
          .build());
      modeLayout.addChild(button);
      this.modeButtons.put(mode, button);
    }

    var speedLayout = layout.addChild(LinearLayout.horizontal().spacing(4));
    // Reverse button
    this.reverseButton = this.addRenderableWidget(ToggleButton
        .toggleBuilder(Component.literal("R"), __ -> this.toggleReverse(),
            ButtonTexture.SMALL_BUTTON)
        .size(12, 16)
        .toggled(this.locomotive.isReverse())
        .build());
    speedLayout.addChild(this.reverseButton);

    // Speed buttons
    for (var speed : Speed.values()) {
      var button = this.addRenderableWidget(RailcraftButton
          .builder(Component.literal(StringUtils.repeat('>', speed.getLevel())),
              __ -> this.setSpeed(speed),
              ButtonTexture.SMALL_BUTTON)
          .size(4 + speed.getLevel() * 6, 16)
          .build());
      button.active = this.locomotive.getSpeed() == speed;
      speedLayout.addChild(button);
      this.speedButtons.put(speed, button);
    }

    // Lock button
    this.lockButton = this.addRenderableWidget(
        MultiButton.builder(ButtonTexture.SMALL_BUTTON, this.locomotive.getLock())
            .bounds(centreX + 154, centreY + this.getYSize() - 111, 16, 16)
            .tooltipFactory(this::createLockTooltip)
            .stateCallback(this::setLock)
            .build());

    this.updateButtons();

    layout.arrangeElements();
  }

  private void setMode(Locomotive.Mode mode) {
    if (this.locomotive.getMode() != mode) {
      this.locomotive.setMode(mode);
      this.sendAttributes();
    }
  }

  private void setSpeed(Locomotive.Speed speed) {
    if (this.locomotive.getSpeed() != speed) {
      this.locomotive.setSpeed(speed);
      this.sendAttributes();
    }
  }

  private void setLock(Locomotive.Lock lock) {
    if (this.locomotive.getLock() != lock) {
      this.locomotive.setLock(lock);
      this.locomotive.setOwner(lock == Locomotive.Lock.UNLOCKED
          ? null
          : this.minecraft.player.getGameProfile());
      this.sendAttributes();
    }
  }

  private void toggleReverse() {
    this.locomotive.setReverse(!this.locomotive.isReverse());
    this.sendAttributes();
  }

  protected void sendAttributes() {
    this.updateButtons();
    PacketHandler.sendToServer(
        new SetLocomotiveMessage(this.locomotive.getId(),
            this.locomotive.getMode(), this.locomotive.getSpeed(), this.locomotive.getLock(),
            this.locomotive.isReverse()));
  }

  @Override
  public void containerTick() {
    super.containerTick();
    if (this.refreshTimer++ >= REFRESH_INTERVAL_TICKS) {
      this.updateButtons();
    }
  }

  private void updateButtons() {
    this.modeButtons
        .forEach((mode, button) -> button.active = locomotive.getMode() != mode
            && locomotive.isAllowedMode(mode));
    this.speedButtons
        .forEach((speed, button) -> button.active = locomotive.getSpeed() != speed
            && (!locomotive.isReverse()
                || speed.getLevel() <= locomotive.getMaxReverseSpeed().getLevel()));
    this.reverseButton.setToggled(locomotive.isReverse());
    this.lockButton.active = !locomotive.isLocked()
        || locomotive.getOwnerOrThrow().equals(this.minecraft.player.getGameProfile());
    this.lockButton.setState(locomotive.getLock());
  }
}
